package com.chanxa.wnb.xml;

import com.chanxa.wnb.bean.FenLeiStore;
import com.chanxa.wnb.bean.StaffStoreGoods;
import com.chanxa.wnb.bean.StaffStoreGoodsLV1;
import com.chanxa.wnb.bean.StaffStoreGoodsLV2;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CHANXA on 2015/1/9.
 */
public class GoodsTypeXmlParser {
    /**
     * 解析商品类别（店面元素唯一）
     * @param xml
     * @return
     * @throws Exception
     */
    public ArrayList<StaffStoreGoodsLV1> parserStaffStoreGoodsLV1(String xml) throws Exception {
        Document document = DocumentHelper.parseText(xml);
        ArrayList<StaffStoreGoodsLV1> staffStoreGoodsLV2ArrayList = new ArrayList<>();
        List<Node> nodeList = document.selectNodes("//店面/" + "商品类别");
        for (Node node : nodeList) {
            StaffStoreGoodsLV1 staffStoreGoodsLV2 = new StaffStoreGoodsLV1();
            Element tempElement = (Element) node;
            staffStoreGoodsLV2.setStoreMark(tempElement.attributeValue("店面标识"));
            staffStoreGoodsLV2.setName(tempElement.attributeValue("名称"));
            staffStoreGoodsLV2.setReMark(tempElement.attributeValue("备注"));
            staffStoreGoodsLV2.setMark(tempElement.attributeValue("标识"));
            staffStoreGoodsLV2.setImgPath(tempElement.attributeValue("图标路径"));
            String nodeStr111 = node.asXML();
            Document document1 = DocumentHelper.parseText(nodeStr111);
            List<Node> childNodeList = document1.selectNodes("/商品类别/商品类别");
            for (Node tempNode : childNodeList) {
                StaffStoreGoodsLV2 staffStoreGoodsLV3 = new StaffStoreGoodsLV2();
                Element tempElement2 = (Element) tempNode;
                staffStoreGoodsLV3.setStoreMark(tempElement2.attributeValue("店面标识"));
                staffStoreGoodsLV3.setName(tempElement2.attributeValue("名称"));
                staffStoreGoodsLV3.setReMark(tempElement2.attributeValue("备注"));
                staffStoreGoodsLV3.setMark(tempElement2.attributeValue("标识"));
                staffStoreGoodsLV3.setImgPath(tempElement2.attributeValue("图标路径"));
                String nodeStr222 = tempNode.asXML();
                Document document2 = DocumentHelper.parseText(tempNode.asXML());
                List<Node> tempChildNodeList = document2.selectNodes("/商品类别/商品类别");
                for (Node node1 : tempChildNodeList) {
                    Element tempElement3 = (Element) node1;
                    StaffStoreGoods staffStoreGoods = new StaffStoreGoods();
                    staffStoreGoods.setStoreMark(tempElement3.attributeValue("店面标识"));
                    staffStoreGoods.setName(tempElement3.attributeValue("名称"));
                    staffStoreGoods.setReMark(tempElement3.attributeValue("备注"));
                    staffStoreGoods.setMark(tempElement3.attributeValue("标识"));
                    staffStoreGoods.setParent(tempElement3.attributeValue("父级"));
                    staffStoreGoods.setImgPath(tempElement3.attributeValue("图标路径"));
                    staffStoreGoodsLV3.addStaffStoreGoods(staffStoreGoods);
                }
                staffStoreGoodsLV2.addStaffStoreGoodsLV3(staffStoreGoodsLV3);
            }
            staffStoreGoodsLV2ArrayList.add(staffStoreGoodsLV2);
        }
        return staffStoreGoodsLV2ArrayList;
    }
    /**
     * 解析商品类别(多店面元素)
     * @param xml
     * @return
     * @throws Exception
     */
    public ArrayList<FenLeiStore> parserStaffStoreGoodsLV1ArrayList(String xml) throws Exception{
        Document document = DocumentHelper.parseText(xml);
        List<Node> nodeList = document.selectNodes("//店面");
        ArrayList<FenLeiStore> fenLeiStores=new ArrayList<>();
        for (Node node : nodeList) {
            FenLeiStore fenLeiStore = new FenLeiStore();
            fenLeiStore.setStaffStoreGoodsLV1ArrayList(parserStaffStoreGoodsLV1(node.asXML()));
            Element tempElement = (Element) node;
            fenLeiStore.setMark(tempElement.attributeValue("标识"));
            fenLeiStore.setName(tempElement.attributeValue("名称"));
            fenLeiStore.setReMark(tempElement.attributeValue("备注"));
            fenLeiStore.setDiscountCoefficient(tempElement.attributeValue("折扣系数"));
            fenLeiStore.setScoreCoefficient(tempElement.attributeValue("积分系数"));
            fenLeiStores.add(fenLeiStore);
        }
        return fenLeiStores;
    }
}
