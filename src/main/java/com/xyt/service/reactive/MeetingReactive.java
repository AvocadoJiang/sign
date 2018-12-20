package com.xyt.service.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.xyt.entity.Meeting;

@Repository
public interface MeetingReactive extends ReactiveMongoRepository<Meeting,String>{
	
}
