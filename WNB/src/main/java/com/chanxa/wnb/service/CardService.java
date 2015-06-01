package com.chanxa.wnb.service;

import java.util.ArrayList;
import java.util.HashMap;


import com.chanxa.wnb.bean.Card;
import com.chanxa.wnb.bean.CardToken;
import com.chanxa.wnb.bean.Profit;
import com.chanxa.wnb.bean.ProfitRecord;
import com.chanxa.wnb.bean.Store;
import com.chanxa.wnb.bean.StoredValueRecord;
import com.chanxa.wnb.bean.User;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.dao.UserDAO;
import com.chanxa.wnb.service.base.BaseAsynCallBack;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.service.base.BaseService;
import com.chanxa.wnb.service.base.DefaultCallBack;
import com.chanxa.wnb.utils.encryption.Base64Helper;
import com.chanxa.wnb.utils.encryption.RSAEncrypt;
import com.chanxa.wnb.xml.DATAxmlHelper;
import com.chanxa.wnb.xml.XmlUtils;
import com.chanxa.wnb.xml.mapping.Mapping;
import com.wtm.library.base.WActivityManager;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;
import com.wtm.library.utils.SystemTool;

/**
 * 卡有关业务
 *
 * @author CHANXA
 */
public class CardService extends BaseService {
    private final String action_login = "卡登录";
    private final String action_changePWd = "修改卡密码";
    private final String action_changePWd2 = "修改卡密码2";//会员忘记密码,使用短信验证码修改密码
    private final String action_readCard = "读卡";
    private final String action_ccTransfer = "卡间转账";
    private final String action_obtainCardProfit = "获取卡收益";
    private final String action_obtainProfitDetails = "获取结息明细";
    private final String action_obtainStoredValueRecord = "获取储值记录";
    private final String action_mentionNow = "新增线上申请资产提现单据";//(提现)

    /**
     * 卡登录
     *
     * @param name     卡号
     * @param pwd      密码
     * @param callBack
     */
    public void login(final String name, String pwd, boolean rememberPassWord, final ServiceCallBack callBack) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("卡号", name);
        hashMap.put("密钥", pwd);

        final User user = new User();
        user.setCardNumber(name);
        if (rememberPassWord) {
            user.setPwd(pwd);
        }

        doRequest(action_login, hashMap, new BaseAsynCallBack(new DefaultCallBack() {
            @Override
            public void onStart() {
                callBack.onStart();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                callBack.onError(e, stateCode);
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                callBack.onEXECisFalse(errroMsg);
            }

            //登陆成功后进行读卡操作,读卡成功才认为是真正登陆成功！
            @Override
            public void onEXECSuccess(String result) {
                RSAEncrypt rsaEncrypt = new RSAEncrypt();
                try {
                    final CardToken cardToken = new CardToken();
                    HashMap<String, String> attMap = DATAxmlHelper
                            .getAllAttribute(result);
                    rsaEncrypt.loadPublicKey(WActivityManager.getInstance().topActivity().getAssets().open("rsa_public_key.pem"));
                    byte[] dataArr = rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(), attMap.get("令牌").getBytes());
                    String token = Base64Helper.encode(dataArr);
                    cardToken.setToken(token);
                    cardToken.setMark(attMap.get("卡标识"));
                    cardToken.setCheckCode(attMap.get("校验码"));
                    //读卡
                    readCard(cardToken, new ServiceCallBack() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onError(Exception e, int stateCode) {
                            callBack.onError(e, stateCode);
                        }

                        @Override
                        public void onEXECisFalse(String errroMsg) {
                            callBack.onEXECisFalse(errroMsg);
                        }

                        @Override
                        public void onEXECSuccess(Object resultObj) {
                            //设置全局参数
                            WnbApplication.getInstance().setCardToken(cardToken);
                            WnbApplication.getInstance().setCard((Card) resultObj);
                            callBack.onEXECSuccess(null);
                            //保存用户到本地
                            try {
                                UserDAO.save(user, UserDAO.TAG.MEMBERTAG, WActivityManager.getInstance().topActivity());
                            } catch (Exception e) {
                            }
                        }

                        @Override
                        public void onEXECSuccess(Object resultObj, int pageSize, int dataSize) {

                        }
                    });
                } catch (Exception e) {
                    callBack.onError(e, 0);
                }
            }

            @Override
            public void onComplete(String result) {
                super.onComplete(result);
                //ViewInject.toast(result);
            }
        }));
    }

    /**
     * 修改卡密码,使用密码改密码
     *
     * @param cardToken  令牌
     * @param oldPwd    老密码
     * @param newPWd    新密码
     * @param callBack
     */
    public void changePwd(CardToken cardToken,
                          String oldPwd, String newPWd, BaseAsynCallBack callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("令牌", cardToken.getToken());
        hashMap.put("校验码", cardToken.getCheckCode());
        hashMap.put("卡标识", cardToken.getMark());
        hashMap.put("原密码", oldPwd);
        hashMap.put("新密码", newPWd);
        doRequest(action_changePWd, hashMap, callBack);
    }

    /**
     * 修改卡密码,通过手机验证码,会员忘记密码,可用此功能
     *
     * @param cardNumber    卡号
     * @param pwd           密码
     * @param checkCode     验证码
     * @param checkCodeMark 验证码标识
     * @param callBack
     */
    public void changePwd4ShortMessage(String cardNumber, String pwd, String checkCode, String checkCodeMark, BaseCallBack callBack) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("卡号", cardNumber);
        hashMap.put("密码", pwd);
        hashMap.put("验证码", checkCode);
        hashMap.put("验证码标识", checkCodeMark);
        doRequest(action_changePWd2, hashMap, new BaseAsynCallBack(callBack));
    }


    /**
     * 读卡
     *
     * @param cardToken
     * @param callBack
     */
    public void readCard(CardToken cardToken, final ServiceCallBack callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("令牌", cardToken.getToken());
        hashMap.put("校验码", cardToken.getCheckCode());
        hashMap.put("卡标识", cardToken.getMark());
        doRequest(action_readCard, hashMap, new BaseAsynCallBack(new BaseCallBack() {
            @Override
            public void onStart() {
                callBack.onStart();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                callBack.onError(e, stateCode);
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                callBack.onEXECisFalse(errroMsg);
            }

            @Override
            public void onEXECSuccess(String result) {
                try {
                    ArrayList<Card> cardList = (ArrayList<Card>) XmlUtils.readXML2BeanList(result, Card.class, "DATA",
                            Mapping.cardMapping);
                    Card card = cardList.get(0);
                    callBack.onEXECSuccess(card);
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.onError(e, 0);
                }
            }

            @Override
            public void onComplete(String result) {
                LogUtils.e(result);
            }
        }));
    }

    /**
     * 卡间转账
     *
     * @param cardToken
     * @param name              接受人
     * @param receiveCardNumber 接受卡号
     * @param money             转入金额
     * @param score             转入积分
     * @param pwd
     * @param callBack
     */
    public void ccTransfer(CardToken cardToken, String name, String receiveCardNumber, String money, String score, String pwd, BaseCallBack callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("令牌", cardToken.getToken());
        hashMap.put("校验码", cardToken.getCheckCode());
        hashMap.put("卡标识", cardToken.getMark());

        hashMap.put("收款人", name);
        hashMap.put("收款卡号", receiveCardNumber);
        hashMap.put("转账储值", money);
        hashMap.put("转账积分", score);
        hashMap.put("验证密码", pwd);
        doRequest(action_ccTransfer, hashMap, new BaseAsynCallBack(callBack));
    }

    /**
     * 获取卡收益
     *
     * @param cardToken
     * @param callBack
     */
    public void obtainCardProfit(CardToken cardToken, final ServiceCallBack callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("令牌", cardToken.getToken());
        hashMap.put("校验码", cardToken.getCheckCode());
        hashMap.put("卡标识", cardToken.getMark());
        doRequest(action_obtainCardProfit, hashMap, new BaseAsynCallBack(new BaseCallBack() {
            @Override
            public void onStart() {
                callBack.onStart();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                callBack.onError(e, stateCode);
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                callBack.onEXECisFalse(errroMsg);
            }

            @Override
            public void onEXECSuccess(String result) {
                //callBack.onEXECSuccess(result);
                try {
                    ArrayList<Profit> profitArrayList = (ArrayList<Profit>) XmlUtils.readXML2BeanList(result, Profit.class, "DATA", Mapping.profitMapping);
                    callBack.onEXECSuccess(profitArrayList.get(0));
                } catch (Exception e) {
                    callBack.onError(e, 404);
                }
            }

            @Override
            public void onComplete(String result) {
            }
        }));
    }

    /**
     * 获取昨日收益
     *
     * @param cardToken
     * @param pageIndex
     * @param pageSize
     */
    public void obtainProfitDetails(CardToken cardToken, int pageIndex, int pageSize, final ServiceCallBack callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("PageIndex", pageIndex + "");
        hashMap.put("PageSize", pageSize + "");
        hashMap.put("令牌", cardToken.getToken());
        hashMap.put("校验码", cardToken.getCheckCode());
        hashMap.put("卡标识", cardToken.getMark());
        doRequest(action_obtainProfitDetails, hashMap, new BaseAsynCallBack(new BaseCallBack() {
            @Override
            public void onStart() {
                callBack.onStart();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                callBack.onError(e, stateCode);
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                callBack.onEXECisFalse(errroMsg);
            }

            @Override
            public void onEXECSuccess(String result) {
                try {
                    callBack.onEXECSuccess(XmlUtils.readXML2BeanList(result, ProfitRecord.class, "记录", Mapping.profitRecordMapping));
                } catch (Exception e) {
                    callBack.onError(e, -1);
                }
            }

            @Override
            public void onComplete(String result) {

            }
        }));
    }

    /**
     * 获取储值记录
     *
     * @param cardToken
     * @param pageIndex
     * @param pageCount
     * @param callBack
     */
    public void obtainStoredValueRecord(CardToken cardToken, int pageIndex, int pageCount,
                                        final ServiceCallBack<ArrayList<StoredValueRecord>> callBack) {

        if (!cardToken.verification()) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("PageIndex", pageIndex + "");
        hashMap.put("PageSize", pageCount + "");
        hashMap.put("令牌", cardToken.getToken());
        hashMap.put("校验码", cardToken.getCheckCode());
        hashMap.put("卡标识", cardToken.getMark());
        //hashMap.put("备注",reMark);
        doRequest(action_obtainStoredValueRecord, hashMap,

                new BaseAsynCallBack(new BaseCallBack() {
                    @Override
                    public void onStart() {
                        callBack.onStart();
                    }

                    @Override
                    public void onError(Exception e, int stateCode) {
                        callBack.onError(e, stateCode);
                    }

                    @Override
                    public void onEXECisFalse(String errroMsg) {
                        callBack.onEXECisFalse(errroMsg);
                    }

                    @Override
                    public void onEXECSuccess(String result) {
                        try {
                            HashMap allAttr = DATAxmlHelper.getAllAttribute(result);
                            int indexCount = 0;
                            int maxDataSize = 0;
                            if ((String) allAttr.get("PageCount") != null) {
                                indexCount = Integer.parseInt((String) allAttr.get("PageCount"));
                            }
                            if ((String) allAttr.get("RecordCount") != null) {
                                maxDataSize = Integer.parseInt((String) allAttr.get("RecordCount"));
                            }

                            ArrayList<StoredValueRecord> storedValueRecordArrayList = (ArrayList<StoredValueRecord>) XmlUtils.readXML2BeanList(result, StoredValueRecord.class, "记录", Mapping.storedValueRecordMapping);
                            callBack.onEXECSuccess(storedValueRecordArrayList);
                            callBack.onEXECSuccess(storedValueRecordArrayList,indexCount,maxDataSize);
                        } catch (Exception e) {
                            callBack.onError(e, -1);
                        }
                    }

                    @Override
                    public void onComplete(String result) {

                    }
                }));
    }

    /**
     * 提现
     *
     * @param cardToken
     * @param money
     * @param pwd
     * @param storeMark
     * @param scrore
     */
    public void mentionNow(CardToken cardToken, String money, String pwd, String storeMark, String scrore, final BaseCallBack callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("令牌", cardToken.getToken());
        hashMap.put("校验码", cardToken.getCheckCode());
        hashMap.put("卡标识", cardToken.getMark());
        hashMap.put("密钥", pwd);
        if (!StringUtils.isEmpty(storeMark)) {
            hashMap.put("店面", storeMark);
        }
        if (!StringUtils.isEmpty(money)) {
            hashMap.put("提现储值", money);
        }
        if (!StringUtils.isEmpty(scrore)) {
            hashMap.put("提现积分", scrore);
        } else {
            hashMap.put("提现积分", 0 + "");
        }
        doRequest(action_mentionNow, hashMap, new BaseAsynCallBack(callBack));
    }

}