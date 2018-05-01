package com.jsonparams.utils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 解析类与规则
 * @author ZhangPJ
 *
 */
public class ParseClassRules {

	/**
	 * 解析类与规则
	 * @param mapClassRules
	 * @return
	 * @throws ClassNotFoundException 
	 */
	public static String parseClassAndRules(Map<String, List<Map<String, Map<String, String>>>> mapClassRules) throws ClassNotFoundException {
		StringBuffer sbJson = new StringBuffer();
		sbJson.append("{");
		for (Map.Entry<String, List<Map<String, Map<String, String>>>> tempMap : mapClassRules.entrySet()) {
			Class<?> clazz = Class.forName(tempMap.getKey());
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				sbJson.append(field.getName())
					  .append(":");
//					  parseValueType();
				
			}
		}
		
		return null;
	}

}
