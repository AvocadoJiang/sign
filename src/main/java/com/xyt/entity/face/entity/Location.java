package com.xyt.entity.face.entity;

import lombok.Data;


/**
 * 人脸在图片中的位置
 * @author Jason Chiang
 *
 */
@Data
public class Location {
	
	/** 人脸区域离上边界的距离 **/
	private double top;
	
	/** 人脸区域离左边界的距离 **/
	private double left;
	
	/** 人脸区域的宽度 **/
	private double width;
	
	/** 人脸区域的高度 **/
	private double height;
	
	/** 人脸框相对于竖直方向的顺时针旋转角，[-180,180] **/
	private int rotation;
	
}
