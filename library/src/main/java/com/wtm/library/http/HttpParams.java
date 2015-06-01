package com.wtm.library.http;


import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class HttpParams {
	// �̰߳�ȫ��hashMap
	protected ConcurrentHashMap<String, String> urlParams;

	// protected void initDataDoMain() {
	//
	// }
	// ��ʼ�������
	public HttpParams() {
		urlParams = new ConcurrentHashMap<String, String>();
	}

	public HttpParams(Map<String, String> params) {
		this();
		urlParams.putAll(params);
	}

	public HttpParams(String paramName, String value) {
		this();
		addParams(paramName, value);
	}

	public void addParams(String paramName, String value) {
		urlParams.put(paramName, value);
	}

	public String removeParams(String paramName) {
		return urlParams.remove(paramName);
	}

	public void clearParams() {
		urlParams.clear();
	}

	// ����ʵ��toString����������get��ʽ
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		Iterator<Entry<String, String>> it = urlParams.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			str.append(key).append("=").append(value).append("&");
		}
		return str.substring(0,str.length()-1);
	}
}
