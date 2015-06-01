package com.wtm.library.http;


import android.os.Handler;
import android.os.Message;

public class WebServiceAsynCallBack extends Handler implements
		WebServiceCallBack {

	private final int ONERROR = 1;
	private final int ONSUCCESS = 2;

	private WebServiceCallBack webServiceCallBack;

	public WebServiceAsynCallBack(WebServiceCallBack webServiceCallBack) {
		this.webServiceCallBack = webServiceCallBack;
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case ONERROR:
			webServiceCallBack.onError((Exception) msg.obj);
			break;
		case ONSUCCESS:
			webServiceCallBack.onSuccess(msg.obj);
			break;
		}
	}

	@Override
	public void onError(Exception e) {
		Message message = Message.obtain();
		message.what = ONERROR;
		message.obj = e;
		sendMessage(message);
	}

	@Override
	public void onSuccess(Object data) {
		Message message = Message.obtain();
		message.what = ONSUCCESS;
		message.obj = data;
		sendMessage(message);
	}

}
