package com.xyt.entity.face.entity;

import lombok.Data;

/**
 * 人脸注册Result类
 * @author Jason Chiang
 *
 */
@Data
public class UserFaceResult {
	
	/** 每个人脸图片赋予一个唯一的标识 **/
	private String face_token;
	
	/** 人脸在图片中的位置 **/
	private Location location;
	
}
