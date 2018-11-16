package com.xyt.advice;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.mongodb.DuplicateKeyException;
import com.xyt.advice.exceptions.CheckException;

/**
 * 异常处理切面
 * @author Yanira
 *
 */
@ControllerAdvice
public class CheckAdvice {

	@ExceptionHandler(CheckException.class)
	public ResponseEntity<String> handleBindException(CheckException e){
		return new ResponseEntity<String>("错误的字段:"+e.getLocation()+"错误的原因:"+e.getReason(),HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(WebExchangeBindException.class)
	public ResponseEntity<String> handleBindException(WebExchangeBindException e){
		return new ResponseEntity<String>(e.getFieldErrors().stream().map(ex->ex.getField()+":"+ex.getDefaultMessage())
				.reduce("",(s1,s2)->s1+"\n"+s2),HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * 用户身份范围异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleBindException(IllegalArgumentException e){
		return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * 关键字重复异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<String> handleBindException(DuplicateKeyException e){
		return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
	}
	
	

	
	
}
