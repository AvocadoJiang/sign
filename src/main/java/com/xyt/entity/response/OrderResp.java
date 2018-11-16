package com.xyt.entity.response;



import java.io.Serializable;

import com.xyt.entity.Order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="order",description="学生选课对象")
@Data
public class OrderResp implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="选课主键")
	private String order_id;
	
	@ApiModelProperty(value="选课所属学生外键,user的user_id")
	private String student_id;
	
	@ApiModelProperty(value="选课所属课程外键,course的course_id")
	private String course_id;
	
	@ApiModelProperty(value="选课所属学生姓名")
	private String student_name;
	
	@ApiModelProperty(value="选课所属课程名称")
	private String course_name;
	
	public OrderResp(Order order) {
		super();
		this.order_id = order.getOrderID();
		this.student_id = order.getStudentID();
		this.course_id = order.getCourseID();
		this.student_name = order.getStudent().getUserName();
		this.course_name = order.getCourse().getCourseName();
	}
	
	
}
