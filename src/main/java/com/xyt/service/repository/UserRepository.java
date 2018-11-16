package com.xyt.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.xyt.entity.User;
@Repository
public interface UserRepository extends MongoRepository<User, String> {
	User findByUserNumber(String userNumber);
}
