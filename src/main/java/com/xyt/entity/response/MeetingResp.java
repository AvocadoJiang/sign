package com.xyt.entity.response;

import java.util.Date;

import com.xyt.entity.Meeting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="meetingResp",description="会议对象")
@Data
public class MeetingResp {
	
	@ApiModelProperty(value="会议主键")
	private String meeting_id;
	
	@ApiModelProperty(value="会议名称")
	private String meeting_name;
	
	@ApiModelProperty(value="会议开始时间,时间戳格式字符串")
	private Date start_time;
	
	@ApiModelProperty(value="会议地点")
	private String address;
	
	@ApiModelProperty(value="主讲人")
	private String speaker;
	
	@ApiModelProperty(value="主办方")
	private String organizer;
	
	@ApiModelProperty(value="班级名称")
	private String class_name;
	
	@ApiModelProperty(value="参会人数")
	private int student_number;
	
	@ApiModelProperty(value="请假人数")
	private int leave_number;
	
	public MeetingResp(Meeting entity) {
		this.meeting_id = entity.getMeetingID();
		this.meeting_name = entity.getMeetingName();
		this.start_time = entity.getStartTime();
		this.address = entity.getAddress();
		this.speaker = entity.getSpeaker();
		this.organizer = entity.getOrganizer();
		this.class_name = entity.getClassName();
		this.student_number = entity.getStudentNumber();
		this.leave_number = entity.getLeaveNumber();
	}
}
