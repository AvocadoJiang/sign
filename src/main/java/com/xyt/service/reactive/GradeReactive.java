package com.xyt.service.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.xyt.entity.Grade;

@Repository
public interface GradeReactive  extends ReactiveMongoRepository<Grade,String>{
	
}
