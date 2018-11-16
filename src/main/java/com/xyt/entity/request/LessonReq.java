package com.xyt.entity.request;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="lessonReq",description="课时对象")
@Data
public class LessonReq {
	
	@ApiModelProperty(value="课时所属课程的外键,course的course_id")
	@NotBlank
	private String course_id;
	
	@ApiModelProperty(value="课时开始时间,时间戳格式字符串",example="2018-10-7 8:00")
	@DateTimeFormat(pattern="yyyy-MM-dd") 
	@NotNull
	private Date start_time;
	
	@ApiModelProperty(value="课时结束时间,时间戳格式字符串",example="2018-10-7 8:45")
	@DateTimeFormat(pattern="yyyy-MM-dd")  
	@NotNull
	private Date end_time;
}
