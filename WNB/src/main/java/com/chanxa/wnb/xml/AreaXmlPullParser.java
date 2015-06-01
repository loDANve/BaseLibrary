package com.chanxa.wnb.xml;

import android.util.Xml;

import com.chanxa.wnb.bean.area.Area;
import com.chanxa.wnb.bean.area.City;
import com.chanxa.wnb.bean.area.District;
import com.chanxa.wnb.bean.area.Province;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

/**
 * Created by CHANXA on 2014/12/27.
 */
public class AreaXmlPullParser {

    private XmlPullParser xmlPullParser;

    public AreaXmlPullParser() {
        xmlPullParser = Xml.newPullParser();
    }

    public Area parserArea(String xmlData) throws Exception {
        Area area= new Area();
        Province currentProvince = null;
        City currentCity = null;
        District currentDistrict=null;
        //xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + xmlData;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xmlData.getBytes("UTF-8"));
        xmlPullParser.setInput(byteArrayInputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = xmlPullParser.getName();
            switch (eventType) {
                /*case XmlPullParser.START_DOCUMENT:
                    if ("省市区县".equals(xmlPullParser.getName())) {
                        area = new Area();
                    }
                    break;*/
                case XmlPullParser.START_TAG:
                    if ("省".equals(xmlPullParser.getName())) {
                        currentProvince = new Province();
                        currentProvince.setMark(xmlPullParser.getAttributeValue(0));
                        currentProvince.setName(xmlPullParser.getAttributeValue(1));
                    } else if ("市".equals(xmlPullParser.getName())) {
                        currentCity = new City();
                        currentCity.setMark(xmlPullParser.getAttributeValue(0));
                        currentCity.setName(xmlPullParser.getAttributeValue(1));
                    } else if ("区县".equals(xmlPullParser.getName())) {
                        currentDistrict = new District();
                        currentDistrict.setMark(xmlPullParser.getAttributeValue(0));
                        currentDistrict.setName(xmlPullParser.getAttributeValue(1));
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("省".equals(xmlPullParser.getName())) {
                        area.addProvince(currentProvince);
                        currentProvince = null;
                    } else if ("市".equals(xmlPullParser.getName())) {
                        currentProvince.addCity(currentCity);
                        currentCity = null;
                    } else if ("区县".equals(xmlPullParser.getName())) {
                        currentCity.addDistrict(currentDistrict);
                        currentDistrict = null;
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }
        return area;
    }
}
