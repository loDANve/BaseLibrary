package com.chanxa.wnb.activity.staff;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.wtm.library.base.BaseActivity;
import com.wtm.library.bitmap.WBitmap;
import com.wtm.library.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StaffGoodsPreviewActivity extends BaseActivity {

    @InjectView(R.id.tv_back)
    ImageView mTvBack;
    @InjectView(R.id.img_goodsDetails)
    ImageView mImgGoodsDetails;
    @InjectView(R.id.tv_goodsDetailsDescription)
    TextView mTvGoodsDetailsDescription;
    @InjectView(R.id.tv_goodsDetailsMoney)
    TextView mTvGoodsDetailsMoney;
    @InjectView(R.id.webview_goodsDetails)
    WebView mWebviewGoodsDetails;

    private Goods previewGoods;
    private WBitmap wBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_goods_preview);
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        mTvBack.setOnClickListener(this);
        if (previewGoods == null) {
            return;
        }

        mTvGoodsDetailsMoney.setText("¥" + StringUtils.toDoubleStr(previewGoods.getMoney(), 2));
        mTvGoodsDetailsDescription.setText(previewGoods.getName()+"\n"+previewGoods.getRemarks());
        String path = previewGoods.getImgPath();
        if (!StringUtils.isEmpty(path)) {
            path = path.replaceAll("_small_", "");
            if (path.startsWith(AppConfig.PATH_PREFIX)) {
                wBitmap.display(mImgGoodsDetails, path);
            } else {
                wBitmap.display(mImgGoodsDetails, AppConfig.PATH_PREFIX + path);
            }
        }

        mWebviewGoodsDetails.getSettings().setJavaScriptEnabled(true);
        mWebviewGoodsDetails.getSettings().setUseWideViewPort(true);
        mWebviewGoodsDetails.getSettings().setLoadWithOverviewMode(true);
        mWebviewGoodsDetails.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebviewGoodsDetails.setVerticalScrollBarEnabled(false);
        mWebviewGoodsDetails.setVerticalScrollbarOverlay(false);
        mWebviewGoodsDetails.setHorizontalScrollBarEnabled(false);
        mWebviewGoodsDetails.setHorizontalScrollbarOverlay(false);
        mWebviewGoodsDetails.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebviewGoodsDetails.loadUrl(url);// 使用当前WebView处理跳转
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
                //加载失败就加载空的,不显示错误页面
                mWebviewGoodsDetails.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        if (!StringUtils.isEmpty(previewGoods.getDetails())) {
            mWebviewGoodsDetails.loadDataWithBaseURL(null, previewGoods.getDetails(), "text/html", "utf-8", null);
        }
    }

    @Override
    public void initData() {
        super.initData();
        wBitmap = WnbApplication.getInstance().getwBitmap();
        previewGoods = (Goods) getIntent().getSerializableExtra(Goods.class.getSimpleName());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        finish();
    }
}
