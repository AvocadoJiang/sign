package com.xyt.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.xyt.entity.Meeting;
@Repository
public interface MeetingRepository extends MongoRepository<Meeting, String> {

}
