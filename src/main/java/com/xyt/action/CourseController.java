package com.xyt.action;


import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xyt.entity.Course;
import com.xyt.entity.User;
import com.xyt.entity.request.CourseReq;
import com.xyt.entity.response.CourseResp;
import com.xyt.advice.exceptions.CheckException;
import com.xyt.globle.Constants;
import com.xyt.service.reactive.CourseReactive;
import com.xyt.service.repository.ClassRepository;
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

@Api(tags = "课程相关接口")
@RestController
@RequestMapping("/course")
public class CourseController {
	private CourseReactive courseReactive;
	private UserRepository userRepository;
	private ClassRepository classRepository;
	/**
	 * 构造函数 构造器注入
	 * @param academyRepository
	 */
	public CourseController(CourseReactive courseReactive, UserRepository userRepository,ClassRepository classRepository) {
		super();
		this.courseReactive = courseReactive;
		this.userRepository = userRepository;
		this.classRepository = classRepository;
	}
	
	@ApiOperation(value = "获取全部课程" ,  notes="获取全部课程,以数组形式一次性返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = CourseResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/all")
	public Flux<CourseResp> getAll(){		
		return courseReactive.findAll().map(entity->new CourseResp(entity));
	}
	
	
	@ApiOperation(value = "获取全部课程" ,  notes="获取全部课程,以SSE形式多次返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = CourseResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping(value="/stream/all",produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<CourseResp> streamGetAll(){
		return courseReactive.findAll().map(entity->new CourseResp(entity));
	}
	
	@ApiOperation(value = "新增课程" ,  notes="上传必要的课程信息来创建一个新的课程")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = CourseResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PostMapping("/add")
	public Mono<CourseResp> add(@ApiParam(value="新增课程的必要信息,以json格式放入Request Body中",required=true) @Valid @RequestBody CourseReq courseReq) {
		Course course = new Course(courseReq);
		CourseCheck(course);
		return courseReactive.save(course).map(entity->new CourseResp(entity));
	}
	
	

	@ApiOperation(value = "删除课程" ,  notes="根据课程的course_id来删除一个课程")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path", dataType = "String", name = "course_id", value = "被操作的目标主键,直接放入地址中,替换{course_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@DeleteMapping("/{course_id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable("course_id")String course_id){
		return courseReactive.findById(course_id)
				.flatMap(course->courseReactive.delete(course)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "更新课程信息" ,  notes="通过course_id定位课程并更新其信息")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path", dataType = "String", name = "course_id", value = "被操作的目标主键,直接放入地址中,替换{course_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = CourseResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PutMapping("/{course_id}")
	public Mono<ResponseEntity<CourseResp>> update(@PathVariable("course_id")String course_id,
			@ApiParam(value="新增课程的必要信息,以json格式放入requestbody中",required=true) @RequestBody CourseReq courseReq){
		Course course = new Course(courseReq);
		return courseReactive.findById(course_id)
				.flatMap(entity->{
					if(StringUtils.isNotBlank(course.getCourseName())) {
						entity.setCourseName(course.getCourseName());
					}
					if(StringUtils.isNotBlank(course.getTeacherID())) {
						entity.setTeacherID(course.getTeacherID());
					}
					if(StringUtils.isNotBlank(course.getAddress())) {
						entity.setAddress(course.getAddress());
					}
					if(StringUtils.isNotBlank(course.getRemark())) {
						entity.setRemark(course.getRemark());
					}
					CourseCheck(entity);
					return courseReactive.save(entity);
				})
				.map(entity->new ResponseEntity<CourseResp>(new CourseResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "根据主键查找课程" ,  notes="根据用户course_id查找课程")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path", name = "course_id", value = "被操作的目标主键,直接放入地址中,替换{course_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = CourseResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/{course_id}")
	public  Mono<ResponseEntity<CourseResp>> findByID(@PathVariable("course_id")String course_id){
		return courseReactive.findById(course_id)
				.map(entity->new ResponseEntity<CourseResp>(new CourseResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "根据教师主键查找课程" ,  notes="根据用户teacher_id查找课程")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path", name = "teacher_id", value = "被操作的目标主键,直接放入地址中,替换{teacher_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = CourseResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/teacher_id/{teacher_id}")
	public  Flux<CourseResp> findByteacherID(@PathVariable("teacher_id")String teacher_id){
		return courseReactive.findByteacherID(teacher_id)
				.map(entity->new CourseResp(entity));
	}
	
	private void CourseCheck(Course course) {
		if(!userRepository.findById(course.getTeacherID()).get().getIdentity().equalsIgnoreCase(User.USER_IDENTITY.TEACHER.name())) {
			throw new CheckException("teacherID",Constants.REFERENTIAL_INTEGRITY_CHECK_FAILED);
		}
		course.setTeacher(userRepository.findById(course.getTeacherID()).get());
		if(!classRepository.existsById(course.getClassID())){
			throw new CheckException("classID",Constants.REFERENTIAL_INTEGRITY_CHECK_FAILED);
		}
		course.setC(classRepository.findById(course.getClassID()).get());
		
	}
	
}
