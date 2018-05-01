package com.jsonparams.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

/**
 * 解析包下的类文件
 * @author ZhangPJ
 *
 */
public class ParsePackageClass {
	/**
	 * 解析包  包含大包  小包
	 * @param element
	 * @return
	 */
	public static Map<String, List<Map<String, Map<String, String>>>> parsePackages(Element element) {
		List<String> filesPath = new ArrayList<String> ();
		//取到所有父节点
		List<Element> ele_packages_parent = element.elements("package-parent");
		//取到每一个父节点
		for (Element temp_ele_package_parent : ele_packages_parent) {
			//获取包的路径
			String ele_package_parent_path = temp_ele_package_parent.attribute("path").getStringValue();
			
			
			//获取子节点
			List<Element> package_child = temp_ele_package_parent.elements("package-child");
			//如果子节点为空，那么只有父节点，负责追加子节点
			if (package_child != null) {
				for (Element tempChild : package_child) {

					StringBuffer sbFileCompletePath = new StringBuffer();
					sbFileCompletePath.append(ele_package_parent_path);
					sbFileCompletePath.append(package_child == null ? 
							"" : tempChild.attribute("path").getStringValue());
					
					filesPath.add(sbFileCompletePath.toString());
				}
			} else {
				//存储所有的文件路径
				filesPath.add(ele_package_parent_path);
			}
		}
		//去路径下取到所有的java文件并形成Map<String, List<Map<String, List<String>>>>
		//	Map<类全路径,属性<属性名, 属性规则<规则>>>
		List<String> filesCompletePath = parseFilesPath(filesPath);
		//将所有的类放到一个大的集合中
		Map<String, List<Map<String, Map<String, String>>>> mapResult = addRuleToMap(filesCompletePath);
		
		
		//获取类节点规则
		for (Element temp_ele_package_parent : ele_packages_parent) {
			//形成单独的路径
			StringBuffer sbPath = new StringBuffer();
			sbPath.append(temp_ele_package_parent.attribute("path").getStringValue());
			Element package_child = temp_ele_package_parent.element("package-child");
			if ( package_child != null) {
				 sbPath.append(package_child.attribute("path").getStringValue());
			}
			//类元素信息 ------>添加规则
			 Element eleClasses = package_child == null ? temp_ele_package_parent.element("classes") :
				 					package_child.element("classes");
			if (eleClasses != null) {
				//得到每一个类
				List<Element> eleClass = eleClasses.elements("class");
			
				if (eleClass != null) {
					for (Element tempEleClass : eleClass) {
						//得到类的全路径
						sbPath.append( tempEleClass.attribute("name").getStringValue());
						//判断大的Map结果集中是否存在
						if (mapResult.containsKey(sbPath.toString())) {
							//存在-----添加属性规则
							Element eleProp = tempEleClass.element("props");
							
							if (eleProp != null) {
								List<Element> ele_values = eleProp.elements("value");
								if (ele_values != null) {
									List<Map<String, Map<String, String>>> propList= new ArrayList<Map<String, Map<String, String>>>();
									for (Element temp_ele_value : ele_values) {
										
										
										Map<String, Map<String, String>> propMap = new HashMap<String, Map<String, String>>();
										
										Map<String, String> rulesMap = new HashMap<String, String>();
										
										String propName = temp_ele_value.attribute("name") != null ?
												temp_ele_value.attribute("name").getStringValue() : null;
										String value = temp_ele_value.attribute("value") !=null ?
												temp_ele_value.attribute("value").getStringValue() : null;
										String ifnull = temp_ele_value.attribute("ifnull") != null ?
												temp_ele_value.attribute("ifnull").getStringValue() : null;
										String randdom = temp_ele_value.attribute("randdom") != null ? 
												temp_ele_value.attribute("randdom").getStringValue() : null;
										String times = temp_ele_value.attribute("times") != null ?
												temp_ele_value.attribute("times").getStringValue() : null;
										
		//								rulesList.put("name", propName);
										rulesMap.put("value", value);
										rulesMap.put("ifnull", ifnull);
										rulesMap.put("randdom", randdom);
										rulesMap.put("times", times);
										
										propMap.put(propName, rulesMap);
										propList.add(propMap);
										
									}
									mapResult.put(sbPath.toString(), propList);
								}
							}
						}
					}
				}
			}
		}
		return mapResult;
	}
	/**
	 * 将类放到Map中并且形成大的Map，形成最终结果集
	 * @param filesCompletePath
	 * @return
	 */
	private static Map<String, List<Map<String, Map<String, String>>>> addRuleToMap(List<String> filesCompletePath) {
		Map<String, List<Map<String, Map<String, String>>>> mapResult = new HashMap<String, List<Map<String,Map<String, String>>>>();
		for (String tempPath : filesCompletePath) {
			mapResult.put(tempPath, null);
		}
		return mapResult;
	}
	/**
	 * 找到java实体类路径
	 * @param filesPath
	 * @return
	 */
	private static List<String> parseFilesPath(List<String> filesPath) {
		List<String> javaClassCompletePath = new ArrayList<String>();
		StringBuffer sbPath = new StringBuffer();
		//得到根目录的绝对路径
		sbPath.append(new File("").getAbsolutePath())
			  .append( File.separator + "src" + File.separator)
			  .append("main" + File.separator)
			  .append("java" + File.separator);
		//遍历集合
		for (String tempFilePath : filesPath) {
			//取到绝对路径下的文件
			File file = new File(sbPath + tempFilePath.replaceAll("\\.", "\\"+File.separator));
			//得到所有的文件
			File[] allFiles = file.listFiles();
			if (allFiles != null) {
				//取到对应的java类文件名称
				for (File tempFile : allFiles) {
					//判断文件是否是java文件
					String fileName = tempFile.getName();
					int lastIndexOf = fileName.lastIndexOf('.');
					String suffixName = fileName.substring(lastIndexOf + 1, fileName.length());
					//是java文件那么加入到集合中
					if("java".equals(suffixName)) {
						javaClassCompletePath.add(tempFilePath + fileName.substring(0,lastIndexOf));
					}
				}
			}
		}	 
		return javaClassCompletePath;
	}
}