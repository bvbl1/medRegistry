package com.abylay.task1.controller;

import com.abylay.task1.DTOs.AuthRequest;
import com.abylay.task1.models.User;
import com.abylay.task1.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class AuthController {
    private final UserService userService;
    AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request) {
        return userService.login(request);
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/username/{username}")
    public User getUserByName(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @GetMapping("/id/{id}")
    public User getUserById(@PathVariable long id) {
        return userService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.delete(id);
    }
}
