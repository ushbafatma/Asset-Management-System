package com.example.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Log;
import com.example.repository.LogRepository;

@Service
public class AuditService {

    @Autowired
    private LogRepository logRepository;

    public void logAction(String tableName, String action, String details) {
        Log log = new Log();
        log.setTableName(tableName);
        log.setAction(action);
        log.setDetails(details);
        log.setTimestamp(new Timestamp(System.currentTimeMillis()));
        logRepository.save(log);
    }
}
