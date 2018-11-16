package com.xyt.service.reactive;

import java.util.Date;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.xyt.entity.Lesson;

import reactor.core.publisher.Flux;

@Repository
public interface LessonReactive extends ReactiveMongoRepository<Lesson,String>{
	
	Flux<Lesson> findByStartTimeBetween(Date from,Date to);
}
