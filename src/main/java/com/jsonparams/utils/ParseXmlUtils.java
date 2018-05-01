package com.jsonparams.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 解析xml文件
 * @author ZhangPj
 *
 */
public class ParseXmlUtils {
	
	public static List<String> parseResult() throws DocumentException {
		return parseXML();
	}
	
	/**
	 * 解析xml配置文件
	 * @return
	 * @throws DocumentException 
	 */
	public static List<String> parseXML () throws DocumentException {
		Element rootElement = readXMLFile () ;
		//得到所有package下的所有包
		Element ele_packages = rootElement.element("packages");
		
		
		Map<String, List<Map<String, Map<String, String>>>> mapClassRules = ParsePackageClass.parsePackages(ele_packages);
		
//		String result = ParseClassRules.parseClassAndRules(mapClassRules);
		
		
		
		
//		Element ele_package_parent = ele_packages.element("package-parent");
		
		//遍历包  
		return null;
	}
	
	/**
	 * 读取配置文件
	 * @return
	 * @throws DocumentException Xj
	 */
	public static Element readXMLFile () throws DocumentException {
		SAXReader saxReader = new SAXReader();
		//读取到相应文件
		Document document = saxReader.read("src/main/resources/JsonParams.xml");
		//获取根元素
		Element rootElement = document.getRootElement();
		
		return rootElement;
	}
	
	
	/**
	 * 列出所有的根节点下的元素
	 * @param element
	 * @return
	 */
	public static List<String> listElement (Element element, String queryString) {
		//返回结果集
		List<String> listResult = new ArrayList<String>();
		//在element中过滤查询串获取相应的节点
		@SuppressWarnings("unchecked")
		List<Element> elements = element.elements(queryString);
		//遍历取到对应的节点中的文字信息
		for (Element temp_ele : elements) {
			listResult.add(temp_ele.getStringValue());
		}
		return listResult;
	}
	
	public static void main(String[] args) {
		 try {
			parseXML ();
		} catch (DocumentException e) {
		
			e.printStackTrace();
		}
	}
}
