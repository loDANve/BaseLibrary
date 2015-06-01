package com.wtm.library.bitmap;


import com.wtm.library.base.WActivityManager;

public class WBitmapSingleBuilder {

	private static WBitmap instance;

	public static WBitmap getWBitmap() {
		if (instance == null) {
			instance = new WBitmap(WActivityManager.getInstance().topActivity());
		}
		return instance;
	}
}
