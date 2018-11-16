package com.xyt.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.xyt.entity.request.LoginReq;
import com.xyt.entity.request.UserReq;

import lombok.Data;

@Document(collection="user")
@Data
public class User {
	
	@Id
	private String userID;
	
	@Indexed(unique=true)
	private String userNumber;
	
	private String userName;
	
	private String password;
	
	private String classID;
	
	private String academyID;
	
	private String identity;
	
	private String image;
	
	@DBRef
	private Class c;
	
	@DBRef
	private Academy academy;
	
	/** 用户账户身份 **/
	public static enum USER_IDENTITY
	{
		/** 学生账号 **/
		STUDENT,
		/** 教师账号 **/
		TEACHER,
		/** 学院管理账号 **/
		ADMIN,
		/** 学校管理账号 **/
		ROOT
	}
	
	/**
	 * 获取用户身份枚举参数名称
	 * @param utype
	 * 		用户类型请求值
	 * @return
	 * 		枚举变量
	 */
	public static USER_IDENTITY getUserTypeEnum(String utype)
	{
		return USER_IDENTITY.values()[USER_IDENTITY.valueOf(utype.toUpperCase().trim()).ordinal()];
	}

	public User(UserReq userReq) {
		super();
		this.userNumber = userReq.getUser_number();
		this.userName = userReq.getUser_name();
		this.password = userReq.getPassword();
		this.classID = userReq.getClass_id();
		this.academyID = userReq.getAcademy_id();
		this.identity = userReq.getIdentity();
		this.image = userReq.getImage();
	}
	
	public User(LoginReq loginReq) {
		super();
		this.userNumber = loginReq.getUser_number();
		this.password = loginReq.getPassword();
	}

	public User() {
		super();
	}
	
	
}
