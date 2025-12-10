package com.importau.pagamentos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller para verificação de saúde do microsserviço
 */
@RestController
public class HealthController {

    @GetMapping("/api/v1/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("service", "pagamentos-service");
        status.put("status", "healthy");
        status.put("timestamp", LocalDateTime.now());
        status.put("version", "1.0.0");
        
        return ResponseEntity.ok(status);
    }
    
    @GetMapping("/actuator/health")
    public ResponseEntity<Map<String, Object>> getActuatorHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        
        return ResponseEntity.ok(health);
    }
}
