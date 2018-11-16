package com.xyt.service;

import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.baidu.aip.face.AipFace;
import com.xyt.entity.face.form.FindFace;
import com.xyt.entity.face.form.General;
import com.xyt.entity.face.form.UserFace;
import com.xyt.util.JSONUtil;

@Service
public class AipFaceService {
	//设置APPID/AK/SK
    private static final String APP_ID = "14607747";
    private static final String API_KEY = "Ca8PpKP8jdGeo42mTXxoHYYk";
    private static final String SECRET_KEY = "TGetlOTGFOOx7erGponbnxnf9o1DGRkq";
    
    public static final String IMAGE_TYPE_BASE64 = "BASE64";
    public static final String IMAGE_TYPE_URL = "URL";
    public static final String IMAGE_TYPE_FACE_TOKEN = "FACE_TOKEN";
    
    
    public UserFace faceAdd(String user_id,String group_id,String image,String image_type) {
    	// 初始化一个AipFace
    	AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

    	HashMap<String, String> options = new HashMap<String, String>();
   
    	// 人脸更新
    	
    	String jsonString = client.addUser(image, image_type, group_id, user_id, options).toString();
    	//将json字符串装化成bean对象
    	return (UserFace) JSONUtil.jsonToBean(jsonString, UserFace.class);
    }
    
    /**
     * 人脸更新，用于对人脸库中指定用户，更新其下的人脸图像。
     *说明：**针对一个uid执行更新操作，新上传的人脸图像将覆盖此uid原有所有图像。
 	 *说明：**执行更新操作，如果该uid不存在时，会返回错误。如果添加了action_type:replace,则不会报错，并自动注册该uid，操作结果等同注册新用户。
     * @param user_id  用户id
     * @param group_id 用户班级id
     * @param image 图片
     * @param image_type  
     * 		BASE64:图片的base64值，base64编码后的图片数据，需urlencode，编码后的图片大小不超过2M；
     * 		URL:图片的 URL地址( 可能由于网络等原因导致下载图片时间过长)；
     * 		FACE_TOKEN: 人脸图片的唯一标识，调用人脸检测接口时，会为每个人脸图片赋予一个唯一的FACE_TOKEN，同一张图片多次检测得到的FACE_TOKEN是同一个
     * @return
     */
    public UserFace faceUpdate(String user_id,String group_id,String image,String image_type) {
    	// 初始化一个AipFace
    	AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        //client.setConnectionTimeoutInMillis(2000);
        //client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        //System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");
    	HashMap<String, String> options = new HashMap<String, String>();
    	//用户资料
        //options.put("user_info", "user's info");
    	//图片质量控制，默认NONE
        //options.put("quality_control", "NORMAL");
    	//活体检测控制，默认NONE
        //options.put("liveness_control", "LOW");
    	// 人脸更新
    	String jsonString = client.updateUser(image, image_type, group_id, user_id, options).toString();
    	//将json字符串装化成bean对象
    	return (UserFace) JSONUtil.jsonToBean(jsonString, UserFace.class);
    }
    
    /**
     * 人脸搜索
     * 1：N人脸搜索：也称为1：N识别，在指定人脸集合中，找到最相似的人脸；
     * @param group_id_list 从指定的group中进行查找 用逗号分隔，上限20个
     * @param image 图片
     * @param image_type 图片类型，注释同上
     * @return
     */
    public FindFace faceSearch(String group_id_list,String image,String image_type) {
    	AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);
    	HashMap<String, String> options = new HashMap<String, String>();
    	//图片质量控制 NONE
        //options.put("quality_control", "NORMAL");
    	//活体检测控制 NONE
        //options.put("liveness_control", "LOW");
    	//对特定用户进行比对
        //options.put("user_id", "233451");
    	//查找后返回的用户数量。返回相似度最高的几个用户，默认为1，最多返回20个。
        options.put("max_user_num", "20");
        
        //人脸搜索
        String jsonString = client.search(image, image_type, group_id_list, options).toString();
        
        return (FindFace) JSONUtil.jsonToBean(jsonString, FindFace.class);
    }
    
    /**
     * 创建用户组
     * @param group_id 用户组id
     * @return
     */
    public General GroupAdd(String group_id) {
    	AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);
    	HashMap<String, String> options = new HashMap<String, String>();
    	String jsonString = client.groupAdd(group_id, options).toString();
    	return (General) JSONUtil.jsonToBean(jsonString, General.class);
    }
    
    /**
     * 删除用户组
     * @param group_id 用户组id
     * @return
     */
    public General GroupDelete(String group_id) {
    	AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);
    	HashMap<String, String> options = new HashMap<String, String>();
    	String jsonString = client.groupDelete(group_id, options).toString();
    	return (General) JSONUtil.jsonToBean(jsonString, General.class);
    }
}
