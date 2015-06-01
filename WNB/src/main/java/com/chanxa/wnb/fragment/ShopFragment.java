package com.chanxa.wnb.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.AskActivity;
import com.chanxa.wnb.activity.WebActivity;
import com.chanxa.wnb.activity.shop.GoodsDetailsActivity;
import com.chanxa.wnb.activity.shop.GoodsSearchActivity;
import com.chanxa.wnb.activity.shop.MallMainActivity;
import com.chanxa.wnb.activity.shop.StoreGoodsActivity;
import com.chanxa.wnb.adapter.ShopHomeGridAdapter;
import com.chanxa.wnb.bean.Advertising;
import com.chanxa.wnb.bean.Card;
import com.chanxa.wnb.bean.FenLeiStore;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.bean.onLineShop.Ask;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.dao.AskDao;
import com.chanxa.wnb.service.DataService;
import com.chanxa.wnb.service.StoreService;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.chanxa.wnb.view.PullToRefreshEventScrollView;
import com.chanxa.wnb.view.ScrollGridView;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.wtm.library.base.BaseFragment;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.DeviceUtils;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;
import com.wtm.library.utils.SystemTool;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wtm on 2014/12/12.
 */
public class ShopFragment extends BaseFragment implements View.OnClickListener {

    SliderLayout mShopBannerSlider;
    PagerIndicator custom_indicator;
    ScrollGridView mShopGridView;
    EditText mEditShopSearch;
    ImageView mImgShopMenu;
    PullToRefreshEventScrollView pullToRefreshEventScrollView;
    private ShopHomeGridAdapter adapter;
    private PopupWindow menu;
    private TextView tv_shop_fenlei, tv_shop_cart, tv_shop_member_center;//泡泡view

    private int pageIndex = 0;
    private int pageMax = 0;
    private static final int PAGESIZE = 10;
    private ArrayList<Goods> goodsArrayList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private MainFragment parentFragment;

    private int openTab = 0;//打开页面的页数

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        progressDialog = ProgressDialogBuilder.builderDialog(getActivity());
        return inflater.inflate(R.layout.fragment_shop, null, false);
    }

    @Override
    public void initView() {
        super.initView();
        initMenu();
        mShopBannerSlider = (SliderLayout) rootView.findViewById(R.id.shopBannerSlider);
        custom_indicator = (PagerIndicator) rootView.findViewById(R.id.custom_indicator);
        mShopBannerSlider.setCustomIndicator(custom_indicator);
        mShopGridView = (ScrollGridView) rootView.findViewById(R.id.shop_gridView);
        mEditShopSearch = (EditText) rootView.findViewById(R.id.edit_shop_search);
        mImgShopMenu = (ImageView) rootView.findViewById(R.id.img_shop_menu);
        mImgShopMenu.setOnClickListener(this);
        mEditShopSearch.setOnClickListener(this);
        pullToRefreshEventScrollView = (PullToRefreshEventScrollView) rootView.findViewById(R.id.scrollView_home);
        pullToRefreshEventScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        initAdvertising();
        adapter = new ShopHomeGridAdapter(getActivity(), goodsArrayList);
        mShopGridView.setAdapter(adapter);
        mShopGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GoodsDetailsActivity.class);
                intent.putExtra("goodsMark", goodsArrayList.get(position).getMark());
                startActivity(intent);
            }
        });
        pullToRefreshEventScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageIndex = 0;
                obtainGoodsData(pageIndex, false);
                if (parentFragment != null) {
                    parentFragment.refreshChildFragmentAdvertising();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageIndex++;
                if (pageIndex < pageMax) {
                    obtainGoodsData(pageIndex, true);
                    return;
                }
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshEventScrollView.onRefreshComplete();
                    }
                });
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        obtainGoodsData(pageIndex, false);
    }

    private void initMenu() {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.menu_shop, null, false);
        tv_shop_fenlei = (TextView) v.findViewById(R.id.tv_shop_fenlei);
        tv_shop_cart = (TextView) v.findViewById(R.id.tv_shop_cart);
        tv_shop_member_center = (TextView) v.findViewById(R.id.tv_shop_member_center);
        tv_shop_fenlei.setOnClickListener(this);
        tv_shop_cart.setOnClickListener(this);
        tv_shop_member_center.setOnClickListener(this);
        int width = DeviceUtils.dip2px(getActivity(), 120);
        int height = width * 273 / 244;
        v.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        menu = new PopupWindow(v, width, height);
        menu.setFocusable(true);
        menu.setBackgroundDrawable(new ColorDrawable(0x070000));
        menu.setOutsideTouchable(true);
    }

    /*
     * 广告位设置start
     */
    private void initAdvertising() {
        ArrayList<Advertising> advertisingArrayList = WnbApplication.getInstance().getAdvertisingArrayList();
        if (advertisingArrayList == null) {
            new DataService().obtainConfig(new ServiceCallBack() {
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
                public void onEXECSuccess(Object resultObj) {
                    ArrayList<Advertising> advertisingArrayList = (ArrayList<Advertising>) resultObj;
                    for(int i=0 ; i<advertisingArrayList.size(); i++){
                        LogUtils.fff(advertisingArrayList.get(i).toString());
                    }
                    advertisingData2View(advertisingArrayList);
                }

                @Override
                public void onEXECSuccess(Object resultObj, int pageSize, int dataSize) {


                }
            });
            return;
        }
        advertisingData2View(advertisingArrayList);
    }

    public void advertisingData2View(ArrayList<Advertising> advertisingArrayList) {
        mShopBannerSlider.removeAllSliders();
        for (final Advertising advertising : advertisingArrayList) {
            DefaultSliderView textSliderView = new DefaultSliderView(
                    getActivity());
            textSliderView.description(advertising.getTitle()).image(AppConfig.PATH_PREFIX + advertising.getSrc());
            textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView baseSliderView) {
                    String url = advertising.getUrl();
                    if (!StringUtils.isEmpty(url)) {
                        if (url.toLowerCase().startsWith("LocalOpen:PanicBuy".toLowerCase())) {//抢购活动
                            try {
                                String goodsMark = url.substring(url.indexOf("(") + 1, url.indexOf(")"));
                                panicBuy(goodsMark);
                                return;
                            } catch (Exception e) {
                                return;
                            }
                        }
                        Intent intent = new Intent();
                        if (url.toLowerCase().startsWith("LocalOpen:StoreIndex".toLowerCase())) {
                            try {
                                String storeMark = url.substring(url.indexOf("(") + 1, url.indexOf(")"));
                                intent.setClass(getActivity(), StoreGoodsActivity.class);
                                intent.putExtra("storeMark", storeMark);
                                startActivity(intent);
                                return;
                            } catch (Exception e) {
                                return;
                            }
                        }
                        if (url.toLowerCase().startsWith("LocalOpen:Commodity".toLowerCase())) {
                            try {
                                String goodsMark = url.substring(url.indexOf("(") + 1, url.indexOf(")"));
                                intent.setClass(getActivity(), GoodsDetailsActivity.class);
                                intent.putExtra("goodsMark", goodsMark);
                                startActivity(intent);
                                return;
                            } catch (Exception e) {
                                return;
                            }
                        }
                        //Intent intent = new Intent(getActivity(), WebActivity.class);
                        intent.setClass(getActivity(), WebActivity.class);
                        intent.putExtra("webUrl", url);
                        startActivity(intent);
                    }
                }
            });
            mShopBannerSlider.addSlider(textSliderView);
        }
    }

    /* 广告位设置end*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_shop_menu:
                menu.showAsDropDown(mImgShopMenu, 0, 0);
                return;
            case R.id.edit_shop_search://搜索功能暂时不需要
                Intent intent = new Intent(getActivity(), GoodsSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_shop_fenlei:
                openTab = 0;
                menu.dismiss();
                obtainGoodsType();
                return;
            case R.id.tv_shop_cart:
                openTab = 1;
                menu.dismiss();
                obtainGoodsType();
                return;
            case R.id.tv_shop_member_center:
                openTab = 2;
                menu.dismiss();
                obtainGoodsType();
                return;
        }
    }

    //获取商品数据
    private void obtainGoodsData(int pageIndex, final boolean isLoadMore) {
        if (!WnbApplication.getInstance().verificationData()) {
            return;
        }
        new StoreService().obtainGoods(WnbApplication.getInstance().getCardToken(), pageIndex, PAGESIZE, null, null, "线上订购", null, null, new ServiceCallBack<ArrayList<Goods>>() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                progressDialog.dismiss();
                pullToRefreshEventScrollView.onRefreshComplete();
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                progressDialog.dismiss();
                ViewInject.toast(errroMsg);
                pullToRefreshEventScrollView.onRefreshComplete();
            }

            @Override
            public void onEXECSuccess(ArrayList<Goods> resultObj) {
                if (!isLoadMore) {
                    goodsArrayList.clear();
                }
                goodsArrayList.addAll(resultObj);
                pullToRefreshEventScrollView.onRefreshComplete();
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onEXECSuccess(ArrayList<Goods> resultObj, int pageSize, int dataSize) {
                pageMax = pageSize;
            }
        });
    }

    //获取商品分类,成功后跳转界面
    public void obtainGoodsType() {
        if (!WnbApplication.getInstance().verificationData()) {
            return;
        }
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

    public void goMallMainActivity() {
        Intent intent = new Intent(getActivity(), MallMainActivity.class);
        intent.putExtra("openTab", openTab);
        startActivity(intent);
    }

    public MainFragment getMainParentFragment() {
        return parentFragment;
    }

    public void setMainParentFragment(MainFragment parentFragment) {
        this.parentFragment = parentFragment;
    }

    /**
     * 抢购
     *
     * @param goodsMark
     */
    public void panicBuy(String goodsMark) {
        final Card card = WnbApplication.getInstance().getCard();
        if (card == null) {
            return;
        }
        new StoreService().obtianGoodsDetails(WnbApplication.getInstance().getCardToken(), goodsMark, new ServiceCallBack<Goods>() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                ViewInject.toast("对不起，当前时间不在抢购时间内");
                progressDialog.dismiss();
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                ViewInject.toast(errroMsg);
                progressDialog.dismiss();
            }

            @Override
            public void onEXECSuccess(Goods resultObj) {
                Goods goods = resultObj;

                double balance = StringUtils.toDouble(card.getBalance());
                double goodsMoney = StringUtils.toDouble(goods.getMoney());
                if (balance < goodsMoney) {
                    ViewInject.toast("余额不足,请先充值");
                    progressDialog.dismiss();
                    return;
                }
                try {
                    DateFormat df = new SimpleDateFormat("HH:mm");
                    String curTime = SystemTool.getDataTime();
                    String startTime = goods.getPanicBuyStartTime();
                    String endTime = goods.getPanicBuyendTime();
                    Date curDate = df.parse(curTime);
                    Date startDate = df.parse(startTime);
                    Date endDate = df.parse(endTime);
                    long curtime = curDate.getTime();
                    long starttime = startDate.getTime();
                    long endtime = endDate.getTime();
                    if (curtime >= starttime && curtime <= endtime) {
                        try {
                            AskDao askDao = new AskDao(getActivity().getApplicationContext());
                            ArrayList<Ask> askArrayList = askDao.queryAsk(3);
                            if (askArrayList == null) {
                                onError(null, -1);
                                return;
                            }
                            Intent intent = new Intent(getActivity(), AskActivity.class);
                            intent.putExtra("ArrayList<Ask>", askArrayList);
                            intent.putExtra(Goods.class.getSimpleName(), goods);
                            startActivity(intent);
                        } catch (Exception e) {
                            onError(e, -1);
                        }
                    } else {
                        ViewInject.toast("对不起，当前时间不在抢购时间内");
                    }
                    progressDialog.dismiss();
                } catch (ParseException e) {
                    onError(e, -1);
                }
            }

            @Override
            public void onEXECSuccess(Goods resultObj, int pageSize, int dataSize) {

            }
        });
    }

}