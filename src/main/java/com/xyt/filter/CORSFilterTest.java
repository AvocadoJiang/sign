package com.xyt.filter;


import javax.servlet.annotation.WebFilter;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


/**
 * 本来是打算用这种方法写过滤器的，但一直找不到这个过滤器
 * @author Jason Chiang
 *
 */
@WebFilter(urlPatterns = "/*")
@Component
@Slf4j
public class CORSFilterTest implements org.springframework.web.server.WebFilter{
	
	@Override
	public Mono<Void> filter(ServerWebExchange swe, WebFilterChain wfc) {
//		log.info("CORSFilter运行");
//		ServerHttpRequest request = swe.getRequest();
//		ServerHttpResponse response = swe.getResponse();
//		HttpHeaders headers = response.getHeaders();
//		System.out.println(request.getHeaders().getOrigin());
//		System.out.println(4444);
//		headers.add("Access-Control-Allow-Origin", request.getHeaders().getOrigin());
//        headers.add("Access-Control-Allow-Methods", "*");
//        headers.add("Access-Control-Allow-Headers", "*");
//        headers.add("Access-Control-Allow-Credentials","true");

		return wfc.filter(swe);
	}

}
