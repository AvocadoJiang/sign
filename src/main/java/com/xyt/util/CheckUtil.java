package com.xyt.util;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.xyt.advice.exceptions.CheckException;


public class CheckUtil {

	private final static String[] INVALID_NAMES= {"admin","root"};
	
	/**
	 * 校验名字，不成功抛出校验异常
	 * @param name
	 */
	public static void checkFiled(String value) {
		Stream.of(INVALID_NAMES).filter(name->name.equalsIgnoreCase(value))
		.findAny().ifPresent(name->{
			throw new CheckException("name",value);
		});
	}
	
	/**
	 * 正则校验是否是Base64编码字符串
	 * @param str
	 * @return
	 */
	public static boolean isBase64(String str) {
		//Base64字符串初始化，去掉data:image/png;base64,
		if(str.split(":")[0].equals("data")) {
			str = str.split("base64,")[1];
		}
	    String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
	    return Pattern.matches(base64Pattern, str);
	}


}
