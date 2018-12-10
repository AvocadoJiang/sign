package com.xyt.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.xyt.entity.request.ClassReq;

import lombok.Data;

@Document(collection="class")
@Data
public class Class {
	
	@Id
	private String classID;
	
	private String className;
	
	private String majorID;
	
	private String gradeID;
	
	@DBRef
	private Major major;
	
	@DBRef
	private Grade grade;

	public Class(ClassReq classReq) {
		super();
		this.className = classReq.getClass_name();
		this.gradeID = classReq.getGrade_id();
		this.majorID = classReq.getMajor_id();
	}

	public Class() {
		super();
	}
	
	
	
	
}
