package com.xyt.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import net.sf.ezmorph.object.AbstractObjectMorpher;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.JSONUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;


public class JSONUtil {
	/**
	 * 注册日期型字符串转java
	 */
	private static void registerDate2Java() {
		JSONUtils.getMorpherRegistry()
				.registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd HH:mm"}));
	}

	/**
	 * java类转换json
	 * @param object
	 * 		Object
	 * @return
	 * 		String
	 */
	public static String beanToJson(Object object) {
		String jsonString = null;
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		// 日期转换方式, 输出yyyy-MM-dd字符串
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {

			@Override
			public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
				return processValue(value);
			}

			@Override
			public Object processArrayValue(Object value, JsonConfig jsonConfig) {
				return processValue(value);
			}

			private Object processValue(Object value) {
				if (value != null) {
					// 格式化日期
					return DateFormatUtils.format((Date) value, "yyyy-MM-dd");
				}
				return "";
			}
		});
		if (object != null) {
			if (object instanceof Collection || object instanceof Object[]) {
				// 集合转换
				jsonString = JSONArray.fromObject(object, jsonConfig).toString();
			} else {
				// 对象转换
				jsonString = JSONObject.fromObject(object, jsonConfig).toString();
			}
		}
		return jsonString == null ? "{}" : jsonString;
	}
	
	/**
	 * 集合转json
	 * @param coll
	 * 		待转换的集合
	 * @param T
	 * 		转换的集合元素类型
	 * @return
	 * 		JSON对象
	 */
	public static <T> String listToJson(Collection<T> coll) {
		return beanToJson(coll);
	}

	/**
	 * 数组转json
	 * @param objects
	 * 		Object[]
	 * @return
	 * 		String
	 */
	public static String arrayToJson(Object[] objects) {
		return beanToJson(objects);
	}

	/**
	 * json转java类
	 * @param jsonString
	 * 		String
	 * @param beanClass
	 * 		Class<T>
	 * @param T
	 * 		JSON对象待转换成的对象类型
	 * @return
	 * 		Object
	 */
	public static <T> Object jsonToBean(String jsonString, Class<T> beanClass) {
		registerDate2Java();
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		return JSONObject.toBean(jsonObject, beanClass);
	}

	/**
	 * json转java类集合
	 * @param jsonString
	 * 		String
	 * @param beanClass
	 * 		Class<T>
	 * @param T
	 * 		JSON对象待转换成的对象类型
	 * @return
	 * 		List<T>
	 */
	public static <T> List<T> jsonToList(String jsonString, Class<T> beanClass) {
		registerDate2Java();
		JSONArray array = JSONArray.fromObject(jsonString);
		return (List<T>) JSONArray.toCollection(array, beanClass);
	}

	/**
	 * json - java 的转换
	 * 处理Timestamp转化异常
	 * @param jsonObject
	 * 		JSONObject
	 * @param clazz
	 * 		Class
	 * @return
	 * 		Object
	 */
	public static Object toBean(JSONObject jsonObject, Class clazz) {
		JSONUtils.getMorpherRegistry().registerMorpher(new AbstractObjectMorpher() {

			@Override
			public Class morphsTo() {
				return Date.class;
			}

			@Override
			public boolean supports(Class clazz) {
				return String.class.isAssignableFrom(clazz);
			}

			@Override
			public Object morph(Object value) {
				if (value == null || StringUtils.isBlank(value.toString())) {
					return null;
				}

				if (Date.class.isAssignableFrom(value.getClass())) {
					return value;
				}

				if (!supports(value.getClass())) {
					try {
						throw new Exception(value.getClass() + " is not supported");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				String strValue = (String) value;

				SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
				try {
					return new Date(dateParser.parse(strValue.toLowerCase()).getTime());
				} catch (ParseException e) {
					return null;
				}
			}
		});
		return JSONObject.toBean(jsonObject, clazz);
	}
}
