package com.chanxa.wnb.service;

import com.chanxa.wnb.bean.CardToken;
import com.chanxa.wnb.bean.MakeRecord;
import com.chanxa.wnb.bean.Store;
import com.chanxa.wnb.service.base.BaseAsynCallBack;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.service.base.BaseService;
import com.chanxa.wnb.xml.DATAxmlHelper;
import com.chanxa.wnb.xml.XmlUtils;
import com.chanxa.wnb.xml.mapping.Mapping;
import com.wtm.library.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 预约有关业务
 * Created by CHANXA on 2014/12/22.
 */
public class MakeService extends BaseService {

    private final String action_obtainOnLineMake = "获取在线预约";
    private final String action_addOnLineMake = "新增在线预约";

    /**
     * 获取在线预约
     * @param cardToken
     * @param pageIndex
     * @param pageSize
     * @param callBack  返回预约列表
     */
    public void obtainOnLineMake(CardToken cardToken, int pageIndex, int pageSize,final ServiceCallBack callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap map = new HashMap();
        map.put("令牌", cardToken.getToken());
        map.put("校验码", cardToken.getCheckCode());
        map.put("卡标识", cardToken.getMark());
        map.put("PageIndex", pageIndex + "");
        map.put("PageSize", pageSize + "");
        doRequest(action_obtainOnLineMake, map, new BaseAsynCallBack(new BaseCallBack() {
            @Override
            public void onStart() {
                callBack.onStart();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                LogUtils.e(e+"");
                callBack.onError(e,stateCode);
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                LogUtils.e(errroMsg);
                callBack.onEXECisFalse(errroMsg);
            }

            @Override
            public void onEXECSuccess(String result) {
                try {
                    int pageSize =0;
                    int dataSize =0;
                    HashMap allAttr = DATAxmlHelper.getAllAttribute(result);
                    if ((String) allAttr.get("PageCount")!=null) {
                        pageSize = Integer.parseInt((String) allAttr.get("PageCount"));
                    }
                    if ((String) allAttr.get("RecordCount")!=null) {
                        dataSize = Integer.parseInt((String) allAttr.get("RecordCount"));
                    }
                    ArrayList<MakeRecord> makeRecordArrayList = (ArrayList<MakeRecord>) XmlUtils.readXML2BeanList(result, MakeRecord.class, "记录", Mapping.makeRecordMapping);
                    callBack.onEXECSuccess(makeRecordArrayList);
                    callBack.onEXECSuccess(makeRecordArrayList,pageSize,dataSize);
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.onError(e,-1);
                }
            }

            @Override
            public void onComplete(String result) {
                LogUtils.e(result);
            }
        }));
    }

    /**
     * 新增在线预约
     * @param cardToken
     * @param store  店面
     * @param goodsId 商品
     * @param contact 联系人
     * @param phone  联系电话
     * @param date  日期
     * @param time  时间
     * @param personNumber  到场人数
     */
    public void addOnLineMake(CardToken cardToken, Store store, int goodsId, String contact, String phone, String date, String time, String personNumber,String reMark,BaseCallBack callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap map = new HashMap();
        map.put("令牌", cardToken.getToken());
        map.put("校验码", cardToken.getCheckCode());
        map.put("卡标识", cardToken.getMark());
        map.put("店面", store.getMark());
        map.put("商品", goodsId+"");
        map.put("联系人", contact);
        map.put("联系电话", phone);
        map.put("预约日期", date);
        map.put("预约时间", time);
        map.put("到场人数", personNumber);
        map.put("备注",reMark);
        doRequest(action_addOnLineMake, map, new BaseAsynCallBack(callBack));
    }
}
