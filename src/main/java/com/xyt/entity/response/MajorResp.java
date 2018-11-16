package com.xyt.entity.response;


import java.io.Serializable;

import com.xyt.entity.Major;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="major",description="专业对象")
@Data
public class MajorResp implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="专业主键")
	private String major_id;
		
	@ApiModelProperty(value="专业名称")
	private String major_name;
	
	@ApiModelProperty(value="专业所属学院名称")
	private String academy_name;

	public MajorResp(Major major) {
		this.major_id = major.getMajorID();
		this.major_name = major.getMajorName();
		this.academy_name = major.getAcademy().getAcademyName();
	}
	
	
}
