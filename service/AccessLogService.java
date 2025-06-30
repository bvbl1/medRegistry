package com.abylay.task1.service;

import com.abylay.task1.models.AccessLog;
import com.abylay.task1.repository.AccessLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccessLogService {
    private final AccessLogRepository accessLogRepository;

    public AccessLogService(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }

    public void logAccess(Long userId, String username, List<String> iin) {
        AccessLog log = new AccessLog();
        log.setUserId(userId);
        log.setUsername(username);
        log.setAccessedAt(LocalDateTime.now());
        log.setAccessedIins(iin);
        accessLogRepository.save(log);
    }
}
