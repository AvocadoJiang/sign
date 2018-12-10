package com.xyt.entity.request;


import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="gradeReq",description="年级对象")
@Data
public class GradeReq {
	
	@ApiModelProperty(value="年级名称",example="15级")
	@NotBlank
	private String grade_name;
}
