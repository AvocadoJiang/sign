package com.xyt.service.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.xyt.entity.Class;

import reactor.core.publisher.Flux;
@Repository
public interface ClassReactive  extends ReactiveMongoRepository<Class,String>{
	Flux<Class> findBymajorID(String majorID);
	Flux<Class> findBygradeID(String gradeID);
}
