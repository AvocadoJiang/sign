package com.xyt.entity.request;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="classReq",description="班级对象")
@Data
public class ClassReq {
	
	@ApiModelProperty(value="班级名称",example="网络152班")
	@NotBlank
	private String class_name;
	
	@ApiModelProperty(value="班级所属专业外键,major的major_id")
	@NotBlank
	private String major_id;
	
	@ApiModelProperty(value="班级所属年级外键,grade的grade_id")
	@NotBlank
	private String grade_id;
	
	
}
