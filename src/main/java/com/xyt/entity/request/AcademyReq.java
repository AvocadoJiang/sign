package com.xyt.entity.request;


import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="academyReq",description="学院对象")
@Data
public class AcademyReq{
	
	@ApiModelProperty(value="学院名称",example="电信学院")
	@NotBlank
	private String academy_name;
}
