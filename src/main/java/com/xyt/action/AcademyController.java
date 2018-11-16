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

import com.xyt.entity.Academy;
import com.xyt.entity.request.AcademyReq;
import com.xyt.entity.response.AcademyResp;
import com.xyt.service.reactive.AcademyReactive;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Api(tags = "学院相关接口")
@RestController
@RequestMapping("/academy")
//@Slf4j
public class AcademyController {
	private AcademyReactive academyReactive;
	
	/**
	 * 构造函数 构造器注入
	 * @param academyRepository
	 */
	public AcademyController(AcademyReactive academyReactive) {
		super();
		this.academyReactive = academyReactive;
		
	}
	
	@ApiOperation(value = "获取全部学院" ,  notes="获取全部学院,以数组形式一次性返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = AcademyResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/all")
	public Flux<AcademyResp> getAll(){		
		return academyReactive.findAll().map(entity->new AcademyResp(entity));
	}
	
	@ApiOperation(value = "获取全部学院" ,  notes="获取全部学院,以SSE形式多次返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = AcademyResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping(value="/stream/all",produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<AcademyResp> streamGetAll(){
		return academyReactive.findAll().map(entity->new AcademyResp(entity));
	}
	
	@ApiOperation(value = "新增学院" ,  notes="上传必要的学院信息来创建一个新的学院")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = AcademyResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PostMapping("/add")
	public Mono<AcademyResp> add(@ApiParam(value="以json格式放入Request Body中",required=true) @Valid @RequestBody AcademyReq academyReq) {
		Academy academy = new Academy(academyReq);
		AcademyCheck(academy);
		return academyReactive.save(academy).map(entity->new AcademyResp(entity));
	}
	
	@ApiOperation(value = "删除学院" ,  notes="根据用户的academy_id来删除一个学院")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "academy_id", value = "被操作的目标主键,直接放入地址中,替换{academy_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@DeleteMapping("/{academy_id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable("academy_id")String academy_id){
		return academyReactive.findById(academy_id)
				.flatMap(entity->academyReactive.delete(entity)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "更新学院信息" ,  notes="通过academy_id定位用户并更新其信息")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path", name = "academy_id", value = "被操作的目标主键,直接放入地址中,替换{academy_id}", required = true)})
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = AcademyResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PutMapping("/{academy_id}")
	public Mono<ResponseEntity<AcademyResp>> update(@PathVariable("academy_id")String academy_id,
			@ApiParam(value="需要更新的学院信息以json格式放入requestbody中",required=true) @RequestBody AcademyReq academyReq){
		Academy academy = new Academy(academyReq);
		return academyReactive.findById(academy_id)
				.flatMap(entity->{
					if(StringUtils.isNotBlank(academy.getAcademyName())) {
						entity.setAcademyName(academy.getAcademyName());
					}
					AcademyCheck(entity);
					return academyReactive.save(entity);
				})
				.map(entity->new ResponseEntity<AcademyResp>(new AcademyResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "根据主键查找学院" ,  notes="根据用户academy_id查找学院")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "academy_id", value = "被操作的目标主键,直接放入地址中,替换{academy_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = AcademyResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/{academy_id}")
	public  Mono<ResponseEntity<AcademyResp>> findByID(@PathVariable("academy_id")String academy_id){
		return academyReactive.findById(academy_id)
				.map(entity->new ResponseEntity<AcademyResp>(new AcademyResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	private void AcademyCheck(Academy academy) {
		
	}
}
