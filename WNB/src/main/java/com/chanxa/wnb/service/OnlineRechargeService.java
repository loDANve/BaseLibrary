package com.chanxa.wnb.service;

import android.content.Intent;

import com.chanxa.wnb.activity.OnLineRechargeWebActivity;
import com.chanxa.wnb.bean.CardToken;
import com.chanxa.wnb.bean.MakeRecord;
import com.chanxa.wnb.service.base.BaseAsynCallBack;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.xml.DATAxmlHelper;
import com.chanxa.wnb.xml.XmlUtils;
import com.chanxa.wnb.xml.mapping.Mapping;
import com.wtm.library.base.WActivityManager;
import com.wtm.library.http.HttpAsynCallBack;
import com.wtm.library.http.HttpParams;
import com.wtm.library.http.HttpResponseCallBack;
import com.wtm.library.http.HttpTask;
import com.wtm.library.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 在线充值有关业务
 * Created by CHANXA on 2014/12/23.
 */
public class OnlineRechargeService{

    private final String action = "充值";
    private HttpTask httpTask;

    public void OnlineRechargeService() {
        httpTask = new HttpTask();
    }


    //支付宝充值
    public void alipayRecharge(String cardNumber,String pwd,String money) {
        String apiPath = "http://app.bi-uc.com/AlipayRequestApi.bi?b=43-AA-27-30-B2-6E-92-3F";
        HashMap hashMap = new HashMap();
        hashMap.put("卡号",cardNumber);
        hashMap.put("密钥",pwd);
        hashMap.put("充值金额", money);
        hashMap.put("ACTION", "充值");
        hashMap.put("return_url", "http://www.bi-uc.com/");
        HttpParams httpParams = new HttpParams(hashMap);
        if (httpTask == null) {
            httpTask = new HttpTask();
        }
        httpTask.urlPost(apiPath,httpParams,new HttpAsynCallBack(new HttpResponseCallBack() {
            @Override
            public void complete(byte[] data) {
                String dataStr = new String(data);
                Intent intent = new Intent(WActivityManager.getInstance().topActivity(), OnLineRechargeWebActivity.class);
                intent.putExtra("abc", dataStr);
                WActivityManager.getInstance().topActivity().startActivity(intent);
            }

            @Override
            public void onLoad(long count, long current, float currentDownLoadScale) {

            }

            @Override
            public void error(Exception e, Object msg, int responseCode) {

            }

            @Override
            public void onStart() {

            }
        }));

    }


    public void unionpayRecharge(String cardNumber,String pwd,String money){
        String apiPath = "http://app.bi-uc.com/UnionpayRequestApi.bi?b=43-AA-27-30-B2-6E-92-3F";
        HashMap hashMap = new HashMap();
        hashMap.put("卡号",cardNumber);
        hashMap.put("密钥",pwd);
        hashMap.put("充值金额", money);
        hashMap.put("ACTION", "充值");
        hashMap.put("PageRetUrl", "http://www.cbn123.com/vipMemberUser.shtml");
        HttpParams httpParams = new HttpParams(hashMap);
        if (httpTask == null) {
            httpTask = new HttpTask();
        }
        httpTask.urlPost(apiPath,httpParams,new HttpAsynCallBack(new HttpResponseCallBack() {
            @Override
            public void complete(byte[] data) {
                String dataStr = new String(data);
                Intent intent = new Intent(WActivityManager.getInstance().topActivity(), OnLineRechargeWebActivity.class);
                intent.putExtra("abc", dataStr);
                WActivityManager.getInstance().topActivity().startActivity(intent);
            }
            @Override
            public void onLoad(long count, long current, float currentDownLoadScale) {

            }
            @Override
            public void error(Exception e, Object msg, int responseCode) {

            }
            @Override
            public void onStart() {

            }
        }));
    }
}