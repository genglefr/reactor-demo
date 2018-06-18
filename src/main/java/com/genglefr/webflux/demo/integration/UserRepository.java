package com.genglefr.webflux.demo.integration;

import com.genglefr.webflux.demo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
