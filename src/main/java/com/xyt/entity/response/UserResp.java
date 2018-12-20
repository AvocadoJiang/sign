package com.xyt.entity.response;


import java.io.Serializable;
import java.util.Date;

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
	
	@ApiModelProperty(value="用户学院外键主键")
	private String academy_id;
	
	@ApiModelProperty(value="手机号")
	private String phone;
	
	@ApiModelProperty(value="用户身份，取值范围{'STUDENT','TEACHER','ADMIN','ROOT'}")
	private String identity;
	
	@ApiModelProperty(value="用户头像URL")
	private String image;

	@ApiModelProperty(value="入职日期")
	private Date create_time;
	
	@ApiModelProperty(value="职称")
	private String title;
	
	@ApiModelProperty(value="性别")
	private String gender;
	
	@ApiModelProperty(value="银行卡号")
	private String card_number;
	
	@ApiModelProperty(value="学历")
	private String education;
	
	@ApiModelProperty(value="出生日期")
	private Date birthday;
	
	@ApiModelProperty(value="寝室号")
	private String bedroom_number;
	
	@ApiModelProperty(value="民族")
	private String ethnic;
	
	@ApiModelProperty(value="生源地")
	private String hometown;
	
	@ApiModelProperty(value="政治面貌")
	private String political_status;
	
	@ApiModelProperty(value="状态")
	private String isdelete;
	
	public UserResp(User user) {
		super();
		this.user_id = user.getUserID();
		this.user_number = user.getUserNumber();
		this.user_name = user.getUserName();
		this.identity = user.getIdentity();
		this.image = user.getImage();
		
		this.phone = user.getPhone();
		if(identity.equalsIgnoreCase(User.USER_IDENTITY.STUDENT.name())) {
			this.class_name = user.getC().getClassName();
			this.academy_name = user.getC().getMajor().getAcademy().getAcademyName();
			this.academy_id = user.getC().getMajor().getAcademy().getAcademyID();
		}else if(identity.equalsIgnoreCase(User.USER_IDENTITY.TEACHER.name())||identity.equalsIgnoreCase(User.USER_IDENTITY.ADMIN.name())) {
			this.academy_name = user.getAcademy().getAcademyName();
			this.academy_id = user.getAcademy().getAcademyID();
			this.class_name = "";
		}else if(identity.equalsIgnoreCase(User.USER_IDENTITY.ROOT.name())) {
			this.academy_name = "";
			this.class_name = "";
		}
		
		this.create_time = user.getCreateTime();
		this.title = user.getTitle();
		this.card_number = user.getCardNumber();
		this.education = user.getEducation();
		this.gender = user.getGender();
		this.birthday = user.getBirthday();
		this.bedroom_number = user.getBedroom_number();
		this.ethnic = user.getEthnic();
		this.hometown = user.getHometown();
		this.political_status = user.getPolitical_status();
		this.isdelete = user.getIsdelete();
		 
	}
	
	public UserResp() {
		super();
	}
	
	
}
