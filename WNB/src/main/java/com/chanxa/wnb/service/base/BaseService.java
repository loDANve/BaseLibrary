package com.chanxa.wnb.service.base;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.zip.DataFormatException;

import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;

import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.utils.encryption.ThreeDes;
import com.chanxa.wnb.xml.DATAxmlHelper;
import com.wtm.library.base.WActivityManager;
import com.wtm.library.http.ThreadPoolManager;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;
import com.wtm.library.utils.SystemTool;


/**
 * 万能宝接口调用基类,继承此类调用doRequest可发起请求
 *
 * @author CHANXA
 */
public class BaseService {

    protected String params = "";
    protected ThreadPoolManager threadPoolManager;
    protected String apiPath = AppConfig.HOSTAPI;

    public BaseService() {
        threadPoolManager = ThreadPoolManager.getInstance();
    }

    public void actionInit(String action, HashMap<String, String> dataMap)
            throws Exception {
        params = "Action=%1$s&IdentityKey=" + AppConfig.IDENTITYKEY
                + "&Data=%2$s";
        String data = DATAxmlHelper.getDATAxml(dataMap);
        LogUtils.fff(data);
        data = ThreeDes.jiaMi(AppConfig._3DESKEY, data);
        params = String.format(params, action, data);
        params = params.replace("+", "%2B");
        LogUtils.e(params);
    }

    public void doRequest(String action, HashMap<String, String> dataMap,
                          BaseCallBack callBack) {
        try {
            actionInit(action, dataMap);
            threadPoolManager.addTask(getDataRunnable(params, callBack));
        } catch (Exception e) {
            callBack.onError(e, 0);
        }
    }

    protected Runnable getDataRunnable(final String params,
                                       final BaseCallBack callBack) {
        return new Runnable() {
            public void run() {
                callBack.onStart();
                if (!SystemTool.isCheckNet(WActivityManager.getInstance().topActivity())) {
                    callBack.onError(new NetworkErrorException("网络无连接"), 404);
                }
                InputStream inputStream = null;
                OutputStream outputStream = null;
                ByteArrayOutputStream byteArrayOutputStream = null;
                int responceCode = 404;
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(
                            apiPath).openConnection();
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(8000);
                    conn.setUseCaches(false);
                    conn.setRequestProperty("ContentType",
                            "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Charset", "UTF-8");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    outputStream = conn.getOutputStream();
                    outputStream.write(params.toString().getBytes());
                    inputStream = conn.getInputStream();
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    responceCode = conn.getResponseCode();
                    if (200 == responceCode) {
                        byte[] buff = new byte[1024];
                        int len = 0;
                        while ((len = inputStream.read(buff)) != -1) {
                            byteArrayOutputStream.write(buff, 0, len);
                        }
                        String result = new String(
                                byteArrayOutputStream.toByteArray(), "utf-8");
                        result = ThreeDes.jieMi(AppConfig._3DESKEY, result);
                        callBack.onComplete(result);
                        LogUtils.e(result);
                        String exec = DATAxmlHelper.getAllAttribute(result)
                                .get("EXEC");
                        if (StringUtils.isEmpty(exec)) {
                            callBack.onError(new DataFormatException("平台调用失败"),
                                    responceCode);
                            return;
                        }
                        if (exec.toLowerCase().equals("false")) {
                            String msg = DATAxmlHelper.getAllAttribute(result)
                                    .get("错误消息");
                            callBack.onEXECisFalse(msg);
                        } else {
                            callBack.onEXECSuccess(result);
                        }
                    } else {
                        callBack.onError(new NetworkErrorException(),
                                responceCode);
                    }
                } catch (Exception e) {
                    callBack.onError(e, responceCode);
                }
            }
        };
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }


}
