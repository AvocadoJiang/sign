package com.xyt.entity.request;



import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="orderReq",description="学生选课对象")
@Data
public class OrderReq {
	
	@ApiModelProperty(value="选课所属学生的主键,user的user_id")
	@NotBlank
	private String student_id;
	
	@ApiModelProperty(value="选课所属课程的主键,course的course_id")
	@NotBlank
	private String course_id;
}
