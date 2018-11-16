package com.xyt.service.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.xyt.entity.Major;

@Repository
public interface MajorReactive  extends ReactiveMongoRepository<Major,String>{
	
}
