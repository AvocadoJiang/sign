package com.xyt.entity.response;


import java.io.Serializable;

import com.xyt.entity.Course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel(value="course",description="课程对象")
@Data
public class CourseResp implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="课程主键")
	private String course_id;
	
	@ApiModelProperty(value="课程名称")
	private String course_name;
	
	@ApiModelProperty(value="课程所属教师的名称")
	private String teacher_name;
	
	@ApiModelProperty(value="课程所属学院的名称")
	private String academy_name;
	
	@ApiModelProperty(value="课程所在教室")
	private String address;

	public CourseResp(Course course) {
		super();
		this.course_id = course.getCourseID();
		this.course_name = course.getCourseName();
		this.teacher_name = course.getTeacher().getUserName();
		this.academy_name = course.getTeacher().getAcademy().getAcademyName();
		this.address = course.getAddress();
	}
	
	
}
