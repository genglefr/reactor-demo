package com.genglefr.webflux.demo.service;

import com.genglefr.webflux.demo.model.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Cacheable("users")
public class UserService extends GenericService<User>{
}
