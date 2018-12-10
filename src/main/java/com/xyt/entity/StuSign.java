package com.xyt.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.xyt.entity.request.StuSignReq;

import lombok.Data;

@Document(collection="stusign")
@Data
public class StuSign {
	
	@Id
	private String stusignID;
	
	private String studentID;
	
	private String lessonID;
	
	@DBRef
	private User student;
	
	@DBRef
	private Lesson lesson;
	
	private String status;
	
	
	/** 用户账户身份 **/
	public static enum SIGN_STATUS
	{
		/** 正常 **/
		NORMAL,
		/** 早退 **/
		LEAVE_EARLY,
		/** 请假 **/
		ASK_FOR_LEAVE
	}
	
	/**
	 * 获取用户身份枚举参数名称
	 * @param utype
	 * 		用户类型请求值
	 * @return
	 * 		枚举变量
	 */
	public static SIGN_STATUS getUserTypeEnum(String type)
	{
		return SIGN_STATUS.values()[SIGN_STATUS.valueOf(type.toUpperCase().trim()).ordinal()];
	}

	public StuSign(StuSignReq stuSignReq) {
		super();
		this.studentID = stuSignReq.getStudent_id();
		this.lessonID = stuSignReq.getLesson_id();
		this.status = stuSignReq.getStatus();
	}

	public StuSign() {
		super();
	}
	
	
}
