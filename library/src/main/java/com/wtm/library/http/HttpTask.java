package com.wtm.library.http;

import com.wtm.library.utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpTask {

	private ThreadPoolManager threadPoolManager;
	private HttpConfig httpConfig;

	public HttpTask(HttpConfig httpConfig) {
		threadPoolManager = ThreadPoolManager.getInstance();
		this.httpConfig = httpConfig;
	}

	public HttpTask() {
		this(new HttpConfig());
	}

	public void urlGet(String url, HttpResponseCallBack callBack) {
		threadPoolManager.addTask(getGetTask(url, callBack));
	}

	/**
	 * ִ���в���get����
	 * 
	 * @param url
	 * @param params
	 * @param callBack
	 */
	public void urlGet(String url, HttpParams params,
			HttpResponseCallBack callBack) {
		if (params != null) {
			url += "?" + params.toString();
		}
		threadPoolManager.addTask(getGetTask(url, callBack));
	}

	/**
	 * ִ��post����
	 * 
	 * @param url
	 * @param params
	 * @param callBack
	 */
	public void urlPost(String url, HttpParams params,
			HttpResponseCallBack callBack) {
		threadPoolManager.addTask(getPostTask(url, params, callBack));
	}

	public void urlPost(String url, String data, HttpResponseCallBack callBack) {
		threadPoolManager.addTask(getPostTask(url, data, callBack));
	}

	/**
	 * 
	 * @return ��ȡһ��get��ʽ����������
	 */
	public Runnable getGetTask(final String _url,
			final HttpResponseCallBack callBack) {
		return new Runnable() {
			@Override
			public void run() {
				callBack.onStart();
				int responceCode = 404;
				URL url;
				InputStream inputStream = null;
				ByteArrayOutputStream byteArrayOutputStream = null;
				try {
					url = new URL(_url);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setUseCaches(httpConfig.isUseCache());
					conn.setReadTimeout(httpConfig.getReadTimeout());
					conn.setConnectTimeout(httpConfig.getConnectTimeOut());
					conn.setRequestProperty("Charset", httpConfig.getCharSet());
					conn.setRequestMethod("GET");
					conn.setInstanceFollowRedirects(httpConfig
							.isFollowRedirects());
					inputStream = conn.getInputStream();
					byteArrayOutputStream = new ByteArrayOutputStream();
					responceCode = conn.getResponseCode();
					byte[] buff = new byte[1024];
					int len = 0;
					int count = conn.getContentLength();
					int length = 0;
					while ((len = inputStream.read(buff)) != -1) {
						length += len;
						byteArrayOutputStream.write(buff, 0, len);
						callBack.onLoad(count, length,
								((length / (float) count) * 100));
					}
					callBack.complete(byteArrayOutputStream.toByteArray());
				} catch (MalformedURLException e) {
					e.printStackTrace();
					callBack.error(e, "URL����", responceCode);
				} catch (IOException e) {
					e.printStackTrace();
					callBack.error(e, "IO�쳣", responceCode);
				} finally {
					FileUtils.closeIO(inputStream, byteArrayOutputStream);
				}

			}
		};
	}

	/**
	 * @return ��ȡһ��post��ʽ����������
	 */
	public Runnable getPostTask(final String _url, final HttpParams params,
			final HttpResponseCallBack callBack) {
		return getPostTask(_url, params.toString(), callBack);
	}

	public Runnable getPostTask(final String _url, final String data,
			final HttpResponseCallBack callBack) {
		return new Runnable() {
			@Override
			public void run() {
				callBack.onStart();
				InputStream inputStream = null;
				OutputStream outputStream = null;
				ByteArrayOutputStream byteArrayOutputStream = null;
				int responceCode = 404;
				try {
					URL url = new URL(_url);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					conn.setDoInput(true);
					conn.setUseCaches(httpConfig.isUseCache());
					conn.setReadTimeout(httpConfig.getReadTimeout());
					conn.setConnectTimeout(httpConfig.getConnectTimeOut());
					conn.setRequestProperty("Charset", httpConfig.getCharSet());
					conn.setInstanceFollowRedirects(httpConfig
							.isFollowRedirects());
					outputStream = conn.getOutputStream();
					outputStream.write(data.getBytes());
					inputStream = conn.getInputStream();
					byteArrayOutputStream = new ByteArrayOutputStream();
					responceCode = conn.getResponseCode();
					byte[] buff = new byte[1024];
					int len = 0;
					int count = conn.getContentLength();
					int length = 0;
					while ((len = inputStream.read(buff)) != -1) {
						length += len;
						byteArrayOutputStream.write(buff, 0, len);
						callBack.onLoad(count, length,
								((length / (float) count) * 100));
					}
					callBack.complete(byteArrayOutputStream.toByteArray());
				} catch (MalformedURLException e) {
					e.printStackTrace();
					callBack.error(e, "URL����", responceCode);
				} catch (IOException e) {
					e.printStackTrace();
					callBack.error(e, "IO�쳣", responceCode);
				} finally {
					FileUtils.closeIO(inputStream, outputStream,
							byteArrayOutputStream);
				}
			}
		};
	}

}
