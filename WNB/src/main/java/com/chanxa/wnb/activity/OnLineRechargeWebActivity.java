package com.chanxa.wnb.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.activity.base.WnbBaseActivity;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.view.ProgressWebView;
import com.wtm.library.base.BaseActivity;
import com.wtm.library.http.HttpParams;
import com.wtm.library.inject.ViewInject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OnLineRechargeWebActivity extends DefaultTitleActivity {

    @InjectView(R.id.webview)
    ProgressWebView mWebview;
    String number;
    String pwd;
    String money;
    int type;
    String apiPath = "";
    String unionPay_apiPath = "http://app.bi-uc.com/UnionpayRequestApi.bi?b=" + AppConfig.IDENTITYKEY;
    String alipay_apiPath = "http://app.bi-uc.com/AlipayRequestApi.bi?b=" + AppConfig.IDENTITYKEY;
    String returnPageUrl="http://www.chanxa.com/Blank.html";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    @Override
    public String initTitleText() {
        return "充值";
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        super.initView();
        Intent intent = getIntent();
        number = intent.getStringExtra("number");
        pwd = intent.getStringExtra("pwd");
        money = intent.getStringExtra("money");
        type = intent.getIntExtra("type", 1);
        HashMap hashMap = new HashMap();
        hashMap.put("卡号", number);
        hashMap.put("密钥", pwd);
        hashMap.put("充值金额", money);
        hashMap.put("ACTION", "充值");
        switch (type) {
            case OnLineRechargeActivity.UNIONPAY:
                apiPath = unionPay_apiPath;
                //hashMap.put("PageRetUrl", "http://www.cbn123.com/vipMemberUser.shtml");
                hashMap.put("PageRetUrl", returnPageUrl);
                break;
            case OnLineRechargeActivity.ALIPAY:
                apiPath = alipay_apiPath;
                //hashMap.put("return_url", "http://www.bi-uc.com/");
                hashMap.put("return_url", returnPageUrl);
                break;
        }
        HttpParams httpParams = new HttpParams(hashMap);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setUseWideViewPort(true);
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals(returnPageUrl)) {
                    ViewInject.toast("充值成功");
                    setResult(RESULT_OK, null);
                    finish();
                    return true;
                }
                mWebview.loadUrl(url);// 使用当前WebView处理跳转
                return true;// true表示此事件在此处被处理，不需要再广播
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // 有页面跳转时被回调
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // 页面跳转结束后被回调
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // 出错
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        mWebview.postUrl(apiPath, httpParams.toString().getBytes());
        //mWebview.loadUrl("http://ai.cx/5ohq");
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
            mWebview.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}