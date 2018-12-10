package com.xyt.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.xyt.entity.request.GradeReq;

import lombok.Data;

@Document(collection="grade")
@Data
public class Grade {
	@Id
	private String gradeID;
	
	private String gradeName;
	
	public Grade(GradeReq gradeReq) {
		super();
		this.gradeName = gradeReq.getGrade_name();
	}
	
	public Grade() {
		super();
	}
}
