package com.xyt.entity.response;


import java.io.Serializable;

import com.xyt.entity.Academy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="academy",description="学院对象")
@Data
public class AcademyResp implements Serializable{

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="学院主键")
	private String academy_id;
	
	@ApiModelProperty(value="学院名称")
	private String academy_name;
	
	public AcademyResp(Academy academy) {
		super();
		this.academy_id = academy.getAcademyID();
		this.academy_name = academy.getAcademyName();
	}
	
	
}
