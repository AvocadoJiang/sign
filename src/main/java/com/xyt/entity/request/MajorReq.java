package com.xyt.entity.request;


import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="majorReq",description="专业对象")
@Data
public class MajorReq {
	
	@ApiModelProperty(value="专业名称",example="网络工程")
	@NotBlank
	private String major_name;	
	
	@ApiModelProperty(value="专业所属学院外键,academy的academy_id")
	@NotBlank
	private String academy_id;	
}
