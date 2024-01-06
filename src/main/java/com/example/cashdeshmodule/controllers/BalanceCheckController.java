package com.example.cashdeshmodule.controllers;

import com.example.cashdeshmodule.service.contracts.BalanceCheckService;
import com.example.cashdeshmodule.service.contracts.HashingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BalanceCheckController {
    private final BalanceCheckService balanceCheckService;
    private final HashingService hashingService;

    @GetMapping("/api/v1/cash-balance")
    public ResponseEntity<?> checkBalance(@RequestHeader("FIB-X-AUTH") String apiKey) {
        if (!hashingService.verifyApiKey(apiKey)) {
            return ResponseEntity.badRequest().body("Invalid API key");
        }
        return ResponseEntity.ok(balanceCheckService.getCurrentBalance());
    }
}
