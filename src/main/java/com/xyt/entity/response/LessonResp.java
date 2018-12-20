package com.xyt.entity.response;

import java.io.Serializable;
import java.util.Date;

import com.xyt.entity.Lesson;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="lesson",description="课时对象")
@Data
public class LessonResp implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="课时主键")
	private String lesson_id;
	
	@ApiModelProperty(value="课时所属课程名称")
	private String course_name;
	
	@ApiModelProperty(value="课时所属教师名称")
	private String teacher_name;
	
	@ApiModelProperty(value="课时开始时间")
	private Date start_time;
	
	@ApiModelProperty(value="课时结束时间")
	private Date end_time;
	
	@ApiModelProperty(value="课程所在教室")
	private String address;
	
	@ApiModelProperty(value="课时所属班级")
	private String class_id;

	public LessonResp(Lesson lesson) {
		super();
		this.lesson_id = lesson.getLessonID();
		this.course_name = lesson.getCourse().getCourseName();
		this.teacher_name = lesson.getCourse().getTeacher().getUserName();
		this.start_time = lesson.getStartTime();
		this.end_time = lesson.getEndTime();
		this.address = lesson.getCourse().getAddress();
		this.class_id = lesson.getCourse().getClassID();
	}
	
	
}
