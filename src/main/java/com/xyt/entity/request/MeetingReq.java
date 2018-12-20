package com.xyt.entity.request;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="meetingReq",description="会议对象")
@Data
public class MeetingReq {
	
	@ApiModelProperty(value="会议名称",example="基于大数据挖掘分享会")
	@NotBlank
	private String meeting_name;
	
	@ApiModelProperty(value="会议开始时间,时间戳格式字符串",example="2018-10-7 8:00")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm") 
	@NotNull
	private Date start_time;
	
	@ApiModelProperty(value="会议地点",example="王伟明报告厅")
	@NotBlank
	private String address;
	
	@ApiModelProperty(value="主讲人",example="王教授")
	@NotBlank
	private String speaker;
	
	@ApiModelProperty(value="主办方",example="电信学院科协")
	@NotBlank
	private String organizer;
	
	@ApiModelProperty(value="班级名称列表,用','分割",example="网络151,网络152")
	@NotBlank
	private String class_name;
	
	@ApiModelProperty(value="参会人数,前端无用,不用管",example="198")
	private int student_number;
	
	@ApiModelProperty(value="请假人数,前端无用,不用管",example="2")
	private int leave_number;
	
	
}
