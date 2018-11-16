package com.xyt.service.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.xyt.entity.Academy;

@Repository
public interface AcademyReactive extends ReactiveMongoRepository<Academy,String>{
	
}
