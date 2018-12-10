package com.xyt.service.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.xyt.entity.Class;
@Repository
public interface ClassRepository extends MongoRepository<Class, String> {
	ArrayList<Class> findByMajorID(String majorID);
}
