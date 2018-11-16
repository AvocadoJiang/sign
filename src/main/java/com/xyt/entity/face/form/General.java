package com.xyt.entity.face.form;


import lombok.Data;

/**
 * 百度API人脸识别接口通用JSON接收类
 * @author Jason Chiang
 *
 */
@Data
public class General {
	/** 日志编码 **/
	private int log_id;
	
	/** 错误信息 **/
	private String error_msg;
	
	/** 不知道是什么东西，官网没写，传过来都是0 **/
	private int cached;
	
	/** 错误编码 **/
	private int error_code;
	
	/** 时间戳 **/
	private int timestamp;
	
	/** 返回结果 **/
	private String result;
}
