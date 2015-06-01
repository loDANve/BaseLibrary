package com.wtm.library.utils;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ResourceManager {

	/**
	 * 
	 * ͨ���ļ�·������һ��ͼƬ�ļ���
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap getResByPath(String path) {
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		return bitmap;
	}

	/**
	 * 
	 * ͨ����ԴID����ͼƬ��
	 * 
	 * @param context
	 * @param resID
	 * @return
	 */
	public static Bitmap getBitmapByResId(Context context, int resID) {
		return BitmapFactory.decodeResource(context.getResources(), resID);
	}

	/**
	 * ͨ��res�ļ�����Ʒ���ͼƬ��
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static Bitmap getBitmapByName(Context context, String name) {
		ApplicationInfo appInfo = context.getApplicationInfo();
		int resID = context.getResources().getIdentifier(name, "drawable",
				appInfo.packageName);
		if (resID == 0)
			return null;// δ�ҵ���ԴͼƬ
		return BitmapFactory.decodeResource(context.getResources(), resID);
	}

	/**
	 * ͨ��res�ļ�����Ʒ���ID��
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static int getRidByName(Context context, String name) {
		ApplicationInfo appInfo = context.getApplicationInfo();
		int resID = context.getResources().getIdentifier(name, "drawable",
				appInfo.packageName);
		return resID;
	}

	/**
	 * ͨ��src�ļ����µ�ͼƬ��ŵ�·�������磺��String path = "com/prj/test.png"������ͼƬ��Դ��
	 * 
	 * @param context
	 * @param path
	 * @return
	 */
	public static InputStream getBitmapStream(Context context, String path) {
		InputStream is = context.getClassLoader().getResourceAsStream(path);
		return is;
	}

	/**
	 * ��ȡAssetsĿ¼�����ͼƬ����ݵ����ļ���ơ�
	 * 
	 * @param context
	 * @param name
	 * @return
	 * @throws java.io.IOException
	 */
	public static InputStream getBitmapStreamByName(Context context, String name)
			throws IOException {
		InputStream is = context.getResources().getAssets().open(name);
		return is;
	}

	/**
	 * ��ȡ�ַ�
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static String getString(Context context, String name) {
		ApplicationInfo appInfo = context.getApplicationInfo();
		int resID = context.getResources().getIdentifier(name, "string",
				appInfo.packageName);
		return context.getResources().getString(resID);
	}

}
