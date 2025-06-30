package com.abylay.task1.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "access_logs")
public class AccessLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String username;

    @ElementCollection
    @CollectionTable(name = "accessed_iins", joinColumns = @JoinColumn(name = "log_id"))
    @Column(name = "iin")
    private List<String> accessedIins = new ArrayList<>();
    private String method;
    private String endpoint;

    private LocalDateTime accessedAt;

    public AccessLog(Long id, Long userId, String username, List<String> accessedIins, LocalDateTime accessedAt, String method, String endpoint) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.accessedIins = accessedIins;
        this.accessedAt = accessedAt;
        this.method = method;
        this.endpoint = endpoint;
    }
    public AccessLog() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getAccessedIins() {
        return accessedIins;
    }

    public LocalDateTime getAccessedAt() {
        return accessedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAccessedIins(List<String> accessedIins) {
        this.accessedIins = accessedIins;
    }

    public void setAccessedAt(LocalDateTime accessedAt) {
        this.accessedAt = accessedAt;
    }
}

