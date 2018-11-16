package com.xyt.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.xyt.entity.request.MajorReq;

import lombok.Data;

@Document(collection="major")
@Data
public class Major {
	@Id
	private String majorID;
	
	private String majorName;
	
	private String academyID;
	
	@DBRef
	private Academy academy;

	public Major(MajorReq majorReq) {
		super();
		this.majorName = majorReq.getMajor_name();
		this.academyID = majorReq.getAcademy_id();
	}

	public Major() {
		super();
	}
	
	
}
