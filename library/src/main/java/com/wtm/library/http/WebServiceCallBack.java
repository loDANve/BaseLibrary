package com.wtm.library.http;


public interface WebServiceCallBack {

	void onError(Exception e);

	void onSuccess(Object data);
	
}
