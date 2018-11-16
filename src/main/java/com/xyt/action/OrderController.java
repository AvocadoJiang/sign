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

import com.xyt.entity.Order;
import com.xyt.entity.request.OrderReq;
import com.xyt.entity.response.OrderResp;
import com.xyt.advice.exceptions.CheckException;
import com.xyt.globle.Constants;
import com.xyt.service.reactive.OrderReactive;
import com.xyt.service.repository.CourseRepository;
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

@Api(tags = "选课相关接口")
@RestController
@RequestMapping("/order")
public class OrderController {
	private OrderReactive orderReactive;
	private UserRepository userRepository;
	private CourseRepository courseRepository;
	private LessonRepository lessonRepository;
	/**
	 * 构造器注入
	 * @param repository
	 * @param academyRepository
	 */
	public OrderController(OrderReactive orderReactive, UserRepository userRepository,
			CourseRepository courseRepository,LessonRepository lessonRepository) {
		super();
		this.orderReactive = orderReactive;
		this.userRepository = userRepository;
		this.courseRepository = courseRepository;
		this.lessonRepository = lessonRepository;
	}
	
	@ApiOperation(value = "获取全部选课" ,  notes="获取全部选课,以数组形式一次性返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = OrderResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/all")
	public Flux<OrderResp> getAll(){		
		return orderReactive.findAll().map(entity->new OrderResp(entity));
	}
	
	@ApiOperation(value = "根据课程ID获取全部选课学生" ,  notes="根据课程ID获取全部选课,以数组形式一次性返回数据")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path", name = "lesson_id", value = "课时ID,直接放入地址中,替换{lesson_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = OrderResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/getByLesson/{lesson_id}")
	public Flux<OrderResp> getByLesson(@PathVariable("lesson_id")String lesson_id){
		String course_id = lessonRepository.findById(lesson_id).get().getCourseID();
		return orderReactive.findByCourseID(course_id).map(entity->new OrderResp(entity));
	}

	@ApiOperation(value = "获取全部选课" ,  notes="获取全部选课,以SSE形式多次返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = OrderResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping(value="/stream/all",produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<OrderResp> streamGetAll(){
		return orderReactive.findAll().map(entity->new OrderResp(entity));
	}
	
	@ApiOperation(value = "新增选课" ,  notes="上传必要的选课信息来创建一个新的选课")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = OrderResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PostMapping("/add")
	public Mono<OrderResp> add(@ApiParam(value="需要更新的课时信息,以json格式放入Request Body中",required=true) @Valid @RequestBody OrderReq orderReq) {
		Order order = new Order(orderReq);
		OrderCheck(order);
		return orderReactive.save(order).map(entity->new OrderResp(entity));
	}
	
	@ApiOperation(value = "删除选课" ,  notes="根据选课的order_id来删除一个选课")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "order_id", value = "被操作的目标主键,直接放入地址中,替换{order_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@DeleteMapping("/{order_id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable("order_id")String order_id){
		return orderReactive.findById(order_id)
				.flatMap(order->orderReactive.delete(order)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	
	
	@ApiOperation(value = "根据主键查找选课" ,  notes="根据选课order_id查找选课")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "order_id", value = "被操作的目标主键,直接放入地址中,替换{order_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = OrderResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/{order_id}")
	public  Mono<ResponseEntity<OrderResp>> findByID(@PathVariable("order_id")String order_id){
		return orderReactive.findById(order_id)
				.map(entity->new ResponseEntity<OrderResp>(new OrderResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	private void OrderCheck(Order order) {
		
		if(!userRepository.existsById(order.getStudentID())) {
			throw new CheckException("studentID",Constants.REFERENTIAL_INTEGRITY_CHECK_FAILED);
		}
		if(!courseRepository.existsById(order.getCourseID())) {
			throw new CheckException("courseID",Constants.REFERENTIAL_INTEGRITY_CHECK_FAILED);
		}
		order.setStudent(userRepository.findById(order.getStudentID()).get());
		order.setCourse(courseRepository.findById(order.getCourseID()).get());
		
	}
	
}
