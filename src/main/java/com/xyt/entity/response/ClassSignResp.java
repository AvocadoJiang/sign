package com.xyt.entity.response;

import java.util.List;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="classSignResp",description="班级签到情况对象")
@Data
public class ClassSignResp implements Comparable<ClassSignResp>{
	@ApiModelProperty(value="班级名称")
	private String class_name;
	
	@ApiModelProperty(value="到课率")
	private float rate_of_attendance;
	
	@ApiModelProperty(value="早退率")
	private float rate_of_leave_early;
	
	@ApiModelProperty(value="请假率")
	private float rate_of_ask_for_leave;
	
	@ApiModelProperty(value="班级总人数")
	private int student_number;
	
	@ApiModelProperty(value="课程签到情况列表")
	private List<CourseSignResp> course_list;

	@Override
	public int compareTo(ClassSignResp o) {
		if(this.rate_of_attendance>o.getRate_of_attendance()) {
			return 1;
		}else {
			return -1;
		}
		
	}
	
	
}
