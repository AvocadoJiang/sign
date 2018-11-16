package com.xyt.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

public class Base64Utils {
		
	public static String encodeBase64FromFile(File file) {
		
		FileInputStream inputFile;
		
		try {
			inputFile = new FileInputStream(file);
			byte[] buffer = new byte[(int)file.length()];
			inputFile.read(buffer);
			inputFile.close();
			
			return Base64.getEncoder().encodeToString(buffer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
		
	}
	

	/**
	 * 将Base64字符串转化成jpg文件
	 * @param image 文件的Base64加密字符串
	 * @param savepath 文件的存储路径
	 * @param filename 文件的存储名
	 * @return
	 */
	public static File decodeBase64ToJPG(String image,String savepath,String filename) {
		
		//Base64字符串初始化，去掉data:image/png;base64,
		if(image.split(":")[0].equals("data")) {
			image = image.split("base64,")[1];
		}
		
		byte[] bytes = Base64.getDecoder().decode(image);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		
		File file = null;
		try {
			
			BufferedImage bi1 = ImageIO.read(bais);
			file = new File(savepath+filename+".jpg");// 可以是jpg,png,gif格式
			
			ImageIO.write(bi1, "jpg", file);// 不管输出什么格式图片，此处不需改动
			
		} catch (IOException e) {
			System.out.println("异常");
			e.printStackTrace();
		}
		
		return file;
		
	}
}
