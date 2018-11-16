package com.xyt.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.xyt.entity.request.OrderReq;

import lombok.Data;

@Document(collection="order")
@Data
public class Order {
	
	@Id
	private String orderID;
	
	private String studentID;
	
	private String courseID;

	@DBRef
	private User student;
	
	@DBRef
	private Course course;
	
	public Order(OrderReq orderReq) {
		super();
		this.studentID = orderReq.getStudent_id();
		this.courseID = orderReq.getCourse_id();
	}

	public Order() {
		super();
	}
	
	
}
