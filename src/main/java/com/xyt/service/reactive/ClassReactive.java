package com.xyt.service.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.xyt.entity.Class;
@Repository
public interface ClassReactive  extends ReactiveMongoRepository<Class,String>{

}
