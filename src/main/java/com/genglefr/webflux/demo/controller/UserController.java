package com.genglefr.webflux.demo.controller;

import com.genglefr.webflux.demo.model.User;
import com.genglefr.webflux.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "users")
    public Iterable<User> all() {
        return this.userService.findAll();
    }

    @PostMapping(value = "user")
    public void create(@RequestBody User user) {
        this.userService.save(user);
    }

    @PostMapping(value = "user/{id}")
    public void update(@RequestBody User user, @PathVariable("id") String id) {
        user.setId(id);
        this.userService.save(user);
    }

    @DeleteMapping(value = "user/{id}")
    public void delete(@PathVariable("id") String id) {
        this.userService.delete(id);
    }
}
