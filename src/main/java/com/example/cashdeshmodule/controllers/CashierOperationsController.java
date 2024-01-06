package com.example.cashdeshmodule.controllers;

import com.example.cashdeshmodule.models.Request;
import com.example.cashdeshmodule.service.contracts.CashierOperationsService;
import com.example.cashdeshmodule.service.contracts.HashingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CashierOperationsController {
    private final HashingService hashingService;
    private final CashierOperationsService cashierOperationsService;

    @PostMapping("/api/v1/cash-operation")
    public ResponseEntity<?> handleCashierOperation(@RequestHeader("FIB-X-AUTH") String apiKey, @RequestBody Request request) {
        if (!hashingService.verifyApiKey(apiKey)) {
            return ResponseEntity.badRequest().body("Invalid API key");
        }
        try {
            cashierOperationsService.handleCashierOperation(request);
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Operations is successful");
    }
}
