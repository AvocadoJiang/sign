package com.xyt.entity.request;


import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="userReq",description="用户对象")
@Data
public class UserReq {
	
	@ApiModelProperty(value="用户学工号",example="15401190209")
	@NotBlank
	private String user_number;
	
	@ApiModelProperty(value="用户姓名",example="徐雅婷")
	@NotBlank
	private String user_name;
	
	@ApiModelProperty(value="用户密码,在前段用32位MD5加密",example="e10adc3949ba59abbe56e057f20f883e")
	@NotBlank
	private String password;
	
	@ApiModelProperty(value="用户班级外键('STUDENT'时不为空)")
	private String class_id;
	
	@ApiModelProperty(value="用户学院外键(用户身份为'ADMIN'或'TEACHER'时不为空)")
	private String academy_id;
	
	@ApiModelProperty(value="用户身份,取值范围{'STUDENT','TEACHER','ADMIN','ROOT'}",example="ADMIN")
	@NotBlank
	private String identity;
	
	@ApiModelProperty(value="用户照片，请用Base64编码成字符串")
	private String image;
	
	
}
