package com.chanxa.wnb.service;

import android.content.Intent;

import com.chanxa.wnb.activity.OnLineRechargeWebActivity;
import com.chanxa.wnb.bean.Advertising;
import com.chanxa.wnb.bean.CardToken;
import com.chanxa.wnb.bean.MakeRecord;
import com.chanxa.wnb.bean.RechargeProducts;
import com.chanxa.wnb.bean.Segment;
import com.chanxa.wnb.bean.Store;
import com.chanxa.wnb.bean.User;
import com.chanxa.wnb.dao.UserDAO;
import com.chanxa.wnb.service.base.BaseAsynCallBack;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.service.base.BaseService;
import com.chanxa.wnb.xml.DATAxmlHelper;
import com.chanxa.wnb.xml.XmlUtils;
import com.chanxa.wnb.xml.mapping.Mapping;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 话费充值有关业务
 * Created by CHANXA on 2014/12/22.
 */
public class RechargeRateService extends BaseService {

    private final String action_querySegment = "查询号段";
    private final String action_rechargeOrder = "充值下单";
    private final String action_getRechargeProducts = "获取话费充值产品";
    private final String action_getRechargeProducts2 = "获取话费充值产品2";

    /**
     * 查询号段
     */
    public void querySegment(CardToken cardToken, String phoneNum, final ServiceCallBack callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap map = new HashMap();
        map.put("令牌", cardToken.getToken());
        map.put("校验码", cardToken.getCheckCode());
        map.put("卡标识", cardToken.getMark());
        map.put("手机", phoneNum);
        doRequest(action_querySegment, map, new BaseAsynCallBack(new BaseCallBack() {
            @Override
            public void onStart() {
                callBack.onStart();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                LogUtils.e(e + "");
                callBack.onError(e, stateCode);
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                LogUtils.e(errroMsg);
                callBack.onEXECisFalse(errroMsg);
            }

            @Override
            public void onEXECSuccess(String result) {
                //<DATA EXEC="True" 运营商="移动" 省="广东" 城市区号="0755" 描述="" />
                try {
                    ArrayList<Segment> segmentArrayList = (ArrayList<Segment>) XmlUtils.readXML2BeanList(result, Segment.class, "DATA", Mapping.segmentMapping);
                    callBack.onEXECSuccess(segmentArrayList);
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.onError(e, -1);
                }
            }

            @Override
            public void onComplete(String result) {
                LogUtils.e(result);
            }
        }));
    }


    /**
     * 获取话费-对应单个面额的充值产品
     *
     * @param cardToken
     * @param denomination
     * @param carrieroperator
     * @param province
     * @param callBack
     */
    public void getRechargeProduct(CardToken cardToken, String denomination, String carrieroperator, String province, final ServiceCallBack callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap map = new HashMap();
        map.put("令牌", cardToken.getToken());
        map.put("校验码", cardToken.getCheckCode());
        map.put("卡标识", cardToken.getMark());
        map.put("面额", denomination);
        map.put("运营商", carrieroperator);
        map.put("省", province);
        doRequest(action_getRechargeProducts, map, new BaseAsynCallBack(new BaseCallBack() {
            @Override
            public void onStart() {
                callBack.onStart();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                LogUtils.e(e + "");
                callBack.onError(e, stateCode);
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                LogUtils.e(errroMsg);
                callBack.onEXECisFalse(errroMsg);
            }

            @Override
            public void onEXECSuccess(String result) {
                //<DATA EXEC="True" 标识="8081" 面额="50" 售价="49.825" 运营商="移动" 充值处理时间="5分钟" 省="广东" 类型="移动电话" />
                //RechargeProducts
                try {
                    ArrayList<RechargeProducts> rechargeProductsArrayList = (ArrayList<RechargeProducts>) XmlUtils.readXML2BeanList(result, RechargeProducts.class, "DATA", Mapping.rechargeProductsMapping);
                    callBack.onEXECSuccess(rechargeProductsArrayList);
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.onError(e, -1);
                }
            }

            @Override
            public void onComplete(String result) {
                LogUtils.e(result);
            }
        }));
    }

    /**
     * 获取话费-对应省,运营商的所有面额充值产品
     *
     * @param cardToken
     * @param carrieroperator
     * @param province
     * @param callBack
     */
    public void getRechargeProducts(CardToken cardToken, String carrieroperator, String province, final ServiceCallBack callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap map = new HashMap();
        map.put("令牌", cardToken.getToken());
        map.put("校验码", cardToken.getCheckCode());
        map.put("卡标识", cardToken.getMark());
        map.put("运营商", carrieroperator);
        map.put("省", province);
        doRequest(action_getRechargeProducts2, map, new BaseAsynCallBack(new BaseCallBack() {
            @Override
            public void onStart() {
                callBack.onStart();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                LogUtils.e(e + "");
                callBack.onError(e, stateCode);
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                LogUtils.e(errroMsg);
                callBack.onEXECisFalse(errroMsg);
            }

            @Override
            public void onEXECSuccess(String result) {
                //<DATA EXEC="True"><产品 标识="26776" 面额="20" 售价="20.09" 运营商="移动" 充值处理时间="5分钟" 省="广东" 类型="移动电话" />
                // <产品 标识="32921" 面额="10" 售价="10.045" 运营商="移动" 充值处理时间="5分钟" 省="广东" 类型="移动电话" />
                // <产品 标识="50078860" 面额="5" 售价="5.075" 运营商="移动" 充值处理时间="5分钟" 省="广东" 类型="移动电话" />
                // <产品 标识="8081" 面额="50" 售价="49.825" 运营商="移动" 充值处理时间="5分钟" 省="广东" 类型="移动电话" />
                // <产品 标识="8115" 面额="100" 售价="99.65" 运营商="移动" 充值处理时间="5分钟" 省="广东" 类型="移动电话" />
                // <产品 标识="8183" 面额="200" 售价="199.3" 运营商="移动" 充值处理时间="5分钟" 省="广东" 类型="移动电话" />
                // <产品 标识="8251" 面额="300" 售价="298.95" 运营商="移动" 充值处理时间="5分钟" 省="广东" 类型="移动电话" />
                // <产品 标识="8387" 面额="500" 售价="498.25" 运营商="移动" 充值处理时间="5分钟" 省="广东" 类型="移动电话" />
                // <产品 标识="9922" 面额="30" 售价="29.895" 运营商="移动" 充值处理时间="5分钟" 省="广东" 类型="移动电话" /></DATA>
                try {
                    ArrayList<RechargeProducts> rechargeProductsArrayList = (ArrayList<RechargeProducts>) XmlUtils.readXML2BeanList(result, RechargeProducts.class, "产品", Mapping.rechargeProductsMapping);
                    callBack.onEXECSuccess(rechargeProductsArrayList);
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.onError(e, -1);
                }
            }

            @Override
            public void onComplete(String result) {
                LogUtils.e(result);
            }
        }));
    }

    /**
     * 充值下单
     */
    public void rechargeOrder(CardToken cardToken, String phoneNum, String pwd, String denomination, final BaseCallBack callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap map = new HashMap();
        map.put("令牌", cardToken.getToken());
        map.put("校验码", cardToken.getCheckCode());
        map.put("卡标识", cardToken.getMark());
        map.put("手机", phoneNum);
        map.put("面额", denomination);
        map.put("密钥", pwd);
        doRequest(action_rechargeOrder, map, new BaseAsynCallBack(callBack));
    }

}
