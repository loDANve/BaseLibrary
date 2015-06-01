package com.chanxa.wnb.service.base;


public interface BaseCallBack {
	void onStart();
	void onError(Exception e, int stateCode);
	void onEXECisFalse(String errroMsg);
	void onEXECSuccess(String result);
	void onComplete(String result);
}
