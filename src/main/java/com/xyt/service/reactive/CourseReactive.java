package com.xyt.service.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.xyt.entity.Course;
@Repository
public interface CourseReactive extends ReactiveMongoRepository<Course,String> {

}
