package com.xyt.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.xyt.entity.request.LessonReq;

import lombok.Data;

@Document(collection="lesson")
@Data
public class Lesson {
	@Id
	private String lessonID;
	
	private String courseID;
	
	private Date startTime;
	
	private Date endTime;
	
	@DBRef
	private Course course;
	
	public Lesson(LessonReq lessonReq) {
		super();
		this.courseID = lessonReq.getCourse_id();
		this.startTime = lessonReq.getStart_time();
		this.endTime = lessonReq.getEnd_time();
	}

	public Lesson() {
		super();
	}
	
	
}
