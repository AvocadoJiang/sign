package com.xyt.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

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
import com.xyt.entity.Course;
import com.xyt.entity.Lesson;
import com.xyt.entity.Major;
import com.xyt.entity.StuSign;
import com.xyt.entity.request.SignReq;
import com.xyt.entity.response.AcademySignResp;
import com.xyt.entity.response.ClassSignResp;
import com.xyt.entity.response.CourseSignResp;
import com.xyt.service.repository.ClassRepository;
import com.xyt.service.repository.CourseRepository;
import com.xyt.service.repository.LessonRepository;
import com.xyt.service.repository.MajorRepository;
import com.xyt.service.repository.StuSignRepository;
import com.xyt.service.repository.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "签到情况相关接口")
@RestController
@RequestMapping("/sign")
//@Slf4j
public class SignController {
	private ClassRepository classRepository;
	private CourseRepository courseRepository;
	private UserRepository userRepository;
	private LessonRepository lessonRepository;
	private StuSignRepository stuSignRepository;
	private MajorRepository majorRepository;
	
	/**
	 * 构造函数 构造器注入
	 * @param classReactive
	 */
	public SignController(ClassRepository classRepository,CourseRepository courseRepository,UserRepository userRepository,LessonRepository lessonRepository,StuSignRepository stuSignRepository,MajorRepository majorRepository) {
		super();
		this.classRepository = classRepository;
		this.courseRepository = courseRepository;
		this.userRepository = userRepository;
		this.lessonRepository = lessonRepository;
		this.stuSignRepository = stuSignRepository;
		this.majorRepository = majorRepository;
		
	}
	
	
	@ApiOperation(value = "按班级查询签到信息" ,  notes="上传班级以及时间范围查询签到情况")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "class_id", value = "被操作的班级主键,直接放入地址中,替换{class_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = ClassSignResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PutMapping("/class/{class_id}")
	public ClassSignResp getClassSign(@PathVariable("class_id")String class_id,@ApiParam(value="查询的时间范围,以json格式放入Request Body中",required=true) @Valid @RequestBody SignReq signReq){	
		return SearchClassSign(class_id,signReq);
	}
	


	@ApiOperation(value = "按学院查询签到信息" ,  notes="上传学院以及时间范围查询签到情况")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "academy_id", value = "被操作的学院主键,直接放入地址中,替换{academy_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = ClassSignResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PutMapping("/academy/{academy_id}")
	public AcademySignResp getAcademySign(@PathVariable("academy_id")String academy_id,@ApiParam(value="查询的时间范围,以json格式放入Request Body中",required=true) @Valid @RequestBody SignReq signReq){	
		AcademySignResp academySignResp = new AcademySignResp();
		//1.查找全部专业
		List<Major> majorList = majorRepository.findByAcademyID(academy_id);
		//2.查找全部班级
		List<com.xyt.entity.Class> classList = new LinkedList();
		for (Major major : majorList) {
			classList.addAll(classRepository.findByMajorID(major.getMajorID()));
		}
		List<ClassSignResp> classSignRespList = new ArrayList<>();
		for (com.xyt.entity.Class c : classList) {
			classSignRespList.add(SearchClassSign(c.getClassID(),signReq));
		}
		int sum = 0;
		float rate_of_attendance = 0f;
		float rate_of_leave_early = 0f;
		float rate_of_ask_for_leave = 0f;
		//正序排序
		classSignRespList.sort(Comparator.naturalOrder());
		
		for (ClassSignResp classSignResp : classSignRespList) {
			
			rate_of_attendance += classSignResp.getRate_of_attendance();
			rate_of_leave_early +=classSignResp.getRate_of_leave_early();
			rate_of_ask_for_leave += classSignResp.getRate_of_ask_for_leave();
			sum += classSignResp.getStudent_number();
		}
		System.out.println(classSignRespList.size());
		if(classSignRespList.size()>0) {
			//3.计算学院到课率，请假率，早退率，总人数,到课率最高、最低班级
			academySignResp.setRate_of_attendance_highest_class_name(classSignRespList.get(classSignRespList.size()-1).getClass_name());
			academySignResp.setRate_of_attendance_lowest_class_name(classSignRespList.get(0).getClass_name());
			academySignResp.setRate_of_attendance(rate_of_attendance/classSignRespList.size());
			academySignResp.setRate_of_leave_early(rate_of_leave_early/classSignRespList.size());
			academySignResp.setRate_of_ask_for_leave(rate_of_ask_for_leave/classSignRespList.size());
			academySignResp.setStudent_number(sum);
		}
		
		academySignResp.setClass_list(classSignRespList);
		return academySignResp;
	}
	
	
	
	private ClassSignResp SearchClassSign(String class_id, @Valid SignReq signReq) {
		//1.根据class_id查找班级信息
		ClassSignResp classSignResp = new ClassSignResp();
		com.xyt.entity.Class c = classRepository.findById(class_id).get();
		classSignResp.setClass_name(c.getClassName());
		//2.根据class_id统计班级总人数
		classSignResp.setStudent_number(userRepository.countByClassID(class_id));
		//3.根据class_id查找课程
		List<Course> courseList = courseRepository.findByClassID(class_id);
		List<CourseSignResp> courseSignRespList = new ArrayList<CourseSignResp>();
		//4.根据course_id,start_time,end_time查找课时
		for (Course course : courseList) {
			CourseSignResp courseSignResp = new CourseSignResp(course);
			List<Lesson> lessonList = lessonRepository.findByCourseIDAndTime(course.getCourseID(),signReq.getStart_time(),signReq.getEnd_time());
			int size = lessonList.size();
			//5.统计每个课程的到课率，早退率，请假率
			int attendance = 0;
			int leave_early = 0;
			int ask_for_leave = 0;
			int sum = size * classSignResp.getStudent_number();
			
			for (Lesson lesson : lessonList) {
				List<StuSign> stuSignList = stuSignRepository.findByLessonID(lesson.getLessonID());
				
				for(StuSign stuSign:stuSignList) {
					if(stuSign.getStatus().equals(StuSign.SIGN_STATUS.NORMAL.name())) {
						attendance++;
					}
					if(stuSign.getStatus().equals(StuSign.SIGN_STATUS.LEAVE_EARLY.name())) {
						leave_early++;
					}
					if(stuSign.getStatus().equals(StuSign.SIGN_STATUS.ASK_FOR_LEAVE.name())) {
						ask_for_leave++;
					}
				}
			}
			if(sum>0) {
				courseSignResp.setRate_of_attendance((float)attendance/sum);
				courseSignResp.setRate_of_leave_early((float)leave_early/sum);
				courseSignResp.setRate_of_ask_for_leave((float)ask_for_leave/sum);
			}
			
			courseSignRespList.add(courseSignResp);
			
		}
		float rate_of_attendance = 0f;
		float rate_of_leave_early = 0f;
		float rate_of_ask_for_leave = 0f;
		for (CourseSignResp courseSignResp : courseSignRespList) {
			rate_of_attendance += courseSignResp.getRate_of_attendance();
			rate_of_leave_early += courseSignResp.getRate_of_leave_early();
			rate_of_ask_for_leave += courseSignResp.getRate_of_ask_for_leave();
		}
		if(courseSignRespList.size()>0) {
			classSignResp.setRate_of_attendance(rate_of_attendance /= courseSignRespList.size());
			classSignResp.setRate_of_leave_early(rate_of_leave_early /= courseSignRespList.size());
			classSignResp.setRate_of_ask_for_leave(rate_of_ask_for_leave /= courseSignRespList.size());
		}
		classSignResp.setCourse_list(courseSignRespList);
		return classSignResp;
	}
	
	
}
