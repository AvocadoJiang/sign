package com.xyt.service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.xyt.entity.StuSign;
@Repository
public interface StuSignRepository extends MongoRepository<StuSign, String> {
	@Query(value="{'studentID' :{ $eq:?0 },'lessonID' :{ $eq:?1 }}")   
	StuSign findByUserIDAndLessonID(String userID,String lessonID);
	
	List<StuSign> findByLessonID(String LessonID);
}
