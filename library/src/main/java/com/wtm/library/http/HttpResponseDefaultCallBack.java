package com.wtm.library.http;

public abstract class HttpResponseDefaultCallBack implements
		HttpResponseCallBack {

	@Override
	public void complete(byte[] data) {
	}

	@Override
	public void onLoad(long count, long current, float currentDownLoadScale) {
	}

	@Override
	public void error(Exception e, Object msg, int responseCode) {
		
	}
	
}
