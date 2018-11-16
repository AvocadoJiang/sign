package com.xyt.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SetCharacterEncodingFilter implements Filter{

	//编码格式
	private static String encoding= "UTF-8";
		
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		// 判断当前请求是否单独设置编码格式
		
		if(request.getCharacterEncoding()==null)
		{
			
			
			//获取默认编码格式
			String encoding = this.encoding;
			if(encoding!=null)
			{
				request.setCharacterEncoding(encoding);
				response.setContentType("text/plain;charset="+encoding);
				
			}
		}else{
			response.setContentType("text/plain;charset="+request.getCharacterEncoding());
		}
		
		//向下一个过滤器转发
		chain.doFilter(request, response);
		
	}

	@Override
	public void destroy() {
	}

}
