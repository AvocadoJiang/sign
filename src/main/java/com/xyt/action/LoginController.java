package com.xyt.action;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xyt.advice.exceptions.CheckException;
import com.xyt.entity.User;
import com.xyt.entity.User.USER_IDENTITY;
import com.xyt.entity.request.LoginReq;
import com.xyt.entity.response.AcademyResp;
import com.xyt.entity.response.UserResp;
import com.xyt.globle.Constants;
import com.xyt.service.repository.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Mono;

@Api(tags = "登录相关接口")
@RestController
@RequestMapping("/login")
public class LoginController {
	private UserRepository userRepository;
	public LoginController(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	@ApiOperation(value = "登录接口" ,  notes="上传学工号和密码进行登录")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = UserResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 404, message = "不存在的账号"),
        @ApiResponse(code = 401, message = "密码错误")})
	@PostMapping("/dologin")
	public Mono<ResponseEntity<UserResp>> doLogin(@ApiParam(value="以json格式放入Request Body中",required=true) @Valid @RequestBody LoginReq loginReq,HttpSession session){
		User youruser = new User(loginReq);
		User myuser = userRepository.findByUserNumber(youruser.getUserNumber());
		System.out.println(myuser);
		if(myuser==null) {
			//不存在的学工号
			return Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND));
		}
		if(!youruser.getPassword().equals(myuser.getPassword())) {
			//密码错误
			return Mono.just(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
		}
		
		
		UserResp userResp= new UserResp(myuser);
		session.setAttribute("academy_name", userResp.getAcademy_name());
		session.setAttribute("identity", userResp.getIdentity());
		session.setAttribute("class_name", userResp.getClass_name());
		session.setAttribute("user_id", userResp.getUser_id());
		session.setAttribute("user_name", userResp.getUser_name());
		session.setAttribute("user_number", userResp.getUser_number());
		return Mono.just(new ResponseEntity<UserResp>(userResp,HttpStatus.OK));
	}
	
	@ApiOperation(value = "Session信息获取接口" ,  notes="获取登录状态")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = UserResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PostMapping("/session")
	public Mono<ResponseEntity<UserResp>> getSession(HttpSession session){
		UserResp userResp= new UserResp();
		if(session.getAttribute("identity")==null) {
			return Mono.just(new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED));
		}
		
		userResp.setAcademy_name(session.getAttribute("academy_name").toString());
		userResp.setIdentity(session.getAttribute("identity").toString());
		userResp.setClass_name(session.getAttribute("class_name").toString());
		userResp.setUser_id(session.getAttribute("user_id").toString());
		userResp.setUser_name(session.getAttribute("user_name").toString());
		userResp.setUser_number(session.getAttribute("user_number").toString());
		return Mono.just(new ResponseEntity<UserResp>(userResp,HttpStatus.OK));
	}
	
	
	
	@ApiOperation(value = "登出接口" ,  notes="清除登录状态")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = UserResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PostMapping("/logout")
	public Mono<ResponseEntity<Object>> doLogout(HttpSession session){
		
		if(session.getAttribute("identity")==null) {
			return Mono.just(new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED));
		}
		session.setAttribute("academy_name",null);
		session.setAttribute("identity", null);
		session.setAttribute("class_name", null);
		session.setAttribute("user_id", null);
		session.setAttribute("user_name", null);
		session.setAttribute("user_number", null);
		return Mono.just(new ResponseEntity<>(HttpStatus.OK));
	}
	
	
	
	
}
