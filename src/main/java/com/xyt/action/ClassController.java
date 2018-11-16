package com.xyt.action;

import java.util.stream.Stream;

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

import com.xyt.entity.Class;
import com.xyt.entity.request.ClassReq;
import com.xyt.entity.response.ClassResp;
import com.xyt.advice.exceptions.CheckException;
import com.xyt.globle.Constants;
import com.xyt.service.AipFaceService;
import com.xyt.service.reactive.ClassReactive;
import com.xyt.service.repository.MajorRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Api(tags = "班级相关接口")
@RestController
@RequestMapping("/class")
public class ClassController {
	private ClassReactive classReactive ;
	private MajorRepository majorRepository ;
	private AipFaceService aipFaceService;
	/**
	 * 构造函数 构造器注入
	 * @param academyRepository
	 */
	public ClassController(ClassReactive classReactive,MajorRepository majorRepository,AipFaceService aipFaceService) {
		super();
		this.classReactive = classReactive;
		this.majorRepository = majorRepository;
		this.aipFaceService = aipFaceService;
	}
	
	@ApiOperation(value = "获取全部班级" ,  notes="获取全部班级,以数组形式一次性返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = ClassResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/all")
	public Flux<ClassResp> getAll(){		
		return classReactive.findAll().map(entity->new ClassResp(entity));
	}
	
	@ApiOperation(value = "获取全部班级" ,  notes="获取全部班级,以SSE形式多次返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = ClassResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping(value="/stream/all",produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ClassResp> streamGetAll(){
		return classReactive.findAll().map(entity->new ClassResp(entity));
	}
	
	@ApiOperation(value = "新增班级" ,  notes="上传必要的班级信息来创建一个新的班级")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = ClassResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PostMapping("/add")
	public Mono<ClassResp> add(@ApiParam(value="以json格式放入Request Body中",required=true) @Valid @RequestBody ClassReq classReq) {
		Class c = new Class(classReq);
		ClassCheck(c);
		return classReactive.save(c).map(entity->{ 
				aipFaceService.GroupAdd(c.getClassID());
				return new ClassResp(entity);
			});
	}
	
	@ApiOperation(value = "删除班级" ,  notes="根据用户的class_id来删除一个班级")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "class_id", value = "被操作的目标主键,直接放入地址中,替换{class_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@DeleteMapping("/{class_id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable("class_id")String class_id){
		return classReactive.findById(class_id)
				
				.flatMap(entity->{
					aipFaceService.GroupDelete(entity.getClassID());
					return classReactive.delete(entity)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)));})
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "更新班级信息" ,  notes="通过class_id定位用户并更新其信息")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "class_id", value = "被操作的目标主键,直接放入地址中,替换{class_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = ClassResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PutMapping("/{class_id}")
	public Mono<ResponseEntity<ClassResp>> update(@PathVariable("class_id")String class_id,
			@ApiParam(value="以json格式放入Request Body中",required=true) @RequestBody ClassReq classReq){
		Class c = new Class(classReq);
		return classReactive.findById(class_id)
				.flatMap(entity->{
					if(StringUtils.isNotBlank(c.getClassName())) {
						entity.setClassName(c.getClassName());
					}
					if(StringUtils.isNotBlank(c.getGrade())) {
						entity.setGrade(c.getGrade());
					}
					if(StringUtils.isNotBlank(c.getMajorID())) {
						entity.setMajorID(c.getMajorID());
					}
					
					ClassCheck(entity);
					return classReactive.save(entity);
				})
				.map(entity->new ResponseEntity<ClassResp>(new ClassResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "根据主键查找班级" ,  notes="根据用户class_id查找班级")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path", name = "class_id", value = "被操作的目标主键,直接放入地址中,替换{class_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = ClassResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/{class_id}")
	public  Mono<ResponseEntity<ClassResp>> findByID(@PathVariable("class_id")String class_id){
		return classReactive.findById(class_id)
				.map(entity->new ResponseEntity<ClassResp>(new ClassResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	
	private void ClassCheck(Class c) {
		
		//majorID
		if(!majorRepository.existsById(c.getMajorID())) {
			throw new CheckException("majorID",Constants.REFERENTIAL_INTEGRITY_CHECK_FAILED);
		}
		
		//grade
		if(Stream.of(Constants.VALID_GRADE).noneMatch(grade->grade.equalsIgnoreCase(c.getGrade()))) {
			throw new CheckException("grade",Constants.REFERENTIAL_INTEGRITY_CHECK_FAILED);	
		}
		c.setMajor(majorRepository.findById(c.getMajorID()).get());
	}
	
}
