package com.xyt.entity.response;



import com.xyt.entity.Course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="courseSignResp",description="课程签到情况对象")
@Data
public class CourseSignResp {
	@ApiModelProperty(value="课程名称")
	private String course_name;
	
	@ApiModelProperty(value="教师名称")
	private String teacher_name;
	
	@ApiModelProperty(value="课程所在教室")
	private String address;
	
	@ApiModelProperty(value="到课率")
	private float rate_of_attendance;
	
	@ApiModelProperty(value="早退率")
	private float rate_of_leave_early;
	
	@ApiModelProperty(value="请假率")
	private float rate_of_ask_for_leave;
	
	
	public CourseSignResp(Course course) {
		super();
		this.course_name = course.getCourseName();
		this.teacher_name = course.getTeacher().getUserName();
		this.address = course.getAddress();
	}
}
