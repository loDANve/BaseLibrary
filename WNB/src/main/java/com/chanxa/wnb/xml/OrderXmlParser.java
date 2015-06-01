package com.chanxa.wnb.xml;

import android.util.Xml;

import com.chanxa.wnb.bean.onLineShop.Distribution;
import com.chanxa.wnb.bean.onLineShop.Logistics;
import com.chanxa.wnb.bean.onLineShop.Order;
import com.chanxa.wnb.bean.onLineShop.OrderDetails;
import com.wtm.library.utils.StringUtils;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by CHANXA on 2015/1/14.
 * 订单xml解析器
 */
public class OrderXmlParser {

    private XmlPullParser xmlPullParser;

    public OrderXmlParser() {
        xmlPullParser = Xml.newPullParser();
    }

    public ArrayList<Order> parserOrderList(String xml) throws Exception {
        if (StringUtils.isEmpty(xml)) {
            return null;
        }
        ArrayList<Order> orderArrayList = new ArrayList<>();
        Order curOrder = null;
        OrderDetails curOrderDetails = null;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        xmlPullParser.setInput(byteArrayInputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = xmlPullParser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if ("单据".equals(xmlPullParser.getName())) {
                        curOrder = new Order();
                        curOrder.setMark(xmlPullParser.getAttributeValue(null,"标识"));
                        curOrder.setStoreMark(xmlPullParser.getAttributeValue(null,"店面标识"));
                        curOrder.setOrderNumber(xmlPullParser.getAttributeValue(null,"单据号"));
                        curOrder.setSubTime(xmlPullParser.getAttributeValue(null,"提交时间"));
                        curOrder.setReceiverPersonName(xmlPullParser.getAttributeValue(null,"收货人"));
                        curOrder.setReceiverPersonPhone(xmlPullParser.getAttributeValue(null,"手机号"));
                        curOrder.setReceiverAddress(xmlPullParser.getAttributeValue(null,"收货地址"));
                        curOrder.setReceiverMoney(xmlPullParser.getAttributeValue(null,"运费"));
                        curOrder.setState(xmlPullParser.getAttributeValue(null,"状态"));
                        curOrder.setConsumptionMoney(xmlPullParser.getAttributeValue(null,"消费总金额"));
                    } else if ("明细".equals(xmlPullParser.getName())) {
                        curOrderDetails = new OrderDetails();
                        curOrderDetails.setMark(xmlPullParser.getAttributeValue(null,"商品标识"));
                        curOrderDetails.setName(xmlPullParser.getAttributeValue(null,"商品名称"));
                        curOrderDetails.setType(xmlPullParser.getAttributeValue(null,"商品类别"));
                        curOrderDetails.setBarCode(xmlPullParser.getAttributeValue(null,"商品条码"));
                        curOrderDetails.setImgPath(xmlPullParser.getAttributeValue(null,"商品图片路径"));
                        curOrderDetails.setNumber(xmlPullParser.getAttributeValue(null,"订购数量"));
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("单据".equals(xmlPullParser.getName())) {
                        orderArrayList.add(curOrder);
                    } else if ("明细".equals(xmlPullParser.getName())) {
                        curOrder.addOrderDetails(curOrderDetails);
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }
        return orderArrayList;
    }
}
