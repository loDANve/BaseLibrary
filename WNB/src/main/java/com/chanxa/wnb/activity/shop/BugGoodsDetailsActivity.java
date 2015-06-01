package com.chanxa.wnb.activity.shop;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.MainActivity;
import com.chanxa.wnb.activity.base.WnbBaseActivity;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.service.StoreService;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.view.SquareRelativeLayout;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wtm.library.bitmap.WBitmap;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BugGoodsDetailsActivity extends WnbBaseActivity {

    @InjectView(R.id.tv_back)
    ImageView mTvBack;
    @InjectView(R.id.img_goodsDetails)
    ImageView mImgGoodsDetails;
    @InjectView(R.id.tv_goodsDetailsDescription)
    TextView mTvGoodsDetailsDescription;
    @InjectView(R.id.tv_goodsDetailsMoney)
    TextView mTvGoodsDetailsMoney;
    @InjectView(R.id.btn_add_shop_cart)
    Button mBtnAddShopCart;
    @InjectView(R.id.webview_goodsDetails)
    WebView mWebviewGoodsDetails;
    private WBitmap wBitmap;
    Goods curGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_goods_details);
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        if (curGoods != null) {
            mTvGoodsDetailsMoney.setText("¥" + StringUtils.toDoubleStr(curGoods.getMoney(), 2));
            mTvGoodsDetailsDescription.setText(curGoods.getRemarks());
            String path = curGoods.getImgPath();
            if (!StringUtils.isEmpty(path)) {
                path = path.replaceAll("_small_", "");
                if (path.startsWith(AppConfig.PATH_PREFIX)) {
                    wBitmap.display(mImgGoodsDetails, path);
                } else {
                    wBitmap.display(mImgGoodsDetails, AppConfig.PATH_PREFIX + path);
                }
            }
            obtainGoodsDetails(curGoods);
        }
        mBtnAddShopCart.setOnClickListener(this);
        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        super.initView();
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
    }

    /**
     * 获取商品详情
     *
     * @param goods
     */
    public void obtainGoodsDetails(Goods goods) {
        new StoreService().obtianGoodsDetails(getCardToken(), goods.getMark(), new ServiceCallBack<Goods>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {

            }

            @Override
            public void onEXECisFalse(String errroMsg) {

            }

            @Override
            public void onEXECSuccess(Goods resultObj) {
                curGoods = resultObj;
                mWebviewGoodsDetails.loadDataWithBaseURL(null, curGoods.getDetails(), "text/html", "utf-8", null);
            }

            @Override
            public void onEXECSuccess(Goods resultObj, int pageSize, int dataSize) {

            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        curGoods = (Goods) getIntent().getSerializableExtra(Goods.class.getSimpleName());
        wBitmap = WnbApplication.getInstance().getwBitmap();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        String number = curGoods.getPanicBuyNumber();
        if (number.equals("0")) {
            ViewInject.toast("对不起,你来晚了,该商品已被抢完");
            return;
        }
        new StoreService().addNewGoodsPanicBuyOrder(getCardToken(), curGoods, new BaseCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {
                ViewInject.toast("抢购失败！");
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                ViewInject.toast(errroMsg);
            }

            @Override
            public void onEXECSuccess(String result) {
                ViewInject.toast("抢购成功！");
                finish();
            }

            @Override
            public void onComplete(String result) {
            }
        });
    }
}
