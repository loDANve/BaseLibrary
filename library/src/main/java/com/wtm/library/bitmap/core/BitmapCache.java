package com.wtm.library.bitmap.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

import com.wtm.library.bitmap.BitmapDisplayConfig;
import com.wtm.library.bitmap.BitmapGlobalConfig;
import com.wtm.library.bitmap.WBitmap;
import com.wtm.library.bitmap.factory.BitmapFactory;
import com.wtm.library.cache.FileNameGenerator;
import com.wtm.library.cache.LruDiskCache;
import com.wtm.library.cache.LruMemoryCache;
import com.wtm.library.utils.IOUtils;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.OtherUtils;


public class BitmapCache {

	private final int DISK_CACHE_INDEX = 0;

	private LruDiskCache mDiskLruCache;
	private LruMemoryCache<MemoryCacheKey, Bitmap> mMemoryCache;

	private final Object mDiskCacheLock = new Object();

	private BitmapGlobalConfig globalConfig;

	public BitmapCache(BitmapGlobalConfig globalConfig) {
		if (globalConfig == null)
			throw new IllegalArgumentException("globalConfig may not be null");
		this.globalConfig = globalConfig;
	}

	public void initMemoryCache() {
		if (!globalConfig.isMemoryCacheEnabled())
			return;

		if (mMemoryCache != null) {
			try {
				clearMemoryCache();
			} catch (Throwable e) {
			}
		}
		mMemoryCache = new LruMemoryCache<MemoryCacheKey, Bitmap>(
				globalConfig.getMemoryCacheSize()) {
			@Override
			protected int sizeOf(MemoryCacheKey key, Bitmap bitmap) {
				if (bitmap == null)
					return 0;
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
	}

	public void initDiskCache() {
		synchronized (mDiskCacheLock) {
			if (globalConfig.isDiskCacheEnabled()
					&& (mDiskLruCache == null || mDiskLruCache.isClosed())) {
				File diskCacheDir = new File(globalConfig.getDiskCachePath());
				if (diskCacheDir.exists() || diskCacheDir.mkdirs()) {
					long availableSpace = OtherUtils
							.getAvailableSpace(diskCacheDir);
					long diskCacheSize = globalConfig.getDiskCacheSize();
					diskCacheSize = availableSpace > diskCacheSize ? diskCacheSize
							: availableSpace;
					try {
						mDiskLruCache = LruDiskCache.open(diskCacheDir, 1, 1,
								diskCacheSize);
						mDiskLruCache.setFileNameGenerator(globalConfig
								.getFileNameGenerator());
						LogUtils.d("create disk cache success");
					} catch (Throwable e) {
						mDiskLruCache = null;
						LogUtils.e("create disk cache error", e);
					}
				}
			}
		}
	}

	public void setMemoryCacheSize(int maxSize) {
		if (mMemoryCache != null) {
			mMemoryCache.setMaxSize(maxSize);
		}
	}

	public void setDiskCacheSize(int maxSize) {
		synchronized (mDiskCacheLock) {
			if (mDiskLruCache != null) {
				mDiskLruCache.setMaxSize(maxSize);
			}
		}
	}

	public void setDiskCacheFileNameGenerator(
			FileNameGenerator fileNameGenerator) {
		synchronized (mDiskCacheLock) {
			if (mDiskLruCache != null && fileNameGenerator != null) {
				mDiskLruCache.setFileNameGenerator(fileNameGenerator);
			}
		}
	}

	public Bitmap downloadBitmap(String uri, BitmapDisplayConfig config,
			final WBitmap.BitmapLoadTask<?> task) {

		BitmapMeta bitmapMeta = new BitmapMeta();

		OutputStream outputStream = null;
		LruDiskCache.Snapshot snapshot = null;

		try {
			Bitmap bitmap = null;

			if (globalConfig.isDiskCacheEnabled()) {
				if (mDiskLruCache == null) {
					initDiskCache();
				}

				if (mDiskLruCache != null) {
					try {
						snapshot = mDiskLruCache.get(uri);
						if (snapshot == null) {
							LruDiskCache.Editor editor = mDiskLruCache
									.edit(uri);
							if (editor != null) {
								outputStream = editor
										.newOutputStream(DISK_CACHE_INDEX);
								bitmapMeta.expiryTimestamp = globalConfig
										.getDownloader().downloadToStream(uri,
												outputStream, task);
								if (bitmapMeta.expiryTimestamp < 0) {
									editor.abort();
									return null;
								} else {
									editor.setEntryExpiryTimestamp(bitmapMeta.expiryTimestamp);
									editor.commit();
								}
								snapshot = mDiskLruCache.get(uri);
							}
						}
						if (snapshot != null) {
							bitmapMeta.inputStream = snapshot
									.getInputStream(DISK_CACHE_INDEX);
							bitmap = decodeBitmapMeta(bitmapMeta, config);
							if (bitmap == null) {
								bitmapMeta.inputStream = null;
								mDiskLruCache.remove(uri);
							}
						}
					} catch (Throwable e) {
						LogUtils.e(e.getMessage(), e);
					}
				}
			}

			if (bitmap == null) {
				outputStream = new ByteArrayOutputStream();
				bitmapMeta.expiryTimestamp = globalConfig.getDownloader()
						.downloadToStream(uri, outputStream, task);
				if (bitmapMeta.expiryTimestamp < 0) {
					return null;
				} else {
					bitmapMeta.data = ((ByteArrayOutputStream) outputStream)
							.toByteArray();
					bitmap = decodeBitmapMeta(bitmapMeta, config);
				}
			}

			if (bitmap != null) {
				bitmap = rotateBitmapIfNeeded(uri, config, bitmap);
				bitmap = addBitmapToMemoryCache(uri, config, bitmap,
						bitmapMeta.expiryTimestamp);
			}
			return bitmap;
		} catch (Throwable e) {
			LogUtils.e(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(outputStream);
			IOUtils.closeQuietly(snapshot);
		}

		return null;
	}

	private Bitmap addBitmapToMemoryCache(String uri,
			BitmapDisplayConfig config, Bitmap bitmap, long expiryTimestamp)
			throws IOException {
		if (config != null) {
			BitmapFactory bitmapFactory = config.getBitmapFactory();
			if (bitmapFactory != null) {
				bitmap = bitmapFactory.cloneNew().createBitmap(bitmap);
			}
		}
		if (uri != null && bitmap != null
				&& globalConfig.isMemoryCacheEnabled() && mMemoryCache != null) {
			MemoryCacheKey key = new MemoryCacheKey(uri, config);
			mMemoryCache.put(key, bitmap, expiryTimestamp);
		}
		return bitmap;
	}

	public Bitmap getBitmapFromMemCache(String uri, BitmapDisplayConfig config) {
		if (mMemoryCache != null && globalConfig.isMemoryCacheEnabled()) {
			MemoryCacheKey key = new MemoryCacheKey(uri, config);
			return mMemoryCache.get(key);
		}
		return null;
	}

	public File getBitmapFileFromDiskCache(String uri) {
		synchronized (mDiskCacheLock) {
			if (mDiskLruCache != null) {
				return mDiskLruCache.getCacheFile(uri, DISK_CACHE_INDEX);
			} else {
				return null;
			}
		}
	}

	public Bitmap getBitmapFromDiskCache(String uri, BitmapDisplayConfig config) {
		if (uri == null || !globalConfig.isDiskCacheEnabled())
			return null;
		if (mDiskLruCache == null) {
			initDiskCache();
		}
		if (mDiskLruCache != null) {
			LruDiskCache.Snapshot snapshot = null;
			try {
				snapshot = mDiskLruCache.get(uri);
				if (snapshot != null) {
					Bitmap bitmap = null;
					if (config == null || config.isShowOriginal()) {
						bitmap = BitmapDecoder.decodeFileDescriptor(snapshot
								.getInputStream(DISK_CACHE_INDEX).getFD());
					} else {
						bitmap = BitmapDecoder
								.decodeSampledBitmapFromDescriptor(snapshot
										.getInputStream(DISK_CACHE_INDEX)
										.getFD(), config.getBitmapMaxSize(),
										config.getBitmapConfig());
					}

					bitmap = rotateBitmapIfNeeded(uri, config, bitmap);
					bitmap = addBitmapToMemoryCache(uri, config, bitmap,
							mDiskLruCache.getExpiryTimestamp(uri));
					return bitmap;
				}
			} catch (Throwable e) {
				LogUtils.e(e.getMessage(), e);
			} finally {
				IOUtils.closeQuietly(snapshot);
			}
		}
		return null;
	}

	public void clearCache() {
		clearMemoryCache();
		clearDiskCache();
	}

	public void clearMemoryCache() {
		if (mMemoryCache != null) {
			mMemoryCache.evictAll();
		}
	}

	public void clearDiskCache() {
		synchronized (mDiskCacheLock) {
			if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
				try {
					mDiskLruCache.delete();
					mDiskLruCache.close();
				} catch (Throwable e) {
					LogUtils.e(e.getMessage(), e);
				}
				mDiskLruCache = null;
			}
		}
		initDiskCache();
	}

	public void clearCache(String uri) {
		clearMemoryCache(uri);
		clearDiskCache(uri);
	}

	public void clearMemoryCache(String uri) {
		MemoryCacheKey key = new MemoryCacheKey(uri, null);
		if (mMemoryCache != null) {
			while (mMemoryCache.containsKey(key)) {
				mMemoryCache.remove(key);
			}
		}
	}

	public void clearDiskCache(String uri) {
		synchronized (mDiskCacheLock) {
			if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
				try {
					mDiskLruCache.remove(uri);
				} catch (Throwable e) {
					LogUtils.e(e.getMessage(), e);
				}
			}
		}
	}

	public void flush() {
		synchronized (mDiskCacheLock) {
			if (mDiskLruCache != null) {
				try {
					mDiskLruCache.flush();
				} catch (Throwable e) {
					LogUtils.e(e.getMessage(), e);
				}
			}
		}
	}

	public void close() {
		synchronized (mDiskCacheLock) {
			if (mDiskLruCache != null) {
				try {
					if (!mDiskLruCache.isClosed()) {
						mDiskLruCache.close();
					}
				} catch (Throwable e) {
					LogUtils.e(e.getMessage(), e);
				}
				mDiskLruCache = null;
			}
		}
	}

	private class BitmapMeta {
		public FileInputStream inputStream;
		public byte[] data;
		public long expiryTimestamp;
	}

	private Bitmap decodeBitmapMeta(BitmapMeta bitmapMeta,
			BitmapDisplayConfig config) throws IOException {
		if (bitmapMeta == null)
			return null;
		Bitmap bitmap = null;
		if (bitmapMeta.inputStream != null) {
			if (config == null || config.isShowOriginal()) {
				bitmap = BitmapDecoder
						.decodeFileDescriptor(bitmapMeta.inputStream.getFD());
			} else {
				bitmap = BitmapDecoder.decodeSampledBitmapFromDescriptor(
						bitmapMeta.inputStream.getFD(),
						config.getBitmapMaxSize(), config.getBitmapConfig());
			}
		} else if (bitmapMeta.data != null) {
			if (config == null || config.isShowOriginal()) {
				bitmap = BitmapDecoder.decodeByteArray(bitmapMeta.data);
			} else {
				bitmap = BitmapDecoder.decodeSampledBitmapFromByteArray(
						bitmapMeta.data, config.getBitmapMaxSize(),
						config.getBitmapConfig());
			}
		}
		return bitmap;
	}

	private synchronized Bitmap rotateBitmapIfNeeded(String uri,
			BitmapDisplayConfig config, Bitmap bitmap) {
		Bitmap result = bitmap;
		if (config != null && config.isAutoRotation()) {
			File bitmapFile = this.getBitmapFileFromDiskCache(uri);
			if (bitmapFile != null && bitmapFile.exists()) {
				ExifInterface exif = null;
				try {
					exif = new ExifInterface(bitmapFile.getPath());
				} catch (Throwable e) {
					return result;
				}
				int orientation = exif.getAttributeInt(
						ExifInterface.TAG_ORIENTATION,
						ExifInterface.ORIENTATION_UNDEFINED);
				int angle = 0;
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					angle = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					angle = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					angle = 270;
					break;
				default:
					angle = 0;
					break;
				}
				if (angle != 0) {
					Matrix m = new Matrix();
					m.postRotate(angle);
					result = Bitmap.createBitmap(bitmap, 0, 0,
							bitmap.getWidth(), bitmap.getHeight(), m, true);
					bitmap.recycle();
					bitmap = null;
				}
			}
		}
		return result;
	}

	public class MemoryCacheKey {
		private String uri;
		private String subKey;

		private MemoryCacheKey(String uri, BitmapDisplayConfig config) {
			this.uri = uri;
			this.subKey = config == null ? null : config.toString();
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof MemoryCacheKey))
				return false;

			MemoryCacheKey that = (MemoryCacheKey) o;

			if (!uri.equals(that.uri))
				return false;

			if (subKey != null && that.subKey != null) {
				return subKey.equals(that.subKey);
			}

			return true;
		}

		@Override
		public int hashCode() {
			return uri.hashCode();
		}
	}
}