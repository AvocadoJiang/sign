package com.xyt.entity.request;


import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="loginReq",description="登录对象")
@Data
public class LoginReq {
	@ApiModelProperty(value="学工号",example="15401190126")
	@NotBlank
	private String user_number;	
	
	@ApiModelProperty(value="用户密码,在前段用32位MD5加密",example="e10adc3949ba59abbe56e057f20f883e")
	@NotBlank
	private String password;	
	
}
