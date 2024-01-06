package com.example.cashdeshmodule.service;

import com.example.cashdeshmodule.models.Balance;
import com.example.cashdeshmodule.service.contracts.BalanceCheckService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BalanceCheckServiceImpl implements BalanceCheckService {
    private final ObjectMapper objectMapper;

    public List<Balance> getCurrentBalance() {
        String currentWorkingDir = System.getProperty("user.dir");
        String filePath = currentWorkingDir + "/src/main/java/repository/balances.txt";

        List<Balance> balances = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                Balance balance = objectMapper.readValue(line, Balance.class);
                balances.add(balance);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return balances;
    }
}
