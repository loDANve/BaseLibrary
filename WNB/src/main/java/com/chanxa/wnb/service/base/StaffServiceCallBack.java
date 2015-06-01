package com.chanxa.wnb.service.base;

public interface StaffServiceCallBack<T>{
	void onStart();
	void onError(Exception e, int stateCode);
	void onEXECisFalse(String errroMsg);
	void onEXECSuccess(T resultObj,String proof);
	void onEXECSuccess(T resultObj,String proof, int pageSize, int dataSize);
}
