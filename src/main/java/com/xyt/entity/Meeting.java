package com.xyt.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.xyt.entity.request.MeetingReq;

import lombok.Data;

@Document(collection="meeting")
@Data
public class Meeting {
	@Id
	private String meetingID;
	
	private Date startTime;
	
	private String address;
	
	private String meetingName;
	
	private String speaker;
	
	private String organizer;
	
	private String className;
	
	private int studentNumber;
	
	private int leaveNumber;
	
	public Meeting() {
		super();
	}
	
	public Meeting(MeetingReq req) {
		this.startTime = req.getStart_time();
		this.address = req.getAddress();
		this.meetingName = req.getMeeting_name();
		this.speaker = req.getSpeaker();
		this.organizer = req.getOrganizer();
		
		this.className = req.getClass_name();
		
		this.leaveNumber = req.getLeave_number();
		
		if(req.getStudent_number()!=0) {
			this.studentNumber = req.getStudent_number();
		}else {
			this.studentNumber = 198;
		}
		
	}

}
