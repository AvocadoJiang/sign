package com.xyt.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.xyt.entity.request.CourseReq;

import lombok.Data;

@Document(collection="course")
@Data
public class Course {
	@Id
	private String courseID;

	private String courseName;
	
	private String teacherID;
	
	private String address;
	
	private String remark;
	
	private String classID;
	
	
	@DBRef
	private User teacher;
	
	@DBRef
	private Class c;

	public Course(CourseReq courseReq) {
		super();
		this.courseName = courseReq.getCourse_name();
		this.teacherID = courseReq.getTeacher_id();
		this.address = courseReq.getAddress();
		this.remark = courseReq.getRemark();
		this.classID =courseReq.getClass_id();
	}

	public Course() {
		super();
	}
	
	
}