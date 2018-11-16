package com.xyt.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.xyt.entity.request.AcademyReq;

import lombok.Data;


@Document(collection="academy")
@Data
public class Academy {
	@Id
	private String academyID;
	
	private String academyName;

	public Academy(AcademyReq academyReq) {
		super();
		this.academyName = academyReq.getAcademy_name();
	}

	public Academy() {
		super();
	}
}
