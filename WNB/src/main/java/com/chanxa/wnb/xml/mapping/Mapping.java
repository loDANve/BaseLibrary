package com.chanxa.wnb.xml.mapping;

import java.util.HashMap;

public class Mapping {

    public static HashMap<String,String>  storeDetailsMapping;
    public static HashMap<String, String> storeMapping;
    public static HashMap<String, String> cardMapping;
    public static HashMap<String, String> memberMapping;
    public static HashMap<String, String> profitMapping;
    public static HashMap<String, String> goodsMapping;
    public static HashMap<String, String> profitRecordMapping;
    public static HashMap<String, String> makeRecordMapping;
    public static HashMap<String, String> industryMapping;
    public static HashMap<String, String> storedValueRecordMapping;
    public static HashMap<String, String> staffMapping;
    public static HashMap<String, String> staffGoodsMapping;
    public static HashMap<String, String> commissionMapping;
    public static HashMap<String, String> drawingMapping;
    public static HashMap<String, String> fenleiMapping;
    public static HashMap<String, String> segmentMapping;
    public static HashMap<String, String> rechargeProductsMapping;
    public static HashMap<String, String> orderMapping;
    public static HashMap<String, String> orderDetailMapping;

    static {
        storeDetailsMapping=new HashMap<String,String>();
        storeDetailsMapping.put("标识", "mark");
        storeDetailsMapping.put("名称", "name");
        storeDetailsMapping.put("联系电话", "phone");
        storeDetailsMapping.put("省", "province");
        storeDetailsMapping.put("市", "city");
        storeDetailsMapping.put("区县", "area");
        storeDetailsMapping.put("所属行业", "job");
        storeDetailsMapping.put("详细地址", "detailsAddress");
        storeDetailsMapping.put("图标路径", "imgPath");
        storeDetailsMapping.put("轮播图", "changePicture");
        storeDetailsMapping.put("纬度", "lat");
        storeDetailsMapping.put("经度", "lng");
        storeDetailsMapping.put("备注", "reMark");
        storeDetailsMapping.put("折扣系数","discountCoefficient");
        storeDetailsMapping.put("积分系数","scoreCoefficient");
        storeDetailsMapping.put("是否启用物流", "startLogistics");
        storeDetailsMapping.put("是否线上订购","onlineBuy");
        storeDetailsMapping.put("是否线上预约","onlineBook");
        storeDetailsMapping.put("是否线上显示","onlineShow");
        storeDetailsMapping.put("是否线上兑换","onlineChange");
/***************************************************************/
        storeMapping = new HashMap<String, String>();
        storeMapping.put("标识", "mark");
        storeMapping.put("名称", "name");
        storeMapping.put("联系电话", "phone");
        storeMapping.put("省", "province");
        storeMapping.put("市", "city");
        storeMapping.put("区县", "area");
        storeMapping.put("详细地址", "detailsAddress");
        storeMapping.put("启用物流", "startLogistics");
        storeMapping.put("图标路径", "imgPath");
        storeMapping.put("纬度", "lat");
        storeMapping.put("经度", "lng");
        storeMapping.put("距离", "distance");
        storeMapping.put("权重", "weight");
        storeMapping.put("备注", "reMark");
/**********************************************************/
        cardMapping = new HashMap<String, String>();
        cardMapping.put("会员标识", "memberMark");
        cardMapping.put("姓名", "name");
        cardMapping.put("称谓", "appellation");
        cardMapping.put("生日", "birthDay");
        cardMapping.put("手机", "phone");
        cardMapping.put("邮箱", "email");
        cardMapping.put("联系地址", "address");
        cardMapping.put("照片路径", "photoPath");
        cardMapping.put("身份证号码", "personID");
        cardMapping.put("备注", "remarks");
        cardMapping.put("注册时间", "regTime");
        cardMapping.put("卡已消费次数", "cardConsumptionCount");
        cardMapping.put("卡最后消费时间", "cardConsumptionLastTime");
        cardMapping.put("卡号", "cardNumber");
        cardMapping.put("卡类型", "cardType");
        cardMapping.put("卡级别", "cardLv");
        cardMapping.put("卡级别图标", "cardLvImgPath");
        cardMapping.put("消费额", "consumptionMoney");
        cardMapping.put("可用储值", "balance");
        cardMapping.put("可用积分", "canUseScore");
        cardMapping.put("卡发放时间", "cardIssuanceTime");
        cardMapping.put("卡作废时间", "cardVoidTime");
        cardMapping.put("利率", "yearInterestRate");
/**********************************************************/
        memberMapping = new HashMap<String, String>();
        memberMapping.put("姓名", "name");
        memberMapping.put("标识", "memberMark");
        memberMapping.put("称谓", "appellation");
        memberMapping.put("手机", "phone");
        memberMapping.put("邮箱", "email");
        memberMapping.put("联系地址", "address");
        memberMapping.put("照片路径", "photoPath");
        memberMapping.put("出生日期", "birhday");
        memberMapping.put("备注", "remarks");
        memberMapping.put("身份证号码", "personID");
/**********************************************************/
        profitMapping = new HashMap<String, String>();
        profitMapping.put("累计收益", "countProfit");
        profitMapping.put("上月收益", "lastMonthProfit");
        profitMapping.put("上周收益", "lastWeekProfit");
/**********************************************************/
        fenleiMapping = new HashMap<String, String>();
        fenleiMapping.put("标识", "mark");
        fenleiMapping.put("店面标识", "storeMark");
        fenleiMapping.put("名称", "name");
/**********************************************************/
        goodsMapping= new HashMap<String, String>();
        goodsMapping.put("标识", "mark");
        goodsMapping.put("店面标识", "storeMark");
        goodsMapping.put("商品类别标识", "goodsTypeMark");
        goodsMapping.put("名称", "name");
        goodsMapping.put("条码", "barCode");
        goodsMapping.put("售价", "money");
        goodsMapping.put("图片路径", "imgPath");
        goodsMapping.put("折扣系数", "discountRatio");
        goodsMapping.put("积分系数", "scoreRatio");
        goodsMapping.put("备注", "remarks");
        goodsMapping.put("是否原价销售", "isOriginalMoneySell");
        goodsMapping.put("详情", "details");
        goodsMapping.put("开始抢购时间", "panicBuyStartTime");
        goodsMapping.put("截止抢购时间", "panicBuyendTime");
        goodsMapping.put("抢购数量", "panicBuyNumber");
        goodsMapping.put("是否线上订购", "isOnLineOrder");
/***********************************************************/
        makeRecordMapping= new HashMap<String, String>();
        makeRecordMapping.put("提交时间", "subTime");
        makeRecordMapping.put("标识", "mark");
        makeRecordMapping.put("店面标识", "storeMark");
        makeRecordMapping.put("商品名称", "goodsName");
        makeRecordMapping.put("联系人", "contact");
        makeRecordMapping.put("联系电话", "contactPhone");
        makeRecordMapping.put("预约时间", "makeTime");
        makeRecordMapping.put("预约日期", "makeDate");
        makeRecordMapping.put("到场人数", "personNumber");
        makeRecordMapping.put("备注", "remarks");
        makeRecordMapping.put("状态", "state");
/***********************************************************/
        profitRecordMapping= new HashMap<String, String>();
        profitRecordMapping.put("店面标识", "storeMark");
        profitRecordMapping.put("可用储值", "canUserMoney");
        profitRecordMapping.put("结息金额", "yesterdayMoney");
        profitRecordMapping.put("时间", "time");
        profitRecordMapping.put("利率", "interestRate");
/***********************************************************/
        storedValueRecordMapping= new HashMap<String, String>();
        storedValueRecordMapping.put("店面标识", "storeMark");
        storedValueRecordMapping.put("变化额度", "changeNumber");
        storedValueRecordMapping.put("变化类型", "changeType");
        storedValueRecordMapping.put("解冻时间", "thawTime");
        storedValueRecordMapping.put("是否冻结", "isFreezing");
        storedValueRecordMapping.put("备注", "raMark");
        storedValueRecordMapping.put("时间", "time");
/***********************************************************/
        industryMapping = new HashMap<>();
        industryMapping.put("标识", "mark");
        industryMapping.put("名称", "name");
/***********************************************************/
        staffMapping = new HashMap<>();
        staffMapping.put("唯一码", "onlyCode");
        staffMapping.put("凭据", "proof");
        staffMapping.put("员工-标识", "mark");
        staffMapping.put("员工-姓名", "name");
        staffMapping.put("员工-电话", "phone");
        staffMapping.put("员工-身份证号码", "personID");
        staffMapping.put("员工-身份证是否审核", "personIDIsOk");
        staffMapping.put("员工-银行账号", "bankNumber");
        staffMapping.put("员工-账户余额", "accountBalance");
        staffMapping.put("商家-标识", "storeMark");
        staffMapping.put("员工-是否存在未审核身份证审核申请单据", "personIDIsExist");
/***********************************************************/
        staffGoodsMapping= new HashMap<>();
        staffGoodsMapping.put("标识", "mark");
        staffGoodsMapping.put("店面标识", "storeMark");
        staffGoodsMapping.put("名称", "name");
        staffGoodsMapping.put("备注", "reMark");
        staffGoodsMapping.put("父级", "parent");
/***********************************************************/
        commissionMapping = new HashMap<>();
        commissionMapping.put("标识", "mark");
        commissionMapping.put("店面标识", "storeMark");
        commissionMapping.put("变化额度", "changeQuota");
        commissionMapping.put("备注", "reMark");
        commissionMapping.put("时间", "time");
/***********************************************************/
        drawingMapping = new HashMap<>();
        drawingMapping.put("标识", "mark");
        drawingMapping.put("店面标识", "storeMark");
        drawingMapping.put("提款金额", "drawingMoney");
        drawingMapping.put("备注", "reMark");
        drawingMapping.put("时间", "time");
/***********************************************************/
        segmentMapping = new HashMap<>();
        segmentMapping.put("运营商", "carrieroperator");
        segmentMapping.put("省", "province");
        segmentMapping.put("城市区号", "cityPostalCode");
        segmentMapping.put("描述", "description");
/***********************************************************/
        rechargeProductsMapping = new HashMap<>();
        rechargeProductsMapping.put("标识", "characteristic");
        rechargeProductsMapping.put("面额", "denomination");
        rechargeProductsMapping.put("售价", "sellingPrice");
        rechargeProductsMapping.put("运营商", "carrieroperator");
        rechargeProductsMapping.put("充值处理时间", "voucherTime");
        rechargeProductsMapping.put("省", "province");
        rechargeProductsMapping.put("类型", "type");
/***********************************************************/
        orderMapping = new HashMap<>();
        orderMapping.put("标识", "mark");
        orderMapping.put("店面标识", "storeMark");
        orderMapping.put("单据号", "orderNumber");
        orderMapping.put("提交时间", "subTime");
        orderMapping.put("收货人", "receiverPersonName");
        orderMapping.put("手机号", "receiverPersonPhone");
        orderMapping.put("收货地址", "receiverAddress");
        orderMapping.put("运费", "receiverMoney");
        orderMapping.put("状态", "state");
        orderMapping.put("消费总金额", "consumptionMoney");
        orderMapping.put("获得总积分", "getTotalScore");
        orderMapping.put("审核时间", "reviewingTime");
        orderMapping.put("消费总金额", "consumptionMoney");
        orderMapping.put("发货时间", "deliveryTime");
        orderMapping.put("取消时间", "cancelTime");
        orderMapping.put("取消原因", "cancelReason");
        orderMapping.put("备注", "remark");
        orderMapping.put("运单号", "waybillNum");
        orderMapping.put("邮费货到付款", "payCashonDelivery");
        orderMapping.put("配送至", "distributionTo");
        orderMapping.put("物流方式", "logistics");
        orderMapping.put("邮编", "postCode");
/***********************************************************/
        orderDetailMapping = new HashMap<>();
        orderMapping.put("商品标识", "mark");
    }

}
