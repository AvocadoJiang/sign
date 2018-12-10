package com.xyt.entity.request;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="classSignReq",description="班级签到情况对象")
@Data
public class SignReq {
	
	@ApiModelProperty(value="统计开始时间",example="2018-12-1 0:0")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date start_time = new Date(0l);
	
	@ApiModelProperty(value="统计结束时间",example="2018-12-8 0:0")
	@DateTimeFormat(pattern="yyyy-MM-dd") 
	private Date end_time = new Date(1607757184000l);
}
