package com.chanxa.wnb.service;

import android.view.View;

import com.baidu.mapapi.model.LatLng;
import com.chanxa.wnb.bean.CardToken;
import com.chanxa.wnb.bean.FenLeiStore;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.bean.Industry;
import com.chanxa.wnb.bean.StaffStoreGoodsLV1;
import com.chanxa.wnb.bean.Store;
import com.chanxa.wnb.bean.onLineShop.Distribution;
import com.chanxa.wnb.bean.onLineShop.Order;
import com.chanxa.wnb.service.base.BaseAsynCallBack;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.service.base.BaseService;
import com.chanxa.wnb.xml.DATAxmlHelper;
import com.chanxa.wnb.xml.DistributionXmlParser;
import com.chanxa.wnb.xml.GoodsTypeXmlParser;
import com.chanxa.wnb.xml.OrderXmlParser;
import com.chanxa.wnb.xml.XmlUtils;
import com.chanxa.wnb.xml.mapping.Mapping;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 店铺有关业务
 *
 * @author CHANXA
 */
public class StoreService extends BaseService {
    private final String action_obtianStore = "获取店面";
    private final String action_obtianNearStore = "获取附近店面";
    private final String action_obtianGoods = "获取商品";
    private final String action_obtianGoodsDetails = "获取商品详情";
    private final String action_obtianLogisticsMoney = "获取物流费用";
    private final String action_obtianStoreGoodsType = "获取店面商品类别";
    private final String action_obtianIndustry = "获取行业";
    private final String action_obtianGoodsOrder = "获取商品订单";
    private final String action_addNewGoodsOrder = "新增商品订单";
    private final String action_addNewGoodsPanicBuyOrder = "新增抢购商品订单";
    private final String action_obtainStoreDetails = "获取店面详情";

    /**
     * 获取附近店面
     *
     * @param cardToken 令牌
     * @param pageIndex 页码数
     * @param pageSize  页的内容数量
     * @param latLng    自己的经纬度
     * @param callBack  onEXECSuccess返回ArrayList<Store> storeList
     */
    public void obtianNearStore(CardToken cardToken, int pageIndex, int pageSize,
                                LatLng latLng, final ServiceCallBack<ArrayList<Store>> callBack) {
        if (!cardToken.verification()) {
            return;
        }
        if (pageSize <= 0 || pageIndex < 0 || callBack == null) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("PageIndex", pageIndex + "");
        hashMap.put("PageSize", pageSize + "");

        hashMap.put("令牌", cardToken.getToken());
        hashMap.put("校验码", cardToken.getCheckCode());
        hashMap.put("卡标识", cardToken.getMark());
        hashMap.put("经度", latLng.longitude + "");
        hashMap.put("纬度", latLng.latitude + "");

        doRequest(action_obtianNearStore, hashMap, new BaseAsynCallBack(new BaseCallBack() {
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
                    int pageSize = 0;
                    int dataSize = 0;
                    HashMap allAttr = DATAxmlHelper.getAllAttribute(result);
                    if (allAttr.get("PageCount") != null) {
                        pageSize = Integer.parseInt((String) allAttr.get("PageCount"));
                    }
                    if (allAttr.get("RecordCount") != null) {
                        dataSize = Integer.parseInt((String) allAttr.get("RecordCount"));
                    }

                    ArrayList<Store> storeList = (ArrayList<Store>) XmlUtils
                            .readXML2BeanList(result, Store.class, "店面",
                                    Mapping.storeMapping);
                    callBack.onEXECSuccess(storeList);
                    callBack.onEXECSuccess(storeList,pageSize,dataSize);
                } catch (Exception e) {
                    callBack.onError(e, 0);
                }
            }

            @Override
            public void onComplete(String result) {
                LogUtils.d(result);
            }
        }));
    }


    /**
     * 获取店面
     *
     * @param pageIndex
     * @param pageSize
     * @param industryMark 行业标识码
     * @param provinceMark 省标识码
     * @param cityMark     城市标识码
     * @param districtMark 地区标识码
     * @param callBack
     */
    public void obtianStore(int pageIndex, int pageSize, String storeName, String industryMark, String provinceMark, String cityMark, String districtMark, final ServiceCallBack<ArrayList<Store>> callBack) {

        if (pageSize <= 0 || pageIndex <= 0 || callBack == null) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("PageIndex", pageIndex + "");
        hashMap.put("PageSize", pageSize + "");
        if (!StringUtils.isEmpty(industryMark)) {
            hashMap.put("行业", industryMark + "");
        }
        if (!StringUtils.isEmpty(provinceMark)) {
            hashMap.put("省", provinceMark + "");
        }
        if (!StringUtils.isEmpty(cityMark)) {
            hashMap.put("市", cityMark + "");
        }
        if (!StringUtils.isEmpty(districtMark)) {
            hashMap.put("区县", districtMark + "");
        }
        if (!StringUtils.isEmpty(storeName)) {
            hashMap.put("店面名称", storeName);
        }

        doRequest(action_obtianStore, hashMap, new BaseAsynCallBack(new BaseCallBack() {
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
                    ArrayList<Store> storeList = (ArrayList<Store>) XmlUtils
                            .readXML2BeanList(result, Store.class, "店面",
                                    Mapping.storeMapping);
                    callBack.onEXECSuccess(storeList);
                } catch (Exception e) {
                    callBack.onError(e, 0);
                }
            }

            @Override
            public void onComplete(String result) {
                LogUtils.d(result);
            }
        }));
    }


    /**
     * 获取商品
     *
     * @param cardToken
     * @param pageIndex
     * @param pageSize
     * @param Category  商品类别
     * @param goodsType 商品类型
     * @param storeMark
     * @param callBack
     */
    public void obtainGoods(CardToken cardToken, int pageIndex, int pageSize, String goodsMarkStr,String goodsName, String goodsType, String Category, String storeMark, final ServiceCallBack<ArrayList<Goods>> callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("PageIndex", pageIndex + "");
        hashMap.put("PageSize", pageSize + "");
        if (!StringUtils.isEmpty(storeMark)) {
            hashMap.put("店面", storeMark);
        }
        hashMap.put("商品类型", goodsType);
        if (!StringUtils.isEmpty(Category)) {
            hashMap.put("商品类别", Category);
        }
        if (!StringUtils.isEmpty(goodsName)) {
            hashMap.put("商品名称", goodsName);
        }
        if (!StringUtils.isEmpty(goodsMarkStr)) {
            hashMap.put("商品标识", goodsMarkStr);
        }
        hashMap.put("令牌", cardToken.getToken());
        hashMap.put("校验码", cardToken.getCheckCode());
        hashMap.put("卡标识", cardToken.getMark());
        doRequest(action_obtianGoods, hashMap, new BaseAsynCallBack(new BaseCallBack() {
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
                    int pageSize = 0;
                    int dataSize = 0;
                    HashMap allAttr = DATAxmlHelper.getAllAttribute(result);
                    if ((String) allAttr.get("PageCount") != null) {
                        pageSize = Integer.parseInt((String) allAttr.get("PageCount"));
                    }
                    if ((String) allAttr.get("RecordCount") != null) {
                        dataSize = Integer.parseInt((String) allAttr.get("RecordCount"));
                    }

                    ArrayList<Goods> goodsArrayList = (ArrayList<Goods>) XmlUtils.readXML2BeanList(result, Goods.class, "商品", Mapping.goodsMapping);
                    callBack.onEXECSuccess(goodsArrayList, pageSize, dataSize);
                    callBack.onEXECSuccess(goodsArrayList);
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.onError(e, -1);
                }
            }

            @Override
            public void onComplete(String result) {

            }
        }));
    }

    /**
     * 获取所有行业
     *
     * @param callBack 成功返回 ArrayList<Industry> industryArrayList
     */
    public void obtainIndustry(final ServiceCallBack<ArrayList<Industry>> callBack) {
        doRequest(action_obtianIndustry, new HashMap<String, String>(), new BaseAsynCallBack(new BaseCallBack() {
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
                    ArrayList<Industry> industryArrayList = (ArrayList<Industry>) XmlUtils.readXML2BeanList(result, Industry.class, "行业", Mapping.industryMapping);
                    callBack.onEXECSuccess(industryArrayList);
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
     * 获取商品详情
     *
     * @param cardToken
     * @param goodeMark
     * @param callBack
     */
    public void obtianGoodsDetails(CardToken cardToken, String goodeMark, final ServiceCallBack<Goods> callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("令牌", cardToken.getToken());
        hashMap.put("校验码", cardToken.getCheckCode());
        hashMap.put("卡标识", cardToken.getMark());
        hashMap.put("标识", goodeMark);

        doRequest(action_obtianGoodsDetails, hashMap, new BaseAsynCallBack(new BaseCallBack() {
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
                    ArrayList<Goods> goodsArrayList = (ArrayList<Goods>) XmlUtils.readXML2BeanList(result, Goods.class, "DATA", Mapping.goodsMapping);
                    callBack.onEXECSuccess(goodsArrayList.get(0));
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
     * 获取店铺商品分类
     *
     * @param cardToken
     * @param callBack
     */
    // result obj = ArrayList<FenLeiStore>
    public void obtainStoreGoodsType(CardToken cardToken, final ServiceCallBack<ArrayList<FenLeiStore>> callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("令牌", cardToken.getToken());
        hashMap.put("校验码", cardToken.getCheckCode());
        hashMap.put("卡标识", cardToken.getMark());

        doRequest(action_obtianStoreGoodsType, hashMap, new BaseAsynCallBack(new BaseCallBack() {
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
                    ArrayList<FenLeiStore> arrayList = new GoodsTypeXmlParser().parserStaffStoreGoodsLV1ArrayList(result);
                    callBack.onEXECSuccess(arrayList);
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
     * 获取商品订单
     *
     * @param cardToken
     * @param pageIndex
     * @param pageSize
     * @param callBack  resultObj:ArrayList<StaffOrderDetail> orderArrayList
     */
    public void obtianGoodsOrder(CardToken cardToken, int pageIndex, int pageSize, final ServiceCallBack<ArrayList<Order>> callBack) {
        if (!cardToken.verification()) {
            return;
        }
        if (pageSize <= 0 || pageIndex < 0 || callBack == null) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("PageIndex", pageIndex + "");
        hashMap.put("PageSize", pageSize + "");
        hashMap.put("令牌", cardToken.getToken());
        hashMap.put("校验码", cardToken.getCheckCode());
        hashMap.put("卡标识", cardToken.getMark());

        doRequest(action_obtianGoodsOrder, hashMap, new BaseAsynCallBack(new BaseCallBack() {
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
                    int pageSize = 0;
                    int dataSize = 0;
                    HashMap allAttr = DATAxmlHelper.getAllAttribute(result);
                    if ((String) allAttr.get("PageCount") != null) {
                        pageSize = Integer.parseInt((String) allAttr.get("PageCount"));
                    }
                    if ((String) allAttr.get("RecordCount") != null) {
                        dataSize = Integer.parseInt((String) allAttr.get("RecordCount"));
                    }
                    //ArrayList<StaffOrderDetail> orderArrayList = (ArrayList<StaffOrderDetail>) XmlUtils.readXML2BeanList(result, StaffOrderDetail.class, "单据", Mapping.goodsMapping);
                    ArrayList<Order> orderArrayList = new OrderXmlParser().parserOrderList(result);
                    callBack.onEXECSuccess(orderArrayList, pageSize, dataSize);
                    callBack.onEXECSuccess(orderArrayList);
                } catch (Exception e) {
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
     * 获取物流费用
     *
     * @param cardToken
     * @param storeMark
     * @param callBack
     */
    public void obtianLogisticsMoney(CardToken cardToken, String storeMark, final ServiceCallBack<ArrayList<Distribution>> callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("令牌", cardToken.getToken());
        hashMap.put("校验码", cardToken.getCheckCode());
        hashMap.put("卡标识", cardToken.getMark());
        hashMap.put("店面", storeMark);
        doRequest(action_obtianLogisticsMoney, hashMap, new BaseAsynCallBack(new BaseCallBack() {
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
                    ArrayList<Distribution> distributionArrayList = new DistributionXmlParser().parserDostributionList(result);
                    callBack.onEXECSuccess(distributionArrayList);
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                }
            }

            @Override
            public void onComplete(String result) {
                LogUtils.e(result);
            }
        }));

    }

    /**
     * 提交订单
     *
     * @param cardToken
     * @param storeMark
     * @param orderXml
     * @param callBack
     */
    public void addNewGoodsOrder(CardToken cardToken, String storeMark, String orderXml, BaseCallBack callBack) {
        if (!cardToken.verification()) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("令牌", cardToken.getToken());
        hashMap.put("校验码", cardToken.getCheckCode());
        hashMap.put("卡标识", cardToken.getMark());
        hashMap.put("店面", storeMark);
        hashMap.put("商品订单", orderXml);
        doRequest(action_addNewGoodsOrder, hashMap, new BaseAsynCallBack(callBack));
    }

    /**
     * 新增商品抢购订单
     *
     * @param cardToken
     * @param callBack
     */
    public void addNewGoodsPanicBuyOrder(CardToken cardToken, Goods goods, BaseCallBack callBack) {
        if (!cardToken.verification()) {
            return;
        }
        if (goods == null) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("令牌", cardToken.getToken());
        hashMap.put("校验码", cardToken.getCheckCode());
        hashMap.put("卡标识", cardToken.getMark());
        hashMap.put("商品", goods.getMark());
        doRequest(action_addNewGoodsPanicBuyOrder, hashMap, new BaseAsynCallBack(callBack));
    }


    /**
     * 获取店面详情
     * @param cardToken
     * @param storeMark
     * @param callBack
     */
    public void obtainStoreDetails(CardToken cardToken, String storeMark, BaseCallBack callBack) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("令牌", cardToken.getToken());
        hashMap.put("校验码", cardToken.getCheckCode());
        hashMap.put("卡标识", cardToken.getMark());
        hashMap.put("标识", storeMark);
        doRequest(action_obtainStoreDetails, hashMap, new BaseAsynCallBack(callBack));
    }
}
