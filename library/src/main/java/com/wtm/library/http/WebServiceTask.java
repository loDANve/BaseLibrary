package com.wtm.library.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class WebServiceTask {

	private ThreadPoolManager threadPoolManager;

	private String nameSpace;
	private String host;
	private WebServiceCallBack webServiceCallBack;
	private int timeout = 10000;
	private int webServiceVersion = 12;

	public WebServiceTask(String nameSpace, String host) {
		threadPoolManager = ThreadPoolManager.getInstance();
		this.nameSpace = nameSpace;
		this.host = host;
	}

	public void callWebService(final String opName,
			final HashMap<String, Object> paras,
			final WebServiceCallBack webServiceCallBack) {

		Runnable task = new Runnable() {
			@Override
			public void run() {
				SoapObject requestObject = new SoapObject(nameSpace, opName);
				Iterator iter = paras.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String key = (String) entry.getKey();
					Object val = entry.getValue();
					if (key != null && val != null) {
						requestObject.addProperty(key, val);
					}
				}
				int version = SoapEnvelope.VER11;
				switch (webServiceVersion) {
				case 10:
					version = SoapEnvelope.VER10;
					break;
				case 11:
					version = SoapEnvelope.VER11;
					break;
				case 12:
					version = SoapEnvelope.VER12;
				}
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						version);
				envelope.dotNet = true;
				envelope.bodyOut = requestObject;
				new MarshalFloat().register(envelope);
				HttpTransportSE transportSE = new HttpTransportSE(host, timeout);
				transportSE.debug = true;
				try {
					transportSE.call(nameSpace + "/" + opName, envelope);
				} catch (Exception e) {
					e.printStackTrace();
					if (webServiceCallBack != null) {
						webServiceCallBack.onError(e);
					}
				}
				try {
					Object data = envelope.getResponse();
					Log.d("WTM","11");
					if (data != null) {
						if (webServiceCallBack != null) {
							webServiceCallBack.onSuccess(data);
						}
					}
				} catch (SoapFault e) {
					e.printStackTrace();
					if (webServiceCallBack != null) {
						webServiceCallBack.onError(e);
					}
				}

			}
		};
		threadPoolManager.addTask(task);
	}

	public void callWebService(String opName, HashMap<String, Object> paras) {
		callWebService(opName, paras, this.webServiceCallBack);
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public WebServiceCallBack getWebServiceCallBack() {
		return webServiceCallBack;
	}

	public void setWebServiceCallBack(WebServiceCallBack webServiceCallBack) {
		this.webServiceCallBack = webServiceCallBack;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getWebServiceVersion() {
		return webServiceVersion;
	}

	public void setWebServiceVersion(int webServiceVersion) {
		this.webServiceVersion = webServiceVersion;
	}
}
