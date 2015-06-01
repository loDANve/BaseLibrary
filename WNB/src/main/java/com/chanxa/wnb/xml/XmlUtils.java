package com.chanxa.wnb.xml;

import com.chanxa.wnb.bean.onLineShop.Order;
import com.chanxa.wnb.bean.onLineShop.OrderDetails;
import com.chanxa.wnb.xml.mapping.Mapping;
import com.wtm.library.utils.LogUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class XmlUtils {
    /**
     * 将一个xml document内某个节点全部转成指定的bean对象,需要属性映射关系集
     *
     * @param document
     * @param beanClass
     * @param nodeName   节点名
     * @param attMapping 属性映射关系 k:xml属性名 v:javaBean字段名
     * @return
     * @throws Exception
     */
    public static <T> List<T> readXML2BeanList(Document document,
                                               Class<T> beanClass, String nodeName, Map<String, String> attMapping)
            throws Exception {
        List<T> list = new ArrayList<T>();// 创建list集合
        @SuppressWarnings("unchecked")
        List<Node> nodeList = document.selectNodes("//" + nodeName);
        if (attMapping == null) {
            for (int i = 0; i < nodeList.size(); i++) {
                Element element = (Element) nodeList.get(i);
                T tempT = beanClass.newInstance();
                Field[] fieldArr = beanClass.getDeclaredFields();
                for (Field field : fieldArr) {
                    field.setAccessible(true);
                    String attVal = element.attributeValue(field.getName());
                    field.set(tempT, attVal);
                }
                list.add(tempT);
            }
            return list;
        }
        for (int i = 0; i < nodeList.size(); i++) {
            Element element = (Element) nodeList.get(i);
            Set<Entry<String, String>> mappingSet = attMapping.entrySet();
            T tempT = beanClass.newInstance();
            for (Entry<String, String> entry : mappingSet) {

                String key = entry.getKey();
                String value = entry.getValue();
                // 获取节点对应Key的属性
                String attVal = element.attributeValue(key);
                Field valueFiled = beanClass.getDeclaredField(value);
                valueFiled.setAccessible(true);
                valueFiled.set(tempT, attVal);
            }
            list.add(tempT);
        }
        return list;
    }

    /**
     * @param documentStr 传入document字符串
     * @param beanClass
     * @param nodeName
     * @param attMapping
     * @return
     * @throws Exception
     */
    public static <T> List<T> readXML2BeanList(String documentStr,
                                               Class<T> beanClass, String nodeName, Map<String, String> attMapping)
            throws Exception {
        return readXML2BeanList(DocumentHelper.parseText(documentStr),
                beanClass, nodeName, attMapping);
    }

    public static <T> String writeBean2Xml(String nodeName, T t) throws Exception {
        Field[] fields = t.getClass().getDeclaredFields();
        String xml = "<" + nodeName + " ";
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(t) == null) {
                continue;
            }
            xml += field.getName() + "=\"" + field.get(t) + "\" ";
        }
        xml += "/>";
        return xml;
    }


    /**
     * 替换字符串中特殊字符
     */
    public static String encodeString(String strData) {
        if (strData == null) {
            return "";
        }
        strData = replaceString(strData, "&", "&amp;");
        strData = replaceString(strData, "<", "&lt;");
        strData = replaceString(strData, ">", "&gt;");
        strData = replaceString(strData, "'", "&apos;");
        strData = replaceString(strData, "\"", "&quot;");
        return strData;
    }

    /**
     * 替换一个字符串中的某些指定字符
     *
     * @param strData     String 原始字符串
     * @param regex       String 要替换的字符串
     * @param replacement String 替代字符串
     * @return String 替换后的字符串
     */
    public static String replaceString(String strData, String regex,
                                       String replacement) {
        if (strData == null) {
            return null;
        }
        int index;
        index = strData.indexOf(regex);
        String strNew = "";
        if (index >= 0) {
            while (index >= 0) {
                strNew += strData.substring(0, index) + replacement;
                strData = strData.substring(index + regex.length());
                index = strData.indexOf(regex);
            }
            strNew += strData;
            return strNew;
        }
        return strData;
    }

    /**
     *
     */
    public static ArrayList<ArrayList<OrderDetails>> readOrderDetail(String documentStr) {
        ArrayList<ArrayList<OrderDetails>> goodsList = null;
        try {
            // 转换成Document对象
            Document document = DocumentHelper.parseText(documentStr);
            //获取根节点元素对象
            Element node = document.getRootElement();
            // 获取node节点中，子节点的元素名称为西游记的元素节点。
            List<Element> orders = node.elements("单据");
            //Element e = node.element("西游记");
            // 获取西游记元素节点中，子节点为作者的元素节点(可以看到只能获取第一个作者元素节点)
	        /*Element author = e.element("作者");
	        Log.e("FFF", e.getName() + "----" + author.getText()); */
            // 获取西游记这个元素节点 中，所有子节点名称为作者元素的节点 。
            goodsList = new ArrayList<ArrayList<OrderDetails>>();
            for (Element goods : orders) {
                List<Element> goodsDetails = goods.elements("明细");
                ArrayList<OrderDetails> orderDetailsArrayList = new ArrayList<OrderDetails>();
                for (Element detail : goodsDetails) {
                    List<Attribute> list = detail.attributes();
                    // 遍历属性节点
                    OrderDetails orderDetails = new OrderDetails();
                    for (Attribute attr : list) {
                        if ("商品标识".equals(attr.getName())){
                            orderDetails.setMark(attr.getValue());
                            continue;
                        }
                        if ("商品名称".equals(attr.getName())){
                            orderDetails.setName(attr.getValue());
                            continue;
                        }
                        if ("商品类别".equals(attr.getName())){
                            orderDetails.setType(attr.getValue());
                            continue;
                        }
                        if ("商品条码".equals(attr.getName())){
                            orderDetails.setBarCode(attr.getValue());
                            continue;
                        }
                        if ("商品图片路径".equals(attr.getName())){
                            orderDetails.setImgPath(attr.getValue());
                            continue;
                        }
                        if ("订购数量".equals(attr.getName())){
                            orderDetails.setNumber(attr.getValue());
                            continue;
                        }
                        //LogUtils.fff(attr.getName()+ "---" + attr.getValue());
                    }
                    //Log.e( "FFF", pjj.getText());
                    orderDetailsArrayList.add(orderDetails);
                }
                goodsList.add(orderDetailsArrayList);
            }
            return goodsList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goodsList;
    }
}
