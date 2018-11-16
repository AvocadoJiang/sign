package com.xyt.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.xyt.entity.Academy;
@Repository
public interface AcademyRepository extends MongoRepository<Academy, String> {

}
