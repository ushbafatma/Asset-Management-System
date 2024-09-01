package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.domain.Log;

public interface LogRepository extends JpaRepository<Log, Long> {
}
