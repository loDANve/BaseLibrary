package com.chanxa.wnb.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.tree.BaseElement;

public class DATAxmlHelper {
	/**
	 * 根据属性对获取所需 DATA xml字符串
	 * 
	 * @param valueMap
	 * @return
	 */
	public static String getDATAxml(HashMap<String, String> valueMap) {
		Element data = new BaseElement("DATA");
		Set<Entry<String, String>> set = valueMap.entrySet();
		for (Entry<String, String> entry : set) {
			data.addAttribute(entry.getKey(), entry.getValue());
		}
		return data.asXML();
	}

    /**
     * 根据属性对获取所需 DATA xml字符串
     *
     * @param valueMap
     * @return
     */
    public static String getDetailxml(HashMap<String, String> valueMap) {
        Element data = new BaseElement("明细");
        Set<Entry<String, String>> set = valueMap.entrySet();
        for (Entry<String, String> entry : set) {
            data.addAttribute(entry.getKey(), entry.getValue());
        }
        return data.asXML();
    }

	/**
	 * 2个参数的简便操作
	 * @param key1
	 * @param value1
	 * @param key2
	 * @param value2
	 * @return
	 */
	public static String getDATAxml(String key1,String value1,String key2,String value2) {
		Element data = new BaseElement("DATA");
		data.addAttribute(key1, value1);
		data.addAttribute(key2, value2);
		return data.asXML();
	}

	/**
	 * 
	 * @param DATA
	 *            DATA节点的xml字符串
	 * @return 获取所有属性对
	 * @throws DocumentException 当传入字符串不能转化为document时,抛出此例外
	 */
	public static HashMap<String, String> getAllAttribute(String DATA)
			throws DocumentException {
		HashMap<String, String> attributeMap = new HashMap<String, String>();
		Document doc = DocumentHelper.parseText(DATA);
		Element data =  doc.getRootElement();
		for (Iterator<?> it = data.attributeIterator(); it.hasNext();) {
			Attribute attribute = (Attribute) it.next();
			attributeMap.put(attribute.getName(), attribute.getText());
		}
		return attributeMap; 
	}
	
	@SuppressWarnings("unchecked")
	public static List<Node> getNodeList(String DATA,String elementName) throws DocumentException{
		Document doc = DocumentHelper.parseText(DATA);
		return doc.selectNodes(elementName);
	}
}
