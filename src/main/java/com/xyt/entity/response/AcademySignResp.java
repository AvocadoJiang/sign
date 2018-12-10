package com.xyt.entity.response;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="academySignResp",description="学院签到情况对象")
@Data
public class AcademySignResp {
	
	@ApiModelProperty(value="到课率")
	private float rate_of_attendance;
	
	@ApiModelProperty(value="早退率")
	private float rate_of_leave_early;
	
	@ApiModelProperty(value="请假率")
	private float rate_of_ask_for_leave;
	
	@ApiModelProperty(value="学院总人数")
	private int student_number;
	
	@ApiModelProperty(value="到课率最高")
	private String rate_of_attendance_highest_class_name;
	
	@ApiModelProperty(value="到课率最低")
	private String rate_of_attendance_lowest_class_name;
	
	@ApiModelProperty(value="班级签到情况列表")
	private List<ClassSignResp> class_list;
}
