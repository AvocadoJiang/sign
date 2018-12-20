package com.xyt.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.xyt.entity.request.LoginReq;
import com.xyt.entity.request.StudentReq;
import com.xyt.entity.request.TeacherReq;
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
	
	private String phone;
	
	private Date createTime;
	
	private String title;
	
	private String gender;
	
	private String cardNumber;
	
	private String education;
	
	private Date birthday;
	
	private String idCard;
	
	private String bedroom_number;
	
	private String ethnic;
	
	private String hometown;
	
	private String political_status;
	
	private String isdelete = "false";
	
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
		this.identity = userReq.getIdentity();
		this.image = userReq.getImage();
		this.createTime = userReq.getCreate_time();
		this.cardNumber = userReq.getCard_number();
		this.gender = userReq.getGender();
		this.education = userReq.getEducation();
		this.idCard = userReq.getId_card();
		this.birthday = userReq.getBirthday();
		this.bedroom_number = userReq.getBedroom_number();
		this.ethnic = userReq.getEthnic();
		this.hometown = userReq.getHometown();
		this.political_status = userReq.getPolitical_status();
		this.isdelete = userReq.getIsdelete();
		
		this.academyID = userReq.getAcademy_id();
		this.title = userReq.getTitle();
	}
	
	public User(LoginReq loginReq) {
		super();
		this.userNumber = loginReq.getUser_number();
		this.password = loginReq.getPassword();
	}
	
	public User(TeacherReq userReq) {
		super();
		this.userNumber = userReq.getUser_number();
		this.userName = userReq.getUser_name();
		this.password = userReq.getPassword();
		this.academyID = userReq.getAcademy_id();
		this.identity = USER_IDENTITY.TEACHER.name();
		this.image = userReq.getImage();
		this.createTime = userReq.getCreate_time();
		this.title = userReq.getTitle();
		this.cardNumber = userReq.getCard_number();
		this.gender = userReq.getGender();
		this.education = userReq.getEducation();
		this.idCard = userReq.getId_card();
		this.birthday = userReq.getBirthday();
	}

	public User(StudentReq userReq) {
		super();
		this.userNumber = userReq.getUser_number();
		this.userName = userReq.getUser_name();
		this.password = userReq.getPassword();
		this.classID = userReq.getClass_id();
		this.identity = USER_IDENTITY.STUDENT.name();
		this.image = userReq.getImage();
		this.createTime = userReq.getCreate_time();
		this.cardNumber = userReq.getCard_number();
		this.gender = userReq.getGender();
		this.education = userReq.getEducation();
		this.idCard = userReq.getId_card();
		this.birthday = userReq.getBirthday();
		this.bedroom_number = userReq.getBedroom_number();
		this.ethnic = userReq.getEthnic();
		this.hometown = userReq.getHometown();
		this.political_status = userReq.getPolitical_status();
	}
	
	public User() {
		super();
	}
	
	
}
