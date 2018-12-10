package com.xyt.entity.request;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="courseReq",description="课程对象")
@Data
public class CourseReq {
	
	@ApiModelProperty(value="课程名称",example="数据结构")
	@NotBlank
	private String course_name;
	
	@ApiModelProperty(value="课程所属教师的外键,user的user_id")
	@NotBlank
	private String teacher_id;
	
	@ApiModelProperty(value="课程所在教室")
	private String address;
	
	@ApiModelProperty(value="课程所属班级的外键,class的class_id")
	@NotBlank
	private String class_id;
	
}
