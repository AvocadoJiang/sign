package com.xyt.service.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.xyt.entity.StuSign;

import reactor.core.publisher.Flux;

@Repository
public interface StuSignReactive extends ReactiveMongoRepository<StuSign,String>{
	Flux<StuSign> findBylessonID(String lessonID);
}
