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

import com.xyt.entity.Grade;
import com.xyt.entity.request.GradeReq;
import com.xyt.entity.response.GradeResp;
import com.xyt.service.reactive.GradeReactive;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Api(tags = "年级相关接口")
@RestController
@RequestMapping("/grade")
//@Slf4j
public class GradeController {
	private GradeReactive gradeReactive;
	
	/**
	 * 构造函数 构造器注入
	 * @param gradeRepository
	 */
	public GradeController(GradeReactive gradeReactive) {
		super();
		this.gradeReactive = gradeReactive;
		
	}
	
	@ApiOperation(value = "获取全部年级" ,  notes="获取全部年级,以数组形式一次性返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = GradeResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/all")
	public Flux<GradeResp> getAll(){		
		return gradeReactive.findAll().map(entity->new GradeResp(entity));
	}
	
	@ApiOperation(value = "获取全部年级" ,  notes="获取全部年级,以SSE形式多次返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = GradeResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping(value="/stream/all",produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<GradeResp> streamGetAll(){
		return gradeReactive.findAll().map(entity->new GradeResp(entity));
	}
	
	@ApiOperation(value = "新增年级" ,  notes="上传必要的年级信息来创建一个新的年级")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = GradeResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PostMapping("/add")
	public Mono<GradeResp> add(@ApiParam(value="以json格式放入Request Body中",required=true) @Valid @RequestBody GradeReq gradeReq) {
		Grade grade = new Grade(gradeReq);
		GradeCheck(grade);
		return gradeReactive.save(grade).map(entity->new GradeResp(entity));
	}
	
	@ApiOperation(value = "删除年级" ,  notes="根据用户的grade_id来删除一个年级")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "grade_id", value = "被操作的目标主键,直接放入地址中,替换{grade_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@DeleteMapping("/{grade_id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable("grade_id")String grade_id){
		return gradeReactive.findById(grade_id)
				.flatMap(entity->gradeReactive.delete(entity)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "更新年级信息" ,  notes="通过grade_id定位用户并更新其信息")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path", name = "grade_id", value = "被操作的目标主键,直接放入地址中,替换{grade_id}", required = true)})
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = GradeResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PutMapping("/{grade_id}")
	public Mono<ResponseEntity<GradeResp>> update(@PathVariable("grade_id")String grade_id,
			@ApiParam(value="需要更新的年级信息以json格式放入requestbody中",required=true) @RequestBody GradeReq gradeReq){
		Grade grade = new Grade(gradeReq);
		return gradeReactive.findById(grade_id)
				.flatMap(entity->{
					if(StringUtils.isNotBlank(grade.getGradeName())) {
						entity.setGradeName(grade.getGradeName());
					}
					GradeCheck(entity);
					return gradeReactive.save(entity);
				})
				.map(entity->new ResponseEntity<GradeResp>(new GradeResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "根据主键查找年级" ,  notes="根据用户grade_id查找年级")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "grade_id", value = "被操作的目标主键,直接放入地址中,替换{grade_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = GradeResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/{grade_id}")
	public  Mono<ResponseEntity<GradeResp>> findByID(@PathVariable("grade_id")String grade_id){
		return gradeReactive.findById(grade_id)
				.map(entity->new ResponseEntity<GradeResp>(new GradeResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	private void GradeCheck(Grade grade) {
		
	}
}
