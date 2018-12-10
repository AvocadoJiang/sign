package com.xyt.service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.xyt.entity.Course;
@Repository
public interface CourseRepository extends MongoRepository<Course, String> {
	List<Course> findByClassID(String classID);
}
