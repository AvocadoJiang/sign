package com.xyt.entity.response;

import com.xyt.entity.Grade;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="gradeResp",description="年级对象")
@Data
public class GradeResp {
	
	@ApiModelProperty(value="年级主键")
	private String grade_id;
	
	@ApiModelProperty(value="年级名称")
	private String grade_name;
	
	public GradeResp(Grade grade) {
		super();
		this.grade_id = grade.getGradeID();
		this.grade_name = grade.getGradeName();
	}
}
