package com.xyt.service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.xyt.entity.Major;
@Repository
public interface MajorRepository extends MongoRepository<Major, String> {
	List<Major> findByAcademyID(String academyID);
}
