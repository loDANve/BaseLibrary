package com.wtm.library.bitmap.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * Bitmap�������֣�����ͼƬ��Сѹ��������
 */
public class BitmapHelper {

	/**
	 * ͼƬѹ�����?ʹ��Options�ķ�����
	 * 
	 * <br>
	 * <b>˵��</b> ʹ�÷�����
	 * ������Ҫ��Options��inJustDecodeBounds��������Ϊtrue��BitmapFactory.decodeһ��ͼƬ ��
	 * Ȼ��Options��ͬ����Ŀ�Ⱥ͸߶�һ�𴫵ݵ����������С�
	 * ֮����ʹ�ñ������ķ���ֵ���������BitmapFactory.decode����ͼƬ��
	 * 
	 * <br>
	 * <b>˵��</b> BitmapFactory����bitmap�᳢��Ϊ�Ѿ�������bitmap�����ڴ�
	 * ����ʱ�ͻ�����׵���OOM���֡�Ϊ��ÿһ�ִ����������ṩ��һ����ѡ��Options����
	 * ������������inJustDecodeBounds��������Ϊtrue�Ϳ����ý���������ֹΪbitmap�����ڴ�
	 * ������ֵҲ������һ��Bitmap���� ����null����ȻBitmap��null�ˣ�����Options��outWidth��
	 * outHeight��outMimeType���Զ��ᱻ��ֵ��
	 * 
	 * @param reqWidth
	 *            Ŀ����,����Ŀ��ֻ�Ƿ�ֵ��ʵ����ʾ��ͼƬ��С�ڵ������ֵ
	 * @param reqHeight
	 *            Ŀ��߶�,����Ŀ��ֻ�Ƿ�ֵ��ʵ����ʾ��ͼƬ��С�ڵ������ֵ
	 */
	public static BitmapFactory.Options calculateInSampleSize(
			final BitmapFactory.Options options, final int reqWidth,
			final int reqHeight) {
		// ԴͼƬ�ĸ߶ȺͿ��
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > 400 || width > 450) {
			if (height > reqHeight || width > reqWidth) {
				// �����ʵ�ʿ�ߺ�Ŀ���ߵı���
				final int heightRatio = Math.round((float) height
						/ (float) reqHeight);
				final int widthRatio = Math.round((float) width
						/ (float) reqWidth);
				// ѡ���͸�����С�ı�����ΪinSampleSize��ֵ��������Ա�֤����ͼƬ�Ŀ�͸�
				// һ��������ڵ���Ŀ��Ŀ�͸ߡ�
				inSampleSize = heightRatio < widthRatio ? heightRatio
						: widthRatio;
			}
		}
		// ����ѹ������
		options.inSampleSize = inSampleSize;
		options.inJustDecodeBounds = false;
		return options;
	}

	/**
	 * ͼƬѹ����������ʹ��compress�ķ�����
	 * 
	 * <br>
	 * <b>˵��</b> ���bitmap����Ĵ�СС��maxSize����������
	 * 
	 * @param bitmap
	 *            Ҫѹ����ͼƬ
	 * @param maxSize
	 *            ѹ����Ĵ�С����λkb
	 */
	public static void imageZoom(Bitmap bitmap, double maxSize) {
		// ��bitmap���������У����ڻ��bitmap�Ĵ�С����ʵ�ʶ�ȡ��ԭ�ļ�Ҫ��
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// ��ʽ�������������
		bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
		byte[] b = baos.toByteArray();
		// ���ֽڻ���KB
		double mid = b.length / 1024;
		// ��ȡbitmap��С ����������С�Ķ��ٱ�
		double i = mid / maxSize;
		// �ж�bitmapռ�ÿռ��Ƿ�����������ռ� ��������ѹ�� С����ѹ��
		if (i > 1) {
			// ����ͼƬ �˴��õ�ƽ���� �����͸߶�ѹ������Ӧ��ƽ����
			// �����ֿ�߲��䣬���ź�Ҳ�ﵽ�����ռ�ÿռ�Ĵ�С��
			bitmap = scale(bitmap, bitmap.getWidth() / Math.sqrt(i),
					bitmap.getHeight() / Math.sqrt(i));
		}
	}

	/***
	 * ͼƬ�����ŷ���
	 * 
	 * @param src
	 *            ��ԴͼƬ��Դ
	 * @param newWidth
	 *            �����ź���
	 * @param newHeight
	 *            �����ź�߶�
	 */
	public static Bitmap scale(Bitmap src, double newWidth, double newHeight) {
		// ��¼src�Ŀ��
		float width = src.getWidth();
		float height = src.getHeight();
		// ����һ��matrix����
		Matrix matrix = new Matrix();
		// �������ű���
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// ��ʼ����
		matrix.postScale(scaleWidth, scaleHeight);
		// �������ź��ͼƬ
		return Bitmap.createBitmap(src, 0, 0, (int) width, (int) height,
				matrix, true);
	}

	/**
	 * ͼƬ�����ŷ���
	 * 
	 * @param src
	 *            ��ԴͼƬ��Դ
	 * @param scaleMatrix
	 *            �����Ź���
	 */
	public static Bitmap scale(Bitmap src, Matrix scaleMatrix) {
		return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(),
				scaleMatrix, true);
	}

	/**
	 * ͼƬ�����ŷ���
	 * 
	 * @param src
	 *            ��ԴͼƬ��Դ
	 * @param scaleX
	 *            ���������ű���
	 * @param scaleY
	 *            ���������ű���
	 */
	public static Bitmap scale(Bitmap src, float scaleX, float scaleY) {
		Matrix matrix = new Matrix();
		matrix.postScale(scaleX, scaleY);
		return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(),
				matrix, true);
	}

	/**
	 * ͼƬ�����ŷ���
	 * 
	 * @param src
	 *            ��ԴͼƬ��Դ
	 * @param scale
	 *            �����ű���
	 */
	public static Bitmap scale(Bitmap src, float scale) {
		return scale(src, scale, scale);
	}

	/**
	 * ��תͼƬ
	 * 
	 * @param angle
	 *            ��ת�Ƕ�
	 * @param bitmap
	 *            Ҫ��ת��ͼƬ
	 * @return ��ת���ͼƬ
	 */
	public static Bitmap rotate(int angle, Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
	}
}
