package com.chanxa.wnb.activity.shop;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.chanxa.wnb.activity.base.WnbBaseActivity;
import com.chanxa.wnb.bean.FenLeiStore;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.bean.onLineShop.MyCollection;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.MemberService;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.service.StoreService;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.chanxa.wnb.view.ShopAddSubViewDetails;
import com.chanxa.wnb.xml.DATAxmlHelper;
import com.wtm.library.base.WActivityManager;
import com.wtm.library.bitmap.WBitmap;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import org.dom4j.DocumentException;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class GoodsDetailsActivity extends WnbBaseActivity {
    @InjectView(R.id.img_goodsDetails)
    ImageView mImgGoodsDetails;
    @InjectView(R.id.tv_goodsDetailsDescription)
    TextView mTvGoodsDetailsDescription;
    @InjectView(R.id.tv_goodsDetailsMoney)
    TextView mTvGoodsDetailsMoney;
    @InjectView(R.id.btn_add_shop_cart)
    RelativeLayout mBtnAddShopCart;
    @InjectView(R.id.picker_goodsNumber)
    ShopAddSubViewDetails mPickerGoodsNumber;
    @InjectView(R.id.img_goods_details_cart)
    ImageView mImgGoodsDetailsCart;
    @InjectView(R.id.webview_goodsDetails)
    WebView mWebviewGoodsDetails;
    @InjectView(R.id.btn_goStore)
    Button mBtnGoStore;
    @InjectView(R.id.container_collection)
    RelativeLayout container_collection;
    @InjectView(R.id.img_goodsDetails_share)
    ImageView mImgGoodsDetailsShare;
    private WBitmap wBitmap;
    private Goods curGoods;
    private ProgressDialog progressDialog;
    private String imgStyle = "<style>img { width: 100%; }</style>";
    private MemberService memberService;
    private MyCollection myCollection;
    private String goodsMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = ProgressDialogBuilder.builderDialog(this);
        setContentView(R.layout.activity_goods_details);
    }

    @Override
    public void initData() {
        goodsMark = getIntent().getStringExtra("goodsMark");
        wBitmap = WnbApplication.getInstance().getwBitmap();
        myCollection = WnbApplication.getInstance().getCollection();
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        super.initView();
        mBtnAddShopCart.setOnClickListener(this);
        mImgGoodsDetailsShare.setOnClickListener(this);
        findViewById(R.id.tv_back).setOnClickListener(this);
        mImgGoodsDetailsCart.setOnClickListener(this);
        mBtnGoStore.setOnClickListener(this);
        container_collection.setOnClickListener(this);
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
        obtainGoodsDetails(goodsMark);

    }

    private void goods2View() {
        if (curGoods == null) {
            return;
        }
        mTvGoodsDetailsMoney.setText("¥" + StringUtils.toDoubleStr(curGoods.getMoney(), 2));
        mTvGoodsDetailsDescription.setText(curGoods.getName() + "\n" + curGoods.getRemarks());
        String path = curGoods.getImgPath();
        if (!StringUtils.isEmpty(path)) {
            path = path.replaceAll("_small_", "");
            if (path.startsWith(AppConfig.PATH_PREFIX)) {
                wBitmap.display(mImgGoodsDetails, path);
            } else {
                wBitmap.display(mImgGoodsDetails, AppConfig.PATH_PREFIX + path);
            }
        }
        if (myCollection != null) {
            container_collection.setSelected(myCollection.goodsIsCollection(curGoods.getMark()));
        }
    }

    /**
     * 获取商品详情
     */
    public void obtainGoodsDetails(String goodsMark) {
        new StoreService().obtianGoodsDetails(getCardToken(), goodsMark, new ServiceCallBack<Goods>() {
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
                goods2View();
                LogUtils.e(curGoods.getDetails());
                mWebviewGoodsDetails.loadDataWithBaseURL(null, imgStyle + curGoods.getDetails(), "text/html", "utf-8", null);
                if (myCollection == null) {
                    obtainCollection();
                }
            }

            @Override
            public void onEXECSuccess(Goods resultObj, int pageSize, int dataSize) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_add_shop_cart:
                curGoods.setCardNumber(WnbApplication.getInstance().getCard().getCardNumber());
                curGoods.setNumber(mPickerGoodsNumber.getNumber());
                if (WnbApplication.getInstance().getGoodsCart().addGoods2Cart(curGoods)) {
                    ViewInject.toast("添加成功");
                } else {
                    ViewInject.toast("不能添加不同店铺的商品,请先结算");
                }
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.img_goodsDetails_share:
                shareApp();
                break;
            case R.id.img_goods_details_cart:
                obtainGoodsType();
                return;
            case R.id.container_collection:
                container_collection.setSelected(!container_collection.isSelected());
                changeCollection(container_collection.isSelected());//更改收藏
                return;
            case R.id.btn_goStore:
                if (curGoods == null) {
                    return;
                }
                if (StringUtils.isEmpty(curGoods.getStoreMark())) {
                    return;
                }
                Intent intent = new Intent(this, StoreGoodsActivity.class);
                intent.putExtra("storeMark", curGoods.getStoreMark());
                startActivity(intent);
                return;
        }
    }

    private void shareApp() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字
        oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        //oks.setTitleUrl("http://android.myapp.com/myapp/detail.htm?apkName=com.chanxa.wnb");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("万能宝应用下载地址：http://android.myapp.com/myapp/detail.htm?apkName=com.chanxa.wnb");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://android.myapp.com/myapp/detail.htm?apkName=com.chanxa.wnb");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.chanxa.com/");
        // 启动分享GUI
        oks.show(this);
    }

    public void goMallMainActivity() {
        Intent intent = new Intent(this, MallMainActivity.class);
        intent.putExtra("openTab", 1);
        try {
            WActivityManager.getInstance().finishActivity(SearchResultActivity.class);
            WActivityManager.getInstance().finishActivity(MallMainActivity.class);
        } catch (Exception e) {
        } finally {
            startActivity(intent);
            finish();
        }
    }

    //获取商品分类,成功后跳转界面
    public void obtainGoodsType() {
        if (WnbApplication.getInstance().getFenleiArrayListArrayList() != null) {
            goMallMainActivity();
            return;
        }
        new StoreService().obtainStoreGoodsType(WnbApplication.getInstance().getCardToken(), new ServiceCallBack<ArrayList<FenLeiStore>>() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                ViewInject.toast("请检查网络");
                progressDialog.dismiss();
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                ViewInject.toast(errroMsg);
                progressDialog.dismiss();
            }

            @Override
            public void onEXECSuccess(ArrayList<FenLeiStore> resultObj) {
                progressDialog.dismiss();
                WnbApplication.getInstance().setFenleiArrayListArrayList(resultObj);
                goMallMainActivity();
            }

            @Override
            public void onEXECSuccess(ArrayList<FenLeiStore> resultObj, int pageSize, int dataSize) {

            }
        });
    }

    /**
     * 当前商品是否收藏
     *
     * @param curGoodsIsCollection
     */
    public void changeCollection(boolean curGoodsIsCollection) {
        if (myCollection != null) {//如果当前已经获取过
            if (curGoodsIsCollection) {
                myCollection.addOneCollection(curGoods.getMark());
            } else {
                myCollection.removeOneCollection(curGoods.getMark());
            }
            upDataCollection();
            return;
        }
    }

    /**
     * 获取收藏
     */
    public void obtainCollection() {
        if (memberService == null) {
            memberService = new MemberService();
        }
        memberService.obtainCustomExpansionData(getCardToken(), AppConfig.Collection_KEY, new BaseCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {
                LogUtils.e(stateCode + "e==>" + e);
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                LogUtils.e(errroMsg);
            }

            @Override
            public void onEXECSuccess(String result) {
                LogUtils.e(result);
                try {
                    String collStr = DATAxmlHelper.getAllAttribute(result).get("Value");
                    myCollection = new MyCollection(collStr);
                    WnbApplication.getInstance().setCollection(myCollection);
                    container_collection.setSelected(myCollection.goodsIsCollection(curGoods.getMark()));
                } catch (DocumentException e) {
                    onError(e, -1);
                }
                //upDataCollection();
            }

            @Override
            public void onComplete(String result) {

            }
        });
    }

    /**
     * 更新收藏
     */
    public void upDataCollection() {
        if (myCollection == null) {
            return;
        }
        if (memberService == null) {
            memberService = new MemberService();
        }

        memberService.upDataCustomExpansionData(getCardToken(), AppConfig.Collection_KEY, myCollection.toString(), new BaseCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {
                LogUtils.e(stateCode + "e==>" + e);
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                LogUtils.e(errroMsg);
            }

            @Override
            public void onEXECSuccess(String result) {
                LogUtils.e(result);
                WnbApplication.getInstance().setCollection(myCollection);
                container_collection.setSelected(myCollection.goodsIsCollection(curGoods.getMark()));
            }

            @Override
            public void onComplete(String result) {

            }
        });

    }
}

