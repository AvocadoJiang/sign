package com.xyt.entity.request;


import javax.validation.constraints.NotBlank;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="stusignReq",description="学生签到对象")
@Data
public class StuSignReq {
	
	@ApiModelProperty(value="签到所属学生的主键,user的user_id")
	@NotBlank
	private String student_id;
	
	@ApiModelProperty(value="签到所属课时的主键,lesson的lesson_id")
	@NotBlank
	private String lesson_id;
}
