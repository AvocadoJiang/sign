package com.xyt.action;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xyt.entity.StuSign;
import com.xyt.entity.request.StuSignReq;
import com.xyt.entity.response.StuSignResp;
import com.xyt.advice.exceptions.CheckException;
import com.xyt.globle.Constants;
import com.xyt.service.reactive.StuSignReactive;
import com.xyt.service.repository.LessonRepository;
import com.xyt.service.repository.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Api(tags = "学生签到相关接口")
@RestController
@RequestMapping("/stusign")
public class StuSignController {
	private StuSignReactive stuSignReactive;
	private LessonRepository lessonRepository;
	private UserRepository userRepository;
	/**
	 * 构造器注入
	 * @param repository
	 * @param academyRepository
	 */
	public StuSignController(StuSignReactive stuSignReactive, LessonRepository lessonRepository,
			UserRepository userRepository) {
		super();
		this.stuSignReactive = stuSignReactive;
		this.lessonRepository = lessonRepository;
		this.userRepository = userRepository;
	}
	
	@ApiOperation(value = "获取全部学生签到" ,  notes="获取全部学生签到,以数组形式一次性返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = StuSignResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/all")
	public Flux<StuSignResp> getAll(){		
		return stuSignReactive.findAll().map(entity->new StuSignResp(entity));
	}
	
	@ApiOperation(value = "获取全部学生签到" ,  notes="获取全部学生签到,以数组形式一次性返回数据")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "lesson_id", value = "被操作的目标主键,直接放入地址中,替换{lesson_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = StuSignResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/getByLesson/{lesson_id}")
	public Flux<StuSignResp> getByLesson(@PathVariable("lesson_id")String lesson_id){		
		return stuSignReactive.findBylessonID(lesson_id).map(entity->new StuSignResp(entity));
	}

	@ApiOperation(value = "获取全部学生签到" ,  notes="获取全部学生签到,以SSE形式多次返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = StuSignResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping(value="/stream/all",produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<StuSignResp> streamGetAll(){
		return stuSignReactive.findAll().map(entity->new StuSignResp(entity));
	}
	
	@ApiOperation(value = "新增学生签到" ,  notes="上传必要的学生签到信息来创建一个新的学生签到")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = StuSignResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PostMapping("/add")
	public Mono<StuSignResp> add(@ApiParam(value="需要更新的课时信息,以json格式放入Request Body中",required=true) @Valid @RequestBody StuSignReq stuSignReq) {
		StuSign stuSign = new StuSign(stuSignReq);
		StuSignCheck(stuSign);
		return stuSignReactive.save(stuSign).map(entity->new StuSignResp(entity));
	}
	
	@ApiOperation(value = "删除学生签到" ,  notes="根据学生签到的stusign_id来删除一个学生签到")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "stusign_id", value = "被操作的目标主键,直接放入地址中,替换{stusign_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@DeleteMapping("/{stusign_id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable("stusign_id")String stusign_id){
		return stuSignReactive.findById(stusign_id)
				.flatMap(stuSign->stuSignReactive.delete(stuSign)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	
	@ApiOperation(value = "根据主键查找学生签到" ,  notes="根据学生签到stusign_id查找学生签到")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "stusign_id", value = "被操作的目标主键,直接放入地址中,替换{stusign_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = StuSignResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/{stusign_id}")
	public  Mono<ResponseEntity<StuSignResp>> findByID(@PathVariable("stusign_id")String stusign_id){
		return stuSignReactive.findById(stusign_id)
				.map(entity->new ResponseEntity<StuSignResp>(new StuSignResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	
	
	
	private void StuSignCheck(StuSign stuSign) {
		
		if(!userRepository.existsById(stuSign.getStudentID())) {
			throw new CheckException("studentID",Constants.REFERENTIAL_INTEGRITY_CHECK_FAILED);
		}
		
		if(!lessonRepository.existsById(stuSign.getLessonID())) {
			throw new CheckException("lessonID",Constants.REFERENTIAL_INTEGRITY_CHECK_FAILED);
		}
		
		stuSign.setStudent(userRepository.findById(stuSign.getStudentID()).get());
		stuSign.setLesson(lessonRepository.findById(stuSign.getLessonID()).get());
		
	}
	
}
