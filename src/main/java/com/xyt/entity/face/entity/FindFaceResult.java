package com.xyt.entity.face.entity;


import lombok.Data;

/**
 * 人脸搜索Result类
 * @author Jason Chiang
 *
 */
@Data
public class FindFaceResult {
	/** 每个人脸图片赋予一个唯一的标识 **/
	private String face_token;
	
	/** 搜索出来的用户列表 **/
	private FaceUser[] user_list;
}
