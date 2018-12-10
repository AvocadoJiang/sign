package com.xyt.service.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.xyt.entity.Lesson;
@Repository
public interface LessonRepository extends MongoRepository<Lesson, String> {
	@Query(value="{'courseID' :{ $eq:?0 },'startTime' :{ $gte:?1,$lte:?2 }}")   
	List<Lesson> findByCourseIDAndTime(String courseID,Date startTime,Date endTime);
}
