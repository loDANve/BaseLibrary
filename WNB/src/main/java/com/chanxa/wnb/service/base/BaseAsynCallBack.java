package com.chanxa.wnb.service.base;

import android.os.Handler;
import android.os.Message;

public class BaseAsynCallBack extends Handler implements BaseCallBack {

	private final int onStart = 1;
	private final int onError = 2;
	private final int onEXECisFalse = 3;
	private final int onEXECSuccess = 4;
	private final int onComplete = 5;
	private BaseCallBack mBack;

	public BaseAsynCallBack(BaseCallBack mBack) {
		this.mBack = mBack;
	}




    @Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);

		switch (msg.what) {
		case onStart:
			mBack.onStart();
			break;
		case onError:
			mBack.onError((Exception) msg.obj, msg.arg1);
			break;
		case onEXECisFalse:
			mBack.onEXECisFalse((String) msg.obj);
			break;
		case onEXECSuccess:
			mBack.onEXECSuccess((String) msg.obj);
			break;
		case onComplete:
			mBack.onComplete((String) msg.obj);
			break;
		}

	}

	@Override
	public void onStart() {
		this.sendEmptyMessage(onStart);
	}

	@Override
	public void onError(Exception e, int stateCode) {
		Message message = Message.obtain();
		message.what = onError;
		message.obj = e;
		message.arg1 = stateCode;
		this.sendMessage(message);
	}

	@Override
	public void onEXECisFalse(String errroMsg) {
		Message message = Message.obtain();
		message.what = onEXECisFalse;
		message.obj = errroMsg;
		this.sendMessage(message);
	}

	@Override
	public void onEXECSuccess(String result) {
		Message message = Message.obtain();
		message.what = onEXECSuccess;
		message.obj = result;
		this.sendMessage(message);
	}

	@Override
	public void onComplete(String result) {
		Message message = Message.obtain();
		message.what = onComplete;
		message.obj = result;
		this.sendMessage(message);
	}

}
