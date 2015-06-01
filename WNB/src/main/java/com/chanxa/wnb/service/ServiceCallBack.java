package com.chanxa.wnb.service;

public interface ServiceCallBack<T> {
	void onStart();
	void onError(Exception e, int stateCode);
	void onEXECisFalse(String errroMsg);
	void onEXECSuccess(T resultObj);
	void onEXECSuccess(T resultObj,int pageSize,int dataSize);
}
