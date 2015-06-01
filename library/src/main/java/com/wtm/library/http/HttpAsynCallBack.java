package com.wtm.library.http;

import android.os.Handler;
import android.os.Message;

public class HttpAsynCallBack extends Handler implements HttpResponseCallBack {

	protected static final int SUCCESS_MESSAGE = 3;
	protected static final int ONLOAD_MESSAGE = 2;
	protected static final int ERROR_MESSAGE = 1;
	protected static final int ONSTART_MESSAGE = 4;

	private HttpResponseCallBack httpResponseCallBack;

	private long count, current;
	private float currentDownLoadScale;
	private Exception e;
	private int responseCode;

	public HttpAsynCallBack(HttpResponseCallBack httpResponseCallBack) {
		this.httpResponseCallBack = httpResponseCallBack;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch (msg.what) {
		case SUCCESS_MESSAGE:
			httpResponseCallBack.complete((byte[]) msg.obj);
			break;
		case ONLOAD_MESSAGE:
			httpResponseCallBack.onLoad(count, current, currentDownLoadScale);
			break;
		case ERROR_MESSAGE:
			httpResponseCallBack.error(e, msg, responseCode);
			break;
		case ONSTART_MESSAGE:
			httpResponseCallBack.onStart();
			break;
		default:
			break;
		}
	}

	@Override
	public void complete(byte[] data) {
		Message message = Message.obtain();
		message.obj = data;
		message.what = SUCCESS_MESSAGE;
		this.sendMessage(message);
	}

	@Override
	public void onLoad(long count, long current, float currentDownLoadScale) {
		Message message = Message.obtain();
		message.what = ONLOAD_MESSAGE;
		this.count = count;
		this.current = current;
		this.currentDownLoadScale = currentDownLoadScale;
		this.sendMessage(message);
	}

	@Override
	public void error(Exception e, Object msg, int responseCode) {
		this.e = e;
		this.responseCode = responseCode;
		Message message = Message.obtain();
		message.obj = msg;
		message.what = ERROR_MESSAGE;
		this.sendMessage(message);
	}

	@Override
	public void onStart() {
		Message message = Message.obtain();
		message.what = ONSTART_MESSAGE;
		this.sendMessage(message);
	}

}
