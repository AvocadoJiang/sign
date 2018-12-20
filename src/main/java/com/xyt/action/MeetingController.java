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

import com.xyt.entity.Meeting;
import com.xyt.entity.request.MeetingReq;
import com.xyt.entity.response.MeetingResp;
import com.xyt.service.reactive.MeetingReactive;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Api(tags = "会议相关接口")
@RestController
@RequestMapping("/meeting")
//@Slf4j
public class MeetingController {
	private MeetingReactive meetingReactive;
	
	/**
	 * 构造函数 构造器注入
	 * @param meetingRepository
	 */
	public MeetingController(MeetingReactive meetingReactive) {
		super();
		this.meetingReactive = meetingReactive;
		
	}
	
	@ApiOperation(value = "获取全部会议" ,  notes="获取全部会议,以数组形式一次性返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = MeetingResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/all")
	public Flux<MeetingResp> getAll(){		
		return meetingReactive.findAll().map(entity->new MeetingResp(entity));
	}
	
	@ApiOperation(value = "获取全部会议" ,  notes="获取全部会议,以SSE形式多次返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = MeetingResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping(value="/stream/all",produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<MeetingResp> streamGetAll(){
		return meetingReactive.findAll().map(entity->new MeetingResp(entity));
	}
	
	@ApiOperation(value = "新增会议" ,  notes="上传必要的会议信息来创建一个新的会议")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = MeetingResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PostMapping("/add")
	public Mono<MeetingResp> add(@ApiParam(value="以json格式放入Request Body中",required=true) @Valid @RequestBody MeetingReq meetingReq) {
		Meeting meeting = new Meeting(meetingReq);
		MeetingCheck(meeting);
		return meetingReactive.save(meeting).map(entity->new MeetingResp(entity));
	}
	
	@ApiOperation(value = "删除会议" ,  notes="根据用户的meeting_id来删除一个会议")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "meeting_id", value = "被操作的目标主键,直接放入地址中,替换{meeting_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@DeleteMapping("/{meeting_id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable("meeting_id")String meeting_id){
		return meetingReactive.findById(meeting_id)
				.flatMap(entity->meetingReactive.delete(entity)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "更新会议信息" ,  notes="通过meeting_id定位用户并更新其信息")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path", name = "meeting_id", value = "被操作的目标主键,直接放入地址中,替换{meeting_id}", required = true)})
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = MeetingResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PutMapping("/{meeting_id}")
	public Mono<ResponseEntity<MeetingResp>> update(@PathVariable("meeting_id")String meeting_id,
			@ApiParam(value="需要更新的会议信息以json格式放入requestbody中",required=true) @RequestBody MeetingReq meetingReq){
		Meeting meeting = new Meeting(meetingReq);
		return meetingReactive.findById(meeting_id)
				.flatMap(entity->{
					if(StringUtils.isNotBlank(meeting.getMeetingName())) {
						entity.setMeetingName(meeting.getMeetingName());
					}
					if(StringUtils.isNotBlank(meeting.getAddress())) {
						entity.setAddress(meeting.getAddress());
					}
					
					if(StringUtils.isNotBlank(meeting.getSpeaker())) {
						entity.setSpeaker(meeting.getSpeaker());
					}
					
					if(meeting.getStartTime()!=null) {
						entity.setStartTime(meeting.getStartTime());
					}
					
					if(StringUtils.isNotBlank(meeting.getOrganizer())) {
						entity.setOrganizer(meeting.getOrganizer());
					}
					
					if(meeting.getLeaveNumber()!=0) {
						entity.setLeaveNumber(meeting.getLeaveNumber());
					}
					
					if(meeting.getStudentNumber()!=0) {
						entity.setStudentNumber(meeting.getStudentNumber());
					}
					
					if(StringUtils.isNotBlank(meeting.getClassName())) {
						entity.setClassName(meeting.getClassName());
					}
					MeetingCheck(entity);
					return meetingReactive.save(entity);
				})
				.map(entity->new ResponseEntity<MeetingResp>(new MeetingResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "根据主键查找会议" ,  notes="根据用户meeting_id查找会议")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "meeting_id", value = "被操作的目标主键,直接放入地址中,替换{meeting_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = MeetingResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/{meeting_id}")
	public  Mono<ResponseEntity<MeetingResp>> findByID(@PathVariable("meeting_id")String meeting_id){
		return meetingReactive.findById(meeting_id)
				.map(entity->new ResponseEntity<MeetingResp>(new MeetingResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	private void MeetingCheck(Meeting meeting) {
		
	}
}
