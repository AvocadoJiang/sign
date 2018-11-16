package com.xyt.entity.response;


import java.io.Serializable;

import com.xyt.entity.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="user",description="用户对象")
@Data
public class UserResp implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="用户主键")
	private String user_id;
	
	@ApiModelProperty(value="用户学工号")
	private String user_number;
	
	@ApiModelProperty(value="用户姓名")
	private String user_name;
	
	@ApiModelProperty(value="用户班级外键('STUDENT'时不为空)")
	private String class_name;
	
	@ApiModelProperty(value="用户学院外键(用户身份为'ADMIN'或'TEACHER'时不为空)")
	private String academy_name;
	
	@ApiModelProperty(value="用户身份，取值范围{'STUDENT','TEACHER','ADMIN','ROOT'}")
	private String identity;
	
	@ApiModelProperty(value="用户头像URL")
	private String image;

	public UserResp(User user) {
		super();
		this.user_id = user.getUserID();
		this.user_number = user.getUserNumber();
		this.user_name = user.getUserName();
		this.identity = user.getIdentity();
		this.image = user.getImage();
		if(identity.equalsIgnoreCase(User.USER_IDENTITY.STUDENT.name())) {
			this.class_name = user.getC().getClassName();
			this.academy_name = user.getC().getMajor().getAcademy().getAcademyName();
		}else if(identity.equalsIgnoreCase(User.USER_IDENTITY.TEACHER.name())||identity.equalsIgnoreCase(User.USER_IDENTITY.ADMIN.name())) {
			this.academy_name = user.getAcademy().getAcademyName();
			this.class_name = "";
		}else if(identity.equalsIgnoreCase(User.USER_IDENTITY.ROOT.name())) {
			this.academy_name = "";
			this.class_name = "";
		}
	}
	
	public UserResp() {
		super();
	}
	
	
}
