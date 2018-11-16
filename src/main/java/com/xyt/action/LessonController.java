package com.xyt.action;

import javax.validation.Valid;

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

import com.xyt.entity.Lesson;
import com.xyt.entity.request.LessonReq;
import com.xyt.entity.response.LessonResp;
import com.xyt.advice.exceptions.CheckException;
import com.xyt.globle.Constants;
import com.xyt.service.reactive.LessonReactive;
import com.xyt.service.repository.CourseRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Api(tags = "课时相关接口")
@RestController
@RequestMapping("/lesson")
public class LessonController {
	private LessonReactive lessonReactive;
	private CourseRepository courseRepository;
	/**
	 * 构造器注入
	 * @param repository
	 * @param academyRepository
	 */
	public LessonController(LessonReactive lessonReactive, CourseRepository courseRepository) {
		super();
		this.lessonReactive = lessonReactive;
		this.courseRepository = courseRepository;
	}
	
	@ApiOperation(value = "获取全部课时" ,  notes="获取全部课时,以数组形式一次性返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = LessonResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/all")
	public Flux<LessonResp> getAll(){		
		return lessonReactive.findAll().map(entity->new LessonResp(entity));
	}
	
	@ApiOperation(value = "按时间范围获取某一位老师的全部课时" ,  notes="按时间范围和教师主键获取符合条件的课时,以数组形式一次性返回数据")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path", name = "teacher_id", value = "课时的上课教师,直接放入地址中,替换{teacher_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = LessonResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PostMapping("/getByTime/{teacher_id}")
	public Flux<LessonResp> getAllByTime(@PathVariable("teacher_id")String teacher_id,@ApiParam(value="需要更新的课时信息,以json格式放入requestbody中",required=true) @RequestBody LessonReq lessonReq){
		System.out.println(lessonReq);
		return lessonReactive.findByStartTimeBetween(lessonReq.getStart_time(),lessonReq.getEnd_time())
				.filter(entity->entity.getCourse().getTeacherID().equals(teacher_id))
				.map(entity->new LessonResp(entity));
	}
	
	@ApiOperation(value = "获取全部课时" ,  notes="获取全部课时,以SSE形式多次返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = LessonResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping(value="/stream/all",produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<LessonResp> streamGetAll(){
		return lessonReactive.findAll().map(entity->new LessonResp(entity));
	}
	
	
	
	@ApiOperation(value = "新增课时" ,  notes="上传必要的课时信息来创建一个新的课时")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = LessonResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PostMapping("/add")
	public Mono<LessonResp> add(@ApiParam(value="需要更新的课时信息,以json格式放入requestbody中",required=true)  @Valid @RequestBody LessonReq lessonReq) {
		Lesson lesson = new Lesson(lessonReq);
		LessonCheck(lesson);
		return lessonReactive.save(lesson).map(entity->new LessonResp(entity));
	}
	
	@ApiOperation(value = "删除课时" ,  notes="根据课时的lesson_id来删除一个课时")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "lesson_id", value = "被操作的目标主键,直接放入地址中,替换{lesson_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@DeleteMapping("/{lesson_id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable("lesson_id")String lesson_id){
		return lessonReactive.findById(lesson_id)
				.flatMap(lesson->lessonReactive.delete(lesson)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "更新课时信息" ,  notes="通过lesson_id定位课时并更新其信息")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path", name = "lesson_id", value = "被操作的目标主键,直接放入地址中,替换{lesson_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = LessonResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PutMapping("/{lesson_id}")
	public Mono<ResponseEntity<LessonResp>> update(@PathVariable("lesson_id")String lesson_id,
			@ApiParam(value="需要更新的课时信息,以json格式放入requestbody中",required=true) @RequestBody LessonReq lessonReq){
		Lesson lesson = new Lesson(lessonReq);
		return lessonReactive.findById(lesson_id)
				.flatMap(entity->{
					if(lesson.getStartTime()!=null) {
						entity.setStartTime(lesson.getStartTime());
					}
					if(lesson.getEndTime()!=null) {
						entity.setEndTime(lesson.getEndTime());
					}
					LessonCheck(entity);
					return lessonReactive.save(entity);
				})
				.map(entity->new ResponseEntity<LessonResp>(new LessonResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "根据主键查找课时" ,  notes="根据课时lesson_id查找课时")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path", name = "lesson_id", value = "被操作的目标主键,直接放入地址中,替换{lesson_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = LessonResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/{lesson_id}")
	public  Mono<ResponseEntity<LessonResp>> findByID(@PathVariable("lesson_id")String lesson_id){
		return lessonReactive.findById(lesson_id)
				.map(entity->new ResponseEntity<LessonResp>(new LessonResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	private void LessonCheck(Lesson lesson) {
		
		if(!courseRepository.existsById(lesson.getCourseID())) {
			throw new CheckException("courseID",Constants.REFERENTIAL_INTEGRITY_CHECK_FAILED);
		}
		lesson.setCourse(courseRepository.findById(lesson.getCourseID()).get());
		
	}
	
}
