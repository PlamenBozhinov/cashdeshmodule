package com.example.cashdeshmodule.service;

import com.example.cashdeshmodule.models.Balance;
import com.example.cashdeshmodule.models.Transaction;
import com.example.cashdeshmodule.service.contracts.BalanceCheckService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BalanceCheckServiceImpl implements BalanceCheckService {
    private final ObjectMapper objectMapper;

    public List<Balance> getCurrentBalance() {
        String currentWorkingDir = System.getProperty("user.dir");
        String filePath = currentWorkingDir + "/src/main/java/repository/balances.txt";
        String transPath = currentWorkingDir + "/src/main/java/repository/transactions.txt";
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(transPath, true))) {

            for (Balance balance : balances) {
                writer.write(objectMapper.writeValueAsString(Transaction.builder()
                        .operation("Check Balance")
                        .currency(balance.getCurrency())
                        .balance(balance.getBalance())
                        .denomination(balance.getDenominations())
                        .date(LocalDateTime.now())
                        .cashier("MARTINA")
                        .build()));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return balances;
    }
}
