package com.xyt.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.xyt.entity.Lesson;
@Repository
public interface LessonRepository extends MongoRepository<Lesson, String> {

}
