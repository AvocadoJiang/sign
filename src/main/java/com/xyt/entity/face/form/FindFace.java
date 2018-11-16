package com.xyt.entity.face.form;


import com.xyt.entity.face.entity.FindFaceResult;

import lombok.Data;

/**
 * 百度API人脸搜索接口JSON接收类
 * @author Jason Chiang
 *
 */
@Data
public class FindFace {
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
	
	/** 结果信息 **/
	private FindFaceResult result;
}
