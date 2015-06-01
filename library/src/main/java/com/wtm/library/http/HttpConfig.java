package com.wtm.library.http;

public class HttpConfig {
	private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8 * 1024; // 8KB
	private static final int SOCKET_TIMEOUT = 8 * 1000;
	private static final int READ_TIMEOUT = 8 * 1000;
	private static final String CHAR_SET = "UTF8";
	private static final String REQUEST_METHOD = "GET";
	private static final boolean DO_OUT_PUT = true;
	private static final String TYPE = "application/octet-stream";

	private int socketBuffer;
	private int connectTimeOut;
	private int readTimeout;
	private String charSet;
	private String requestMethod;
	private boolean doOutput;
	private boolean doInput;
	private boolean followRedirects;
	private boolean useCache;
	private String contentType;

	public HttpConfig() {
		socketBuffer = DEFAULT_SOCKET_BUFFER_SIZE;
		connectTimeOut = SOCKET_TIMEOUT;
		readTimeout = READ_TIMEOUT;
		charSet = CHAR_SET;
		doOutput = DO_OUT_PUT;
		requestMethod = REQUEST_METHOD;
		useCache = false;
		followRedirects = true;
		contentType = TYPE;
	}

	public boolean isUseCache() {
		return useCache;
	}

	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public int getSocketBuffer() {
		return socketBuffer;
	}

	public void setSocketBuffer(int socketBuffer) {
		this.socketBuffer = socketBuffer;
	}

	public int getConnectTimeOut() {
		return connectTimeOut;
	}

	public void setConnectTimeOut(int connectTimeOut) {
		this.connectTimeOut = connectTimeOut;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public String getCharSet() {
		return charSet;
	}

	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public boolean isDoOutput() {
		return doOutput;
	}

	public void setDoOutput(boolean doOutput) {
		this.doOutput = doOutput;
	}

	public boolean isDoInput() {
		return doInput;
	}

	public void setDoInput(boolean doInput) {
		this.doInput = doInput;
	}

	public boolean isFollowRedirects() {
		return followRedirects;
	}

	public void setFollowRedirects(boolean followRedirects) {
		this.followRedirects = followRedirects;
	}
}
