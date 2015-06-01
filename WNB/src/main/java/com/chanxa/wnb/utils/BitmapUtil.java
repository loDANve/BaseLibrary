package com.chanxa.wnb.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.wtm.library.utils.DeviceUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ExifInterface;

public class BitmapUtil {

	private BitmapUtil() {
		return;
	}

	public static Bitmap getBitmap(Context context, Bitmap srcBitmap,
			float min, float max) {
		Options options = new Options();
		options.inJustDecodeBounds = true;
		float imagewidth = srcBitmap.getWidth();
		float imagehight = srcBitmap.getHeight();
		float newwidth = 0;
		float newheight = 0;
		float biger = 0;
		float smaller = 0;
		float demax = 0;
		float demin = 0;
		int statecode = 0;
		float dem = 0;
		System.err.println("最大为:" + max + "最小为" + min + "边长为:");
		biger = imagewidth >= imagehight ? imagewidth : imagehight;
		smaller = imagewidth <= imagehight ? imagewidth : imagehight;
		demax = DeviceUtils.dip2px(context, max) / biger;
		demin = DeviceUtils.dip2px(context, min) / smaller;
		System.err.println("最小比:" + demin + "最大比" + demax);
		newwidth = imagewidth * demax;
		newheight = imagehight * demax;
		System.err.println("宽度:" + imagewidth + "高度" + imagehight);
		System.err.println("新的宽度:" + newwidth + "新的高度" + newheight);
		if (newheight < DeviceUtils.dip2px(context, min)
				|| newwidth < DeviceUtils.dip2px(context, min)) {
			newheight = imagehight * demin;
			newwidth = imagewidth * demin;
			statecode = 1;
		}
		System.err.println("处理过一次后的新的宽度:" + newwidth + "新的高度" + newheight);
		Matrix matrix = new Matrix();
		dem = statecode == 1 ? demin : demax;
		System.err.println("压缩比是" + dem);
		matrix.postScale(dem, dem);
		Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, (int) imagewidth,
				(int) imagehight, matrix, true);
		System.err.println("处理后第二次的图片宽为:" + bitmap.getWidth() + "高为:"
				+ bitmap.getHeight());
		if (statecode == 1) {
			System.err.println("特殊处理");
			int cuth = (int) (newheight > DeviceUtils.dip2px(context, max) ? (newheight - DeviceUtils
					.dip2px(context, max)) / 2 : 0);
			int cutw = (int) (newwidth > DeviceUtils.dip2px(context, max) ? (newwidth - DeviceUtils
					.dip2px(context, max)) / 2 : 0);
			newheight = newheight < DeviceUtils.dip2px(context, max) ? newheight
					: DeviceUtils.dip2px(context, max);
			newwidth = newwidth < DeviceUtils.dip2px(context, max) ? newwidth
					: DeviceUtils.dip2px(context, max);
			System.err.println("处理后宽为" + newwidth + "高为:" + newheight);

			bitmap = Bitmap.createBitmap(bitmap, cutw, cuth, (int) newwidth,
					(int) newheight);
		}
		System.err.println("最终处理后的图片宽为:" + bitmap.getWidth() + "高为:"
				+ bitmap.getHeight());
		return bitmap;
	}

	public static Bitmap getBitmap2(Context context, String filePath, float reft) {
		// BitmapFactory.Options opts = new BitmapFactory.Options();
		// opts.inTempStorage = new byte[100 * 1024];
		// opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// opts.inPurgeable = true;
		// opts.inSampleSize = 4;
		// opts.inInputShareable = true;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath);
		return getBitmap2(context, bitmap, reft);
	}

	public static Bitmap getBitmap2(Context context, Bitmap srcBitmap,
			float reft) {
		float smaller = 0;
		float demin = 0;
		float newwidth = 0;
		float newheight = 0;
		float imagewidth = srcBitmap.getWidth();
		float imagehight = srcBitmap.getHeight();
		smaller = imagewidth <= imagehight ? imagewidth : imagehight;
		demin = DeviceUtils.dip2px(context, reft) / smaller;
		System.err.println("压缩比例:" + demin);
		newwidth = imagewidth * demin;
		newheight = imagehight * demin;
		System.err.println("新的宽度:" + newwidth + "新的高度" + newheight);
		Matrix matrix = new Matrix();
		matrix.postScale(demin, demin);
		Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, (int) imagewidth,
				(int) imagehight, matrix, true);
		System.err.println("第一次处理后的图片宽为:" + bitmap.getWidth() + "高为:"
				+ bitmap.getHeight());
		int cuth = (int) (newheight > DeviceUtils.dip2px(context, reft) ? (newheight - DeviceUtils
				.dip2px(context, reft)) / 2 : 0);
		int cutw = (int) (newwidth > DeviceUtils.dip2px(context, reft) ? (newwidth - DeviceUtils
				.dip2px(context, reft)) / 2 : 0);
		newheight = newheight < DeviceUtils.dip2px(context, reft) ? newheight
				: DeviceUtils.dip2px(context, reft);
		newwidth = newwidth < DeviceUtils.dip2px(context, reft) ? newwidth
				: DeviceUtils.dip2px(context, reft);
		bitmap = Bitmap.createBitmap(bitmap, cutw, cuth, (int) newwidth,
				(int) newheight);
		System.err.println("最终处理后的图片宽为:" + bitmap.getWidth() + "高为:"
				+ bitmap.getHeight());
		return bitmap;
	}

	public static Bitmap getBitmap(int width, int heigh, int textSize,
			int bgColor, int fontColor, String text, float startX,
			float startY, int typeface, float rotate) {
		Bitmap bitmap = Bitmap.createBitmap(width, heigh, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(bgColor);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.DEV_KERN_TEXT_FLAG);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.defaultFromStyle(typeface));
		paint.setColor(fontColor);
		canvas.drawText(text, startX, startY, paint);
		canvas.save(canvas.ALL_SAVE_FLAG);
		canvas.restore();
		Matrix m = new Matrix();
		m.setRotate(rotate);
		Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), m, true);
		return b2;
	}

	public static Bitmap getBitmap(int width, int heigh, int bgColor,
			int strokeColor) {
		Bitmap bitmap = Bitmap.createBitmap(width, heigh, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(bgColor);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.DEV_KERN_TEXT_FLAG);
		paint.setColor(strokeColor);
		canvas.save(canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return bitmap;
	}

	public static boolean compressImage(Context context,String srcPath, String outFile) {
		/** 
         * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转 
         */ 
        int degree = readPictureDegree(srcPath);  
		final int DEF_MIN = 480;
		final int DEF_MAX = 1280;
		Options newOpts = new Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Bitmap bitmap;
		BitmapFactory.decodeFile(srcPath, newOpts);
		int outWidth = 0;
		int outHeight = 0;
		int picMin = Math.min(newOpts.outWidth, newOpts.outHeight);
		int picMax = Math.max(newOpts.outWidth, newOpts.outHeight);
		float picShap = 1.0f * picMin / picMax;
		float defShap = 1.0f * DEF_MIN / DEF_MAX;
		float scale = 1f;
		if (picMax < DEF_MAX) {

		} else if (picShap > defShap) {// 比较 方正的，等比压缩至最大值
			scale = 1.0f * DEF_MAX / picMax;

		} else if (picMin > DEF_MIN) {// 较窄边压缩至最小值
			scale = 1.0f * DEF_MIN / picMin;
		}
		int scaledWidth = (int) (scale * newOpts.outWidth);
		int scaledHeight = (int) (scale * newOpts.outHeight);
		newOpts.inSampleSize = (int) ((scale == 1 ? 1 : 2) / scale);
		newOpts.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		
		if (scale != 1) {
			Matrix matrix = new Matrix();
			scale = (float) scaledWidth / bitmap.getWidth();
			matrix.setScale(scale, scale);
			Bitmap bitmapNew = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, false);
			bitmap.recycle();
			bitmap = bitmapNew;
		}
		int quality;
		if (scaledHeight * scaledWidth > (1280 * 720)) {
			quality = 30;
		} else {
			quality = 80 - (int) ((float) scaledHeight * scaledWidth
					/ (1280 * 720) * 50);
		}
		try {
			System.err.println("file byte is:" + new File(srcPath).length());
	        //
			/** 
	         * 把图片旋转为正的方向 
	         */ 
			bitmap = rotaingImageView(degree, bitmap); 
			//
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality,
					new FileOutputStream(new File(outFile)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
            return false;
        }
		bitmap.recycle();
		bitmap = null;
		System.gc();
        return true;
	}

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/*
	 * 旋转图片
	 * 
	 * @param angle
	 * 
	 * @param bitmap
	 * 
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		;
		matrix.postRotate(angle);
		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}
}
