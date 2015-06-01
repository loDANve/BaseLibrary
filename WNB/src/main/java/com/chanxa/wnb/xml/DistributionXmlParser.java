package com.chanxa.wnb.xml;

import android.util.Xml;

import com.chanxa.wnb.bean.area.City;
import com.chanxa.wnb.bean.area.District;
import com.chanxa.wnb.bean.area.Province;
import com.chanxa.wnb.bean.onLineShop.Distribution;
import com.chanxa.wnb.bean.onLineShop.Logistics;
import com.wtm.library.utils.StringUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 * Created by CHANXA on 2015/1/14.
 * 解析配送地xml数据
 */
public class DistributionXmlParser {
    private XmlPullParser xmlPullParser;

    public DistributionXmlParser() {
        xmlPullParser = Xml.newPullParser();
    }

    public ArrayList<Distribution> parserDostributionList(String xml) throws Exception {
        if (StringUtils.isEmpty(xml)) {
            return null;
        }
        ArrayList<Distribution> distributionArrayList = new ArrayList<>();
        Distribution curDistrict = null;//配送地
        Logistics curLogistics = null;//物流
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        xmlPullParser.setInput(byteArrayInputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = xmlPullParser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if ("配送地".equals(xmlPullParser.getName())) {
                        curDistrict = new Distribution();
                        curDistrict.setProvince(xmlPullParser.getAttributeValue(null,"省"));
                        curDistrict.setCity(xmlPullParser.getAttributeValue(null,"市"));
                        curDistrict.setDistrict(xmlPullParser.getAttributeValue(null,"区县"));
                    } else if ("物流".equals(xmlPullParser.getName())) {
                        curLogistics = new Logistics();
                        curLogistics.setType(xmlPullParser.getAttributeValue(0));
                        curLogistics.setMoney(xmlPullParser.getAttributeValue(1));
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("配送地".equals(xmlPullParser.getName())) {
                        distributionArrayList.add(curDistrict);
                        curDistrict = null;
                    } else if ("物流".equals(xmlPullParser.getName())) {
                        curDistrict.addLogistics(curLogistics);
                        curLogistics = null;
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }
        return distributionArrayList;
    }

}
