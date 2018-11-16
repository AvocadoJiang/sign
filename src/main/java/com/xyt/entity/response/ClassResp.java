package com.xyt.entity.response;




import java.io.Serializable;

import com.xyt.entity.Class;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="class",description="班级对象")
@Data
public class ClassResp implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="班级主键")
	private String class_id;
	
	@ApiModelProperty(value="班级名称")
	private String class_name;
	
	@ApiModelProperty(value="班级年级")
	private String grade;
	
	@ApiModelProperty(value="班级所属专业名称")
	private String major_name;
	
	@ApiModelProperty(value="班级所属学院名称")
	private String academy_name;

	public ClassResp(Class c) {
		super();
		this.class_id = c.getClassID();
		this.class_name = c.getClassName();
		this.grade = c.getGrade();
		this.major_name = c.getMajor().getMajorName();
		this.academy_name = c.getMajor().getAcademy().getAcademyName();
	}
	
	
}
