package com.chanxa.wnb.service;

import com.chanxa.wnb.bean.CardToken;
import com.chanxa.wnb.bean.Commission;
import com.chanxa.wnb.bean.ContactDetails;
import com.chanxa.wnb.bean.Drawing;
import com.chanxa.wnb.bean.FenLeiStore;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.bean.Staff;
import com.chanxa.wnb.bean.Store;
import com.chanxa.wnb.bean.StoreDetails;
import com.chanxa.wnb.bean.onLineShop.Order;
import com.chanxa.wnb.bean.onLineShop.OrderDetails;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.base.BaseAsynCallBack;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.service.base.BaseService;
import com.chanxa.wnb.service.base.StaffServiceCallBack;
import com.chanxa.wnb.xml.DATAxmlHelper;
import com.chanxa.wnb.xml.GoodsTypeXmlParser;
import com.chanxa.wnb.xml.XmlUtils;
import com.chanxa.wnb.xml.mapping.Mapping;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 员工有关业务
 * Created by CHANXA on 2014/12/26.
 */
public class StaffService extends BaseService {
    private final String action_login = "账号登录";
    private final String action_changeData = "修改资料";
    private final String action_changePwd = "修改密码";
    private final String action_addSfzImage = "新增身份证审核申请单据";
    private final String action_upLoadImage = "上传图片";
    private final String action_mentionNow = "新增申请提款明细";
    private final String action_obtainStoreGoodsTypes = "获取店面商品类别";
    private final String action_AddGoods = "新增或者编辑商品";
    private final String action_obtainDrawingDetails = "获取提款明细";
    private final String action_obtainCommissionDetails = "获取提成明细";
    private final String action_obtainGoods = "获取商品";
    private final String action_obtainGoodsDetails = "获取商品BeforeEdit";
    private final String action_obtianStoreGoodsType = "获取店面商品类别";
    private final String action_obtainGoodsOrder = "获取商品订单";
    private final String action_editStoreDetails ="编辑店面";
    private final String action_obtainStoreDetails = "获取店面详情";
    public StaffService() {
        apiPath = "http://bi.bi-uc.com/External-Employee.api";
    }
    /**
     * 获取店面详情
     *
     *
     */
    public void obtainStoreDetails(BaseCallBack callBack) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("唯一码", WnbApplication.getInstance().getStaff().getOnlyCode());
        hashMap.put("凭据", WnbApplication.getInstance().getStaff().getProof());
        hashMap.put("员工", WnbApplication.getInstance().getStaff().getMark());
        doRequest(action_obtainStoreDetails, hashMap, new BaseAsynCallBack(callBack));
    }
    /**
     * 编辑店面
     * @param contactDetails
     * @param callBack
     */
    public void editStoreDetails(ContactDetails contactDetails,String logo,StoreDetails storeDetails,BaseCallBack callBack){
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("唯一码", WnbApplication.getInstance().getStaff().getOnlyCode());
        hashMap.put("凭据",WnbApplication.getInstance().getStaff().getProof());
        hashMap.put("员工", WnbApplication.getInstance().getStaff().getMark());
        hashMap.put("名称", storeDetails.getName());
        hashMap.put("联系人", contactDetails.getContactPerson()+"");
        hashMap.put("电话", contactDetails.getContactPhone()+"");
        hashMap.put("图标路径", logo);
        hashMap.put("省", 6+"");
        hashMap.put("市", 37+"");
        hashMap.put("区县",473+"");
        hashMap.put("详细地址",storeDetails.getDetailsAddress());
        hashMap.put("经度",storeDetails.getLat());
        hashMap.put("纬度",storeDetails.getLng());
        hashMap.put("是否线上订购",storeDetails.isOnlineBuy());
        hashMap.put("是否线上显示",storeDetails.isOnlineShow());
        hashMap.put("是否线上兑换",storeDetails.isOnlineChange());
        hashMap.put("是否线上预约",storeDetails.isOnlineBook());
        hashMap.put("是否启用物流",storeDetails. getStartLogistics());
        hashMap.put("备注", storeDetails.getReMark());
        hashMap.put("折扣系数", storeDetails.getDiscountCoefficient());
        hashMap.put("积分系数", storeDetails.getScoreCoefficient());
        hashMap.put("所属行业", storeDetails.getJob());
        hashMap.put("轮换图", storeDetails.getChangePicture());
        doRequest(action_editStoreDetails,hashMap, callBack);
    }
    /**
     * 登陆
     *
     * @param uName
     * @param uPwd
     * @param callBack
     */
    public void login(String uName, String uPwd, final ServiceCallBack callBack) {
        HashMap map = new HashMap();
        map.put("账号", uName);
        map.put("密钥", uPwd);
        doRequest(action_login, map, new BaseAsynCallBack(new BaseCallBack() {
            @Override
            public void onStart() {
                callBack.onStart();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                LogUtils.e("onError");
                callBack.onError(e, stateCode);
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                LogUtils.e("onEXECisFalse");
                callBack.onEXECisFalse(errroMsg);
            }

            @Override
            public void onEXECSuccess(String result) {
                LogUtils.e("onEXECSuccess");
                try {
                    ArrayList<Staff> staffArrayList = (ArrayList<Staff>) XmlUtils.readXML2BeanList(result, Staff.class, "DATA", Mapping.staffMapping);
                    Staff staff = staffArrayList.get(0);
                    Document document = DocumentHelper.parseText(result);
                    ArrayList<Node> elementArrayList = (ArrayList<Node>) document.selectNodes("//Module[@Key='Commodity']");
                    if (elementArrayList != null && elementArrayList.size() > 0) {
                        staff.setCanUpLoadGoods(true);
                    }
                    callBack.onEXECSuccess(staff);
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
     * 修改员工资料
     *
     * @param staff
     * @param callBack
     */
    public void changeData(Staff staff, BaseCallBack callBack) {
        if (!(staff != null && staff.verification())) {
            return;
        }
        HashMap map = new HashMap();
        map.put("唯一码", staff.getOnlyCode());
        map.put("凭据", staff.getProof());
        map.put("员工", staff.getMark());
        map.put("姓名", staff.getName());
        map.put("电话", staff.getPhone());
        map.put("身份证号码", staff.getPersonID());
        map.put("备注", "");
        if (!StringUtils.isEmpty(staff.getBankNumber())) {
            map.put("银行账号", staff.getBankNumber());
        }
        doRequest(action_changeData, map, new BaseAsynCallBack(callBack));
    }

    /**
     * 修改密码
     *
     * @param staff
     * @param oldPwd
     * @param newPwd
     * @param callBack
     */
    public void changePwd(Staff staff, String oldPwd, String newPwd, BaseCallBack callBack) {
        if (!(staff != null && staff.verification())) {
            return;
        }
        HashMap map = new HashMap();
        map.put("唯一码", staff.getOnlyCode());
        map.put("凭据", staff.getProof());
        map.put("员工", staff.getMark());
        map.put("原密码", oldPwd);
        map.put("新密码", newPwd);
        doRequest(action_changePwd, map, new BaseAsynCallBack(callBack));
    }

    /**
     * 提交身份证
     *
     * @param staff
     * @param positiveImg
     * @param backImg
     * @param callBack
     */
    public void addSfzImage(Staff staff, String positiveImg, String backImg, BaseCallBack callBack) {
        if (!(staff != null && staff.verification())) {
            return;
        }
        HashMap map = new HashMap();
        map.put("唯一码", staff.getOnlyCode());
        map.put("凭据", staff.getProof());
        map.put("员工", staff.getMark());
        map.put("正面图像", positiveImg);
        map.put("反面图像", backImg);
        doRequest(action_addSfzImage, map, new BaseAsynCallBack(callBack));
    }

    /**
     * 上传图片
     *
     * @param base64Img
     * @param baseCallBack
     */
    public void upLoadImage(String base64Img, BaseCallBack baseCallBack) {
        HashMap map = new HashMap();
        map.put("文件数据", base64Img);
        map.put("Action", "Scale");
        map.put("MaxWidth", "320");
        map.put("MaxHeight", "320");
        doRequest(action_upLoadImage, map, new BaseAsynCallBack(baseCallBack));
    }

    /**
     * 提现
     *
     * @param staff
     * @param pwd
     * @param money
     * @param baseCallBack
     */
    public void mentionNow(Staff staff, String pwd, int money, BaseCallBack baseCallBack) {
        if (!(staff != null && staff.verification())) {
            return;
        }
        HashMap map = new HashMap();
        map.put("唯一码", staff.getOnlyCode());
        map.put("凭据", staff.getProof());
        map.put("员工", staff.getMark());
        map.put("密钥", pwd);
        map.put("提取金额", money + "");
        doRequest(action_mentionNow, map, new BaseAsynCallBack(baseCallBack));
    }

    /**
     * 获取店面商品类别
     *
     * @param staff
     * @param baseCallBack
     */
    public void obtainStoreGoodsTypes(Staff staff, BaseCallBack baseCallBack) {
        if (!(staff != null && staff.verification())) {
            return;
        }
        HashMap map = new HashMap();
        map.put("唯一码", staff.getOnlyCode());
        map.put("凭据", staff.getProof());
        map.put("员工", staff.getMark());
        doRequest(action_obtainStoreGoodsTypes, map, new BaseAsynCallBack(baseCallBack));
    }

    /**
     * 上传商品
     *
     * @param staff
     * @param goodsTypeMark
     * @param goodsName
     * @param barCode
     * @param imgPath
     * @param money
     * @param reMark
     * @param baseCallBack
     * @param isOnLineOrder 是否线上订购
     */
    public void action_AddGoods(Staff staff, String goodsMark, String goodsTypeMark, String goodsName, String barCode
            , String imgPath, String money, String reMark, String htmlDetails, boolean isOnLineOrder, BaseCallBack baseCallBack
    ) {
        if (!(staff != null && staff.verification())) {
            return;
        }
        HashMap map = new HashMap();
        map.put("唯一码", staff.getOnlyCode());
        map.put("凭据", staff.getProof());
        map.put("员工", staff.getMark());
        map.put("商品类别标识", goodsTypeMark);
        map.put("名称", goodsName);
        map.put("条码", barCode);
        map.put("图片路径", imgPath);
        map.put("售价", money);
        map.put("图片路径", imgPath);
        reMark = StringUtils.isEmail(reMark) ? "" : reMark;
        map.put("备注", reMark);
        map.put("是否初始消费项目", "0");
        map.put("折扣系数", "1");
        map.put("积分系数", "0");
        map.put("预警值", "0");
        map.put("是否原价销售", "0");
        if (!StringUtils.isEmpty(goodsMark)) {
            map.put("标识", goodsMark);
        }
        if (isOnLineOrder) {
            map.put("是否线上订购", "1");
        } else {
            map.put("是否线上订购", "0");
        }
        map.put("是否线上预约", "0");
        if (StringUtils.isEmpty(htmlDetails)) {
            htmlDetails = "";
        }
        map.put("详情", htmlDetails);
        doRequest(action_AddGoods, map, new BaseAsynCallBack(baseCallBack));
    }

    /**
     * 获取提成明细
     *
     * @param staff
     * @param pageIndex
     * @param pageSize
     * @param callBack
     */
    public void obtainCommissionDetails(Staff staff, int pageIndex, int pageSize, final StaffServiceCallBack<ArrayList<Commission>> callBack) {
        if (!(staff != null && staff.verification())) {
            return;
        }
        HashMap map = new HashMap();
        map.put("唯一码", staff.getOnlyCode());
        map.put("凭据", staff.getProof());
        map.put("员工", staff.getMark());
        map.put("PageIndex", pageIndex + "");
        map.put("PageSize", pageSize + "");
        doRequest(action_obtainCommissionDetails, map, new BaseAsynCallBack(new BaseCallBack() {
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
                    String dataSizeStr = DATAxmlHelper.getAllAttribute(result).get("RecordCount");
                    String pageSizeStr = DATAxmlHelper.getAllAttribute(result).get("PageCount");
                    if (!StringUtils.isEmpty(dataSizeStr)) {
                        dataSize = Integer.parseInt(dataSizeStr);
                    }
                    if (!StringUtils.isEmpty(pageSizeStr)) {
                        pageSize = Integer.parseInt(pageSizeStr);
                    }
                    ArrayList<Commission> commissionArrayList = (ArrayList<Commission>) XmlUtils.readXML2BeanList(result, Commission.class, "提成", Mapping.commissionMapping);
                    String proof = DATAxmlHelper.getAllAttribute(result).get("凭据");
                    callBack.onEXECSuccess(commissionArrayList, proof, pageSize, dataSize);
                    callBack.onEXECSuccess(commissionArrayList, proof);
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
     * 获取提款明细
     *
     * @param staff
     * @param pageIndex
     * @param pageSize
     * @param callBack
     */
    public void obtainDrawingDetails(Staff staff, int pageIndex, int pageSize, final StaffServiceCallBack<ArrayList<Drawing>> callBack) {
        if (!(staff != null && staff.verification())) {
            return;
        }
        HashMap map = new HashMap();
        map.put("唯一码", staff.getOnlyCode());
        map.put("凭据", staff.getProof());
        map.put("员工", staff.getMark());
        map.put("PageIndex", pageIndex + "");
        map.put("PageSize", pageSize + "");
        doRequest(action_obtainDrawingDetails, map, new BaseAsynCallBack(new BaseCallBack() {
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
                    String dataSizeStr = DATAxmlHelper.getAllAttribute(result).get("RecordCount");
                    String pageSizeStr = DATAxmlHelper.getAllAttribute(result).get("PageCount");
                    if (!StringUtils.isEmpty(dataSizeStr)) {
                        dataSize = Integer.parseInt(dataSizeStr);
                    }
                    if (!StringUtils.isEmpty(pageSizeStr)) {
                        pageSize = Integer.parseInt(pageSizeStr);
                    }
                    ArrayList<Drawing> drawingArrayList = (ArrayList<Drawing>) XmlUtils.readXML2BeanList(result, Drawing.class, "提款", Mapping.drawingMapping);
                    String proof = DATAxmlHelper.getAllAttribute(result).get("凭据");
                    callBack.onEXECSuccess(drawingArrayList, proof, pageSize, dataSize);
                    callBack.onEXECSuccess(drawingArrayList, proof);
                } catch (Exception e) {
                    callBack.onError(e, -1);
                }
            }

            @Override
            public void onComplete(String result) {

            }
        }));
    }

    public void obtainGoods(Staff staff, int PageIndex, int PageSize, final StaffServiceCallBack<ArrayList<Goods>> callBack) {
        if (!(staff != null && staff.verification())) {
            return;
        }
        HashMap map = new HashMap();
        map.put("唯一码", staff.getOnlyCode());
        map.put("凭据", staff.getProof());
        map.put("员工", staff.getMark());
        map.put("商品类型", "线上订购");
        map.put("PageIndex", staff.getMark());
        map.put("PageSize", staff.getMark());
        doRequest(action_obtainGoods, map, new BaseAsynCallBack(new BaseCallBack() {
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
                    String dataSizeStr = DATAxmlHelper.getAllAttribute(result).get("RecordCount");
                    String pageSizeStr = DATAxmlHelper.getAllAttribute(result).get("PageCount");
                    if (!StringUtils.isEmpty(dataSizeStr)) {
                        dataSize = Integer.parseInt(dataSizeStr);
                    }
                    if (!StringUtils.isEmpty(pageSizeStr)) {
                        pageSize = Integer.parseInt(pageSizeStr);
                    }
                    ArrayList<Goods> drawingArrayList = (ArrayList<Goods>) XmlUtils.readXML2BeanList(result, Goods.class, "商品", Mapping.goodsMapping);
                    String proof = DATAxmlHelper.getAllAttribute(result).get("凭据");
                    callBack.onEXECSuccess(drawingArrayList, proof, pageSize, dataSize);
                    callBack.onEXECSuccess(drawingArrayList, proof);
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
     * 获取商品详情
     *
     * @param staff
     * @param goodsMark
     * @param callBack  onEXECSuccess obj =Goods
     */
    public void obtainGoodsDetails(Staff staff, String goodsMark, final StaffServiceCallBack<Goods> callBack) {
        HashMap map = new HashMap();
        map.put("唯一码", staff.getOnlyCode());
        map.put("凭据", staff.getProof());
        map.put("员工", staff.getMark());
        map.put("标识", goodsMark);
        doRequest(action_obtainGoodsDetails, map, new BaseAsynCallBack(new BaseCallBack() {
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
                    ArrayList<Goods> drawingArrayList = (ArrayList<Goods>) XmlUtils.readXML2BeanList(result, Goods.class, "DATA", Mapping.goodsMapping);
                    String proof = DATAxmlHelper.getAllAttribute(result).get("凭据");
                    callBack.onEXECSuccess(drawingArrayList.get(0), proof, 0, 0);
                    callBack.onEXECSuccess(drawingArrayList.get(0), proof);
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
     * @param callBack
     */
    // result obj = ArrayList<FenLeiStore>
    public void obtainGoodsType(Staff staff, final StaffServiceCallBack<ArrayList<FenLeiStore>> callBack) {
        HashMap map = new HashMap();
        map.put("唯一码", staff.getOnlyCode());
        map.put("凭据", staff.getProof());
        map.put("员工", staff.getMark());
        doRequest(action_obtianStoreGoodsType, map, new BaseAsynCallBack(new BaseCallBack() {
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
                    ArrayList<FenLeiStore> fenleiArrayList = (ArrayList<FenLeiStore>) XmlUtils.readXML2BeanList(result, FenLeiStore.class, "商品类别", Mapping.fenleiMapping);
                    String proof = DATAxmlHelper.getAllAttribute(result).get("凭据");
                    callBack.onEXECSuccess(fenleiArrayList, proof, 0, 0);
                    callBack.onEXECSuccess(fenleiArrayList, proof);
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
     * @param callBack
     */
    public void obtainGoodsOrder(Staff staff, int pageIndex, int pageSize, final StaffServiceCallBack<ArrayList<Order>> callBack) {
        HashMap map = new HashMap();
        map.put("唯一码", staff.getOnlyCode());
        map.put("凭据", staff.getProof());
        map.put("员工", staff.getMark());
        map.put("PageIndex", pageIndex + "");
        map.put("PageSize", pageSize + "");
        doRequest(action_obtainGoodsOrder, map, new BaseAsynCallBack(new BaseCallBack() {
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
                System.out.print(result);
                LogUtils.fff(result);
                try {
                    int pageSize = 0;
                    int dataSize = 0;
                    String dataSizeStr = DATAxmlHelper.getAllAttribute(result).get("RecordCount");
                    String pageSizeStr = DATAxmlHelper.getAllAttribute(result).get("PageCount");
                    if (!StringUtils.isEmpty(dataSizeStr)) {
                        dataSize = Integer.parseInt(dataSizeStr);
                    }
                    if (!StringUtils.isEmpty(pageSizeStr)) {
                        pageSize = Integer.parseInt(pageSizeStr);
                    }
                    ArrayList<Order> orderList = (ArrayList<Order>) XmlUtils.readXML2BeanList(result, Order.class, "单据", Mapping.orderMapping);
                    ArrayList<ArrayList<OrderDetails>> orderDetailList = XmlUtils.readOrderDetail(result);
                    for (int i = 0; i<orderList.size(); i++){
                        orderList.get(i).setOrderDetailsArrayList(orderDetailList.get(i));
                    }

                    String proof = DATAxmlHelper.getAllAttribute(result).get("凭据");
                    callBack.onEXECSuccess(orderList, proof, pageSize, dataSize);
                    callBack.onEXECSuccess(orderList, proof);
                } catch (Exception e) {
                    callBack.onError(e, -1);
                }
            }

            @Override
            public void onComplete(String result) {

            }
        }));
    }

}