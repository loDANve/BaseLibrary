package com.chanxa.wnb.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.AskActivity;
import com.chanxa.wnb.activity.CCTransferActivity;
import com.chanxa.wnb.activity.ConvenienceServicesActivity;
import com.chanxa.wnb.activity.LookRecordActivity;
import com.chanxa.wnb.activity.MemberDataActivity;
import com.chanxa.wnb.activity.MyMakeActivity;
import com.chanxa.wnb.activity.OnLineRechargeActivity;
import com.chanxa.wnb.activity.PayPhoneBillActivity;
import com.chanxa.wnb.activity.RebateQueryActivity;
import com.chanxa.wnb.activity.SafetyCenterActivity;
import com.chanxa.wnb.activity.SystemMessageActivity;
import com.chanxa.wnb.activity.WebActivity;
import com.chanxa.wnb.activity.shop.GoodsDetailsActivity;
import com.chanxa.wnb.activity.shop.StoreGoodsActivity;
import com.chanxa.wnb.bean.Advertising;
import com.chanxa.wnb.bean.Card;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.bean.onLineShop.Ask;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.dao.AskDao;
import com.chanxa.wnb.service.DataService;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.service.StoreService;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.wtm.library.base.BaseFragment;
import com.wtm.library.inject.ViewInject;
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
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private SliderLayout img_homeBannerSlider;
    private ImageView img_goLookRecord;
    private Fragment parentFragment;
    private PagerIndicator custom_indicator;
    private ProgressDialog progressDialog;
    public void setParentFragment(Fragment parentFragment) {
        this.parentFragment = parentFragment;
    }

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        progressDialog = ProgressDialogBuilder.builderDialog(getActivity());
        return inflater.inflate(R.layout.fragment_home_content, null, false);
    }

    @Override
    public void initView() {
        super.initView();
        img_goLookRecord = (ImageView) rootView.findViewById(R.id.img_goLookRecord);
        img_homeBannerSlider = (SliderLayout) rootView.findViewById(R.id.shopBannerSlider);
        custom_indicator = (PagerIndicator) rootView.findViewById(R.id.custom_indicator);
        img_homeBannerSlider.setCustomIndicator(custom_indicator);
        rootView.findViewById(R.id.img_toggle).setOnClickListener(this);
        initAdvertising();
        rootView.findViewById(R.id.container_memberData).setOnClickListener(this);      //会员信息
        rootView.findViewById(R.id.container_online_recharge).setOnClickListener(this); //在线充值
        rootView.findViewById(R.id.container_rebate_query).setOnClickListener(this);    //返利查询
        rootView.findViewById(R.id.container_cc_transfer).setOnClickListener(this);     //卡卡转账
        rootView.findViewById(R.id.container_mymake).setOnClickListener(this);          //我的愉悦
        rootView.findViewById(R.id.container_game_center).setOnClickListener(this);     //游戏中心
        rootView.findViewById(R.id.container_safety_center).setOnClickListener(this);   //安全中心
        rootView.findViewById(R.id.container_system_message).setOnClickListener(this);  //系统消息
        rootView.findViewById(R.id.container_convenience_services).setOnClickListener(this);  //便民服务
        img_goLookRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LookRecordActivity.class));
            }
        });
    }

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
        img_homeBannerSlider.removeAllSliders();
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
                                intent.putExtra("storeMark",storeMark);
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
                                intent.putExtra("goodsMark",goodsMark);
                                startActivity(intent);
                                return;
                            } catch (Exception e) {
                                return;
                            }
                        }
                        //Intent intent = new Intent(getActivity(), WebActivity.class);
                        intent.setClass(getActivity(), WebActivity.class);
                        intent.putExtra("webUrl",url);
                        startActivity(intent);
                    }
                }
            });
            img_homeBannerSlider.addSlider(textSliderView);
        }
    }

    public void panicBuy(String goodsMark){
        final Card card = WnbApplication.getInstance().getCard();
        if (card == null) {
            return;
        }
        new StoreService().obtianGoodsDetails(WnbApplication.getInstance().getCardToken(),goodsMark,new ServiceCallBack<Goods>() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                //ViewInject.toast("获取抢购信息失败");
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
                Goods goods = (Goods) resultObj;

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
                                onError(null,-1);
                                return;
                            }
                            Intent intent = new Intent(getActivity(), AskActivity.class);
                            intent.putExtra("ArrayList<Ask>", askArrayList);
                            intent.putExtra(Goods.class.getSimpleName(), goods);
                            startActivity(intent);
                        } catch (Exception e) {
                            onError(e,-1);
                        }
                    } else {
                        ViewInject.toast("对不起，当前时间不在抢购时间内");
                    }
                    progressDialog.dismiss();
                } catch (ParseException e) {
                    onError(e,-1);
                }
            }
            @Override
            public void onEXECSuccess(Goods resultObj, int pageSize, int dataSize) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.container_memberData:
                intent.setClass(getActivity(), MemberDataActivity.class);
                break;
            case R.id.container_online_recharge:
                intent.setClass(getActivity(), OnLineRechargeActivity.class);
                break;
            case R.id.container_rebate_query:
                intent.setClass(getActivity(), RebateQueryActivity.class);
                break;
            case R.id.container_cc_transfer:
                intent.setClass(getActivity(), CCTransferActivity.class);
                break;
            case R.id.container_mymake:
                intent.setClass(getActivity(), MyMakeActivity.class);
                break;
            case R.id.container_game_center:
                return;
            case R.id.container_safety_center:
                intent.setClass(getActivity(), SafetyCenterActivity.class);
                break;
            case R.id.container_system_message:
                intent.setClass(getActivity(), SystemMessageActivity.class);
                break;
            case R.id.container_convenience_services:
                intent.setClass(getActivity(), PayPhoneBillActivity.class);
                break;
            case R.id.img_toggle:
                ((MainFragment) parentFragment).activityPagerIndex(0);
                return;
        }
        startActivity(intent);
    }
}