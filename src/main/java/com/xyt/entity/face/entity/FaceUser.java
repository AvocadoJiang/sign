package com.xyt.entity.face.entity;

import lombok.Data;

/**
 * 人脸搜索用户类
 * @author Jason Chiang
 *
 */
@Data
public class FaceUser {
	
	/** 搜索相似度 **/
	private double score;
	
	/** 用户组别标识 **/
	private String group_id;
	
	/** 用户标识 **/
	private String user_id;
	
	/** 用户信息 **/
	private String user_info;
}
