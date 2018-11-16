package com.xyt.service.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.xyt.entity.Order;

import reactor.core.publisher.Flux;

@Repository
public interface OrderReactive extends ReactiveMongoRepository<Order,String>{
	Flux<Order> findByCourseID(String courseID);
}
