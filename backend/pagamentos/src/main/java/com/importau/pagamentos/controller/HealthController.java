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
@RequestMapping("/api/v1")
public class HealthController {

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("service", "pagamentos-service");
        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now());
        status.put("version", "1.0.0");
        
        return ResponseEntity.ok(status);
    }
}
