package com.xyt.entity.request;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="faceAipReq",description="面部识别图片上传")
@Data
public class FaceAipReq {

	@ApiModelProperty(value="照片",example="base64字符串")
	@NotBlank
	private String image;
	
}
