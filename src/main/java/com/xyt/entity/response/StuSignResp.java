package com.xyt.entity.response;



import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.xyt.entity.StuSign;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="stusign",description="学生签到对象")
@Data
public class StuSignResp implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="学生签到主键")
	private String stuSign_id;
	
	@ApiModelProperty(value="学生签到所属学生外键,user的user_id")
	private String student_id;

	@ApiModelProperty(value="学生签到所属课时外键,lesson的lesson_id")
	private String lesson_id;
	
	@ApiModelProperty(value="学生签到所属学生姓名")
	private String student_name;
	
	@ApiModelProperty(value="学生签到所属课程名称")
	private String course_name;
	
	@ApiModelProperty(value="签到状态:NORMAL,LEAVE_EARLY,ASK_FOR_LEAVE",example="NORMAL")
	private String status;

	public StuSignResp(StuSign stuSign) {
		super();
		this.stuSign_id = stuSign.getStusignID();
		this.student_id = stuSign.getStudentID();
		this.lesson_id = stuSign.getLessonID();
		this.student_name = stuSign.getStudent().getUserName();
		this.course_name = stuSign.getLesson().getCourse().getCourseName();
		this.status = stuSign.getStatus();
	}
	
	
	
	
}
