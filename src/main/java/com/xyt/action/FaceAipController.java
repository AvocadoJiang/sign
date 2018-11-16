package com.xyt.action;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xyt.entity.request.FaceAipReq;
import com.xyt.entity.response.UserResp;
import com.xyt.service.AipFaceService;
import com.xyt.service.repository.ClassRepository;
import com.xyt.service.repository.LessonRepository;
import com.xyt.service.repository.StuSignRepository;
import com.xyt.service.repository.UserRepository;
import com.xyt.util.Base64Utils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.xyt.entity.Class;
import com.xyt.entity.StuSign;
import com.xyt.entity.User;
import com.xyt.entity.face.entity.FaceUser;
import com.xyt.entity.face.form.FindFace;

@Api(tags = "人脸识别相关接口")
@RestController
@RequestMapping("/face")
//@Slf4j
public class FaceAipController {
	
	LessonRepository lessonRepository;
	StuSignRepository stuSignRepository;
	UserRepository userRepository;
	ClassRepository classRepository;
	AipFaceService aipFaceService;
	/**
	 * 构造函数 构造器注入
	 * @param 
	 */
	public FaceAipController(LessonRepository lessonRepository,StuSignRepository stuSignRepository,UserRepository userRepository,ClassRepository classRepository,AipFaceService aipFaceService) {
		super();
		this.lessonRepository = lessonRepository;
		this.stuSignRepository = stuSignRepository;
		this.userRepository = userRepository;
		this.classRepository = classRepository;
		this.aipFaceService = aipFaceService;
	}

	@Value("${userphoto.savepath}")
	private String userSavePath;
	
	@Value("${userphoto.urlprefix}")
	private String userUrlPrefix;
	
	@Value("${lessonphoto.savepath}")
	private String lessonSavePath;
	
	@Value("${lessonphoto.urlprefix}")
	private String lessonUrlPrefix;
	
	@PostMapping("/test")
	public String FaceTest(@RequestBody FaceAipReq faceAipReq){
		//File file = Base64Utils.decodeBase64ToJPG(faceAipReq.getImage(),userSavePath,"7");
	
		//System.out.println(userUrlPrefix+file.getName());
		return "123";
	}
	
	@ApiOperation(value = "根据上课照片对前来上课的学生进行签到" ,  notes="通过lesson_id确定课程")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "lesson_id", value = "正在上课的课程主键,直接放入地址中,替换{lesson_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = UserResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PostMapping("/{lesson_id}")
	public Flux<UserResp> LessonSign(@PathVariable("lesson_id")String lesson_id,
			@ApiParam(value="上课照片通过Base64编码成字符串放入Request Body中",required=true)@RequestBody FaceAipReq faceAipReq){
		/**
		 * 1.储存好图片，获得base64加密编码
		 * 2.获取班级列表
		 * 3.搜索接口
		 * 4.获取学生列表
		 * 5.进行签到
		 */
		System.out.println(faceAipReq);
		File file = Base64Utils.decodeBase64ToJPG(faceAipReq.getImage(),lessonSavePath,String.valueOf(System.currentTimeMillis()));
		List<Class> list = classRepository.findAll();
		StringBuffer groupList = new StringBuffer(); 
		for (Class c : list) {
			groupList.append(c.getClassID());
			groupList.append(",");
		}
		
		String image = Base64Utils.encodeBase64FromFile(file);
		
		//人脸搜索
		FindFace findFace =aipFaceService.faceSearch(groupList.toString(), image,AipFaceService.IMAGE_TYPE_BASE64);
		System.out.println(findFace);
		FaceUser[] faceUser = findFace.getResult().getUser_list();
		
		List<UserResp> userList= new ArrayList<UserResp>();
		for (FaceUser u : faceUser) {
			User user = userRepository.findByUserNumber(u.getUser_id());
			userList.add(new UserResp(user));
			
			
			//新增签到
			StuSign stuSign = stuSignRepository.findByUserIDAndLessonID(user.getUserID(), lesson_id);
			if(stuSign==null) {
				stuSign = new StuSign();
				stuSign.setLessonID(lesson_id);
				stuSign.setStudentID(user.getUserID());
				stuSign.setStudent(user);
				stuSign.setLesson(lessonRepository.findById(stuSign.getLessonID()).get());
				stuSignRepository.save(stuSign);
			}

			
			
		}
		
		
		return Mono.just(userList).flatMapMany(Flux::fromIterable);
	}
	
	
}
