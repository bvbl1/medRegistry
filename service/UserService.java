package com.abylay.task1.service;

import com.abylay.task1.DTOs.AuthRequest;
import com.abylay.task1.models.User;
import com.abylay.task1.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EncryptionService encryptionService;

    public UserService(UserRepository userRepository, EncryptionService encryptionService) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
    }

    public User register(User user) {
        user.setPassword(encryptionService.encrypt(user.getPassword()));
        return userRepository.save(user);
    }

    public String login(AuthRequest authRequest) {
        User existingUser = userRepository.findByUsername(authRequest.getUsername());
        if (existingUser == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        if (!encryptionService.encrypt(authRequest.getPassword()).equals(existingUser.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String token = JWT.create()
                .withSubject(existingUser.getUsername())
                .withClaim("authorities", List.of(existingUser.getRole()))
                .withExpiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .sign(Algorithm.HMAC256("this-is-a-very-strong-secret-key-with-32-chars-123456"));
        return Map.of("token", token).toString();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findById(long id) {
        return userRepository.getReferenceById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void delete(long id) {
        userRepository.deleteById(id);
    }
}
