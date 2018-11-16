package com.xyt.action;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xyt.entity.Major;
import com.xyt.entity.request.MajorReq;
import com.xyt.entity.response.MajorResp;
import com.xyt.advice.exceptions.CheckException;
import com.xyt.globle.Constants;
import com.xyt.service.reactive.MajorReactive;
import com.xyt.service.repository.AcademyRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Api(tags = "专业相关接口")
@RestController
@RequestMapping("/major")
public class MajorController {
	private MajorReactive majorReactive;
	private AcademyRepository academyRepository;
	/**
	 * 构造器注入
	 * @param repository
	 * @param academyRepository
	 */
	public MajorController(MajorReactive majorReactive,AcademyRepository academyRepository) {
		super();
		this.majorReactive = majorReactive;
		this.academyRepository = academyRepository;
	}
	
	@ApiOperation(value = "获取全部专业" ,  notes="获取全部专业,以数组形式一次性返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = MajorResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/all")
	@Async
	public Flux<MajorResp> getAll(){		
		return majorReactive.findAll().map(entity->new MajorResp(entity));
	}
	
	@ApiOperation(value = "获取全部专业" ,  notes="获取全部专业,以SSE形式多次返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = MajorResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping(value="/stream/all",produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	@Async
	public Flux<MajorResp> streamGetAll(){
		return majorReactive.findAll().map(entity->new MajorResp(entity));
	}
	
	@ApiOperation(value = "新增专业" ,  notes="上传必要的专业信息来创建一个新的专业")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = MajorResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PostMapping("/add")
	@Async
	public Mono<MajorResp> add(@ApiParam(value="新增专业的必要信息,以json格式放入Request Body中",required=true) @Valid @RequestBody MajorReq majorReq) {
		Major major = new Major(majorReq);
		MajorCheck(major);
		return majorReactive.save(major).map(entity->new MajorResp(entity));
	}
	
	@ApiOperation(value = "删除专业" ,  notes="根据专业的major_id来删除一个专业")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "major_id", value = "被操作的目标主键,直接放入地址中,替换{major_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@DeleteMapping("/{major_id}")
	@Async
	public Mono<ResponseEntity<Void>> delete(@PathVariable("major_id")String major_id){
		return majorReactive.findById(major_id)
				.flatMap(major->majorReactive.delete(major)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "更新专业信息" ,  notes="通过major_id定位专业并更新其信息")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "major_id", value = "被操作的目标主键,直接放入地址中,替换{major_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = MajorResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PutMapping("/{major_id}")
	public Mono<ResponseEntity<MajorResp>> update(@PathVariable("major_id")String major_id,
			@ApiParam(value="新增专业的必要信息,以json格式放入Request Body中",required=true) @RequestBody MajorReq majorReq){
		Major major = new Major(majorReq);
		return majorReactive.findById(major_id)
				.flatMap(entity->{
					if(StringUtils.isNotBlank(major.getMajorName())) {
						entity.setMajorName(major.getMajorName());
					}
					if(StringUtils.isNotBlank(major.getAcademyID())) {
						entity.setAcademyID(major.getAcademyID());
					}
					MajorCheck(entity);
					return majorReactive.save(entity);
				})
				.map(entity->new ResponseEntity<MajorResp>(new MajorResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "根据主键查找专业" ,  notes="根据专业major_id查找专业")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "major_id", value = "被操作的目标主键,直接放入地址中,替换{major_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = MajorResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/{major_id}")
	public  Mono<ResponseEntity<MajorResp>> findByID(@PathVariable("major_id")String major_id){
		return majorReactive.findById(major_id)
				.map(entity->new ResponseEntity<MajorResp>(new MajorResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	private void MajorCheck(Major major) {
		
		if(!academyRepository.existsById(major.getAcademyID())) {
			throw new CheckException("academyID",Constants.REFERENTIAL_INTEGRITY_CHECK_FAILED);
		}
		major.setAcademy(academyRepository.findById(major.getAcademyID()).get());
		
	}
	
}
