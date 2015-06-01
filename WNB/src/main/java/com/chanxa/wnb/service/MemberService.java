package com.chanxa.wnb.service;

import android.util.Xml;

import com.chanxa.wnb.bean.Card;
import com.chanxa.wnb.bean.CardToken;
import com.chanxa.wnb.bean.Member;
import com.chanxa.wnb.bean.MemberExpansion;
import com.chanxa.wnb.service.base.BaseAsynCallBack;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.service.base.BaseService;
import com.chanxa.wnb.xml.XmlUtils;
import com.chanxa.wnb.xml.mapping.Mapping;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 用户有关业务
 * Created by CHANXA on 2014/12/18.
 */
public class MemberService extends BaseService {

    private String actionShort = "获取新增会员验证码";
    private String actionChangePwdShort = "获取修改密码验证码";
    private String actionReg = "新增会员";
    private String actionRequestMemberData = "获取会员";
    private String actionMemberDataUpdata = "编辑会员";
    private String actionSendMessage = "新增留言";
    private String obtainExpansionData = "获取会员扩展属性";
    private String upDataCustomExpansionData = "更新自定义扩展";
    private String obtainCustomExpansionData = "获取自定义扩展";

    /**
     * 获取注册的短信验证
     *
     * @param storeMark
     * @param phone
     * @param callback
     */
    public void requestRegShortMessage(String storeMark, String phone, BaseCallBack callback) {

        HashMap map = new HashMap();
        if (!StringUtils.isEmpty(storeMark)) {
            map.put("店面", storeMark);
        }
        map.put("手机", phone);
        doRequest(actionShort, map, new BaseAsynCallBack(callback));
    }

    /**
     * 获取修改密码的短信验证
     *
     * @param cardNumber
     * @param callback
     */
    public void requestChangePwdShortMessage(String cardNumber, BaseCallBack callback) {
        HashMap map = new HashMap();
        map.put("卡号", cardNumber);
        doRequest(actionChangePwdShort, map, new BaseAsynCallBack(callback));
    }


    //用户注册

    /**
     * @param phone           电话
     * @param password        密码
     * @param phoneCheckCode  手机验证码
     * @param checkCodeMark   验证码标识
     * @param name            姓名
     * @param appellation     称谓
     * @param recommendNumber 推荐人卡号
     */
    public void regMember(String phone, String password, String phoneCheckCode, String checkCodeMark, String name, String appellation, String recommendNumber, BaseCallBack callback) {
        HashMap map = new HashMap();
        map.put("手机", phone);
        map.put("验证码", phoneCheckCode);
        map.put("密钥", password);
        map.put("验证码标识", checkCodeMark);
        map.put("姓名", name);
        map.put("称谓", appellation);
        if (!StringUtils.isEmpty(recommendNumber)) {
            map.put("推荐会员卡号", recommendNumber);
        }
        doRequest(actionReg, map, new BaseAsynCallBack(callback));
    }

    /**
     * 请求用户数据
     *
     * @param cardToken
     * @param memberMark
     * @param callBack
     */
    public void requestMemberData(CardToken cardToken, String memberMark, final ServiceCallBack callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap map = new HashMap();
        map.put("令牌", cardToken.getToken());
        map.put("校验码", cardToken.getCheckCode());
        map.put("卡标识", cardToken.getMark());
        map.put("标识", memberMark);
        doRequest(actionRequestMemberData, map, new BaseAsynCallBack(new BaseCallBack() {
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
                LogUtils.e(result);
                callBack.onEXECSuccess(result);
               /* try {
                    ArrayList<Member> memberArrayList = (ArrayList<Member>) XmlUtils.readXML2BeanList(result, Member.class, "DATA", Mapping.memberMapping);
                    Member member = memberArrayList.get(0);
                    callBack.onEXECSuccess(member);
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.onError(e, 0);
                }*/
            }

            @Override
            public void onComplete(String result) {
                LogUtils.e(result);
            }
        }));
    }

    /**
     * 更新用户数据
     *
     * @param cardToken
     * @param memberMark
     * @param member
     * @param callBack
     */
    public void updataMember(CardToken cardToken, String memberMark, Member member, BaseCallBack callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap map = new HashMap();
        map.put("令牌", cardToken.getToken());
        map.put("校验码", cardToken.getCheckCode());
        map.put("卡标识", cardToken.getMark());
        map.put("标识", memberMark);
        map.put("姓名", member.getName());
        map.put("称谓", member.getAppellation());
        map.put("手机", member.getPhone());
        map.put("联系地址", member.getAddress());
        map.put("照片路径", member.getPhotoPath());
        map.put("备注", member.getRemarks());

        String exData = "<DATA>";
        try {
            ArrayList<MemberExpansion> memberExpansionArrayList = member.getMemberExpansionArrayList();
            for (MemberExpansion memberExpansion : memberExpansionArrayList) {
                memberExpansion.seteETV_eETP_Id(memberExpansion.geteETP_Id());
                memberExpansion.seteETP_Id(null);
                exData+= XmlUtils.writeBean2Xml("ITEM", memberExpansion);
            }
        } catch (Exception e) {
            callBack.onError(e, -1);
        }
        exData += "</DATA>";
        map.put("扩展属性",exData);
        //map.put("扩展属性","&lt;DATA&gt;&lt;ITEM eETV_eETP_Id=&quot;1926&quot; eETP_FieldName=&quot;银行卡号&quot; eETP_sValueType_Id=&quot;1&quot; eETV_Value=&quot;000000&quot; eETP_sValueType_Id=&quot;1&quot; eETP_sInputControl_Id=&quot;1&quot; eETP_AllowedNull=&quot;1&quot; &gt;&lt;/ITEM&gt;&lt;/DATA&gt;");
        //非必填
        if (!StringUtils.isEmpty(member.getBirhday())) {
            map.put("出生日期", member.getBirhday());
            map.put("生日类型", "1");
        }
        if (!StringUtils.isEmpty(member.getPersonID())) {
            map.put("身份证号码", member.getPersonID());
        }
        doRequest(actionMemberDataUpdata, map, new BaseAsynCallBack(callBack));
    }

    /**
     * 新增留言
     *
     * @param cardToken
     * @param content
     * @param callBack
     */
    public void sendMessage(CardToken cardToken, String content, BaseCallBack callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap map = new HashMap();
        map.put("令牌", cardToken.getToken());
        map.put("校验码", cardToken.getCheckCode());
        map.put("卡标识", cardToken.getMark());
        map.put("标题", "#APP留言#");
        map.put("内容", content);
        doRequest(actionSendMessage, map, new BaseAsynCallBack(callBack));
    }

    /**
     * 获取会员扩张属性
     *
     * @param cardToken
     * @param cardMark
     * @param callBack
     */
    public void obtainExpansionData(CardToken cardToken, String cardMark, final ServiceCallBack<ArrayList<MemberExpansion>> callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap map = new HashMap();
        map.put("令牌", cardToken.getToken());
        map.put("校验码", cardToken.getCheckCode());
        map.put("卡标识", cardToken.getMark());
        map.put("标识", cardMark);
        doRequest(obtainExpansionData, map, new BaseAsynCallBack(new BaseCallBack() {
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
                    ArrayList<MemberExpansion> memberExpansionArrayList = (ArrayList<MemberExpansion>) XmlUtils.readXML2BeanList(result, MemberExpansion.class, "扩展属性", null);
                    callBack.onEXECSuccess(memberExpansionArrayList);
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
     * 更新自定义拓展属性
     * @param cardToken
     * @param key
     * @param value
     * @param callBack
     */
    public void upDataCustomExpansionData(CardToken cardToken, String key, String value, BaseCallBack callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap map = new HashMap();
        map.put("令牌", cardToken.getToken());
        map.put("校验码", cardToken.getCheckCode());
        map.put("卡标识", cardToken.getMark());
        map.put("Key", key);
        map.put("Value",value);
        doRequest(upDataCustomExpansionData, map, new BaseAsynCallBack(callBack));
    }

    /**
     * 获取自定义拓展属性
     * @param cardToken
     * @param key
     * @param callBack
     */
    public void obtainCustomExpansionData(CardToken cardToken, String key,BaseCallBack callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap map = new HashMap();
        map.put("令牌", cardToken.getToken());
        map.put("校验码", cardToken.getCheckCode());
        map.put("卡标识", cardToken.getMark());
        map.put("Key", key);
        doRequest(obtainCustomExpansionData, map, new BaseAsynCallBack(callBack));
    }

}
