package com.xyt.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.xyt.entity.Order;
@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

}
