package com.xyt.service.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.xyt.entity.User;

import reactor.core.publisher.Mono;

@Repository
public interface UserReactive extends ReactiveMongoRepository<User,String>{
	Mono<Boolean> existsByUserNumber(String userNumber);
}
