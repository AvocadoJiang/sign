package com.xyt.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.xyt.entity.request.StuSignReq;

import lombok.Data;

@Document(collection="stusign")
@Data
public class StuSign {
	
	@Id
	private String stusignID;
	
	private String studentID;
	
	private String lessonID;
	
	@DBRef
	private User student;
	
	@DBRef
	private Lesson lesson;

	public StuSign(StuSignReq stuSignReq) {
		super();
		this.studentID = stuSignReq.getStudent_id();
		this.lessonID = stuSignReq.getLesson_id();
	}

	public StuSign() {
		super();
	}
	
	
}
