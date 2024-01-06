package com.example.cashdeshmodule.service;

import com.example.cashdeshmodule.models.Balance;
import com.example.cashdeshmodule.models.Request;
import com.example.cashdeshmodule.models.Transaction;
import com.example.cashdeshmodule.service.contracts.CashierOperationsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CashierOperationsServiceImpl implements CashierOperationsService {

    private final ObjectMapper objectMapper;

    @Override
    public void handleCashierOperation(Request request) {
        String currentWorkingDir = System.getProperty("user.dir");
        String filePath = currentWorkingDir + "/src/main/java/repository/balances.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            List<String> lines = reader.lines().collect(Collectors.toList());
            reader.close();

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                Balance balance = objectMapper.readValue(line, Balance.class);
                HashMap<Integer, Integer> currentDenominations = balance.getDenominations();
                if (balance.getCurrency().equals(request.getCurrency())) {
                    if (request.getOperation().equals("Deposit")) {
                        request.getDenomination().keySet().stream().forEach(k -> {
                            if (currentDenominations.containsKey(k)) {
                                Integer value = currentDenominations.get(k);
                                value += request.getDenomination().get(k);
                                currentDenominations.put(k, value);
                            } else {
                                currentDenominations.put(k, request.getDenomination().get(k));
                            }

                        });
                        balance.setBalance(balance.getBalance().add(request.getValue()));
                    }
                    if (request.getOperation().equals("Withdraw")) {
                        request.getDenomination().keySet().stream().forEach(k -> {
                            if (currentDenominations.containsKey(k)) {
                                Integer value = currentDenominations.get(k);
                                value -= request.getDenomination().get(k);
                                if (value < 0) {
                                    throw new UnsupportedOperationException("Insufficient amount");
                                }
                                currentDenominations.put(k, value);
                            } else {
                                throw new UnsupportedOperationException("Insufficient amount");
                            }

                        });
                        balance.setBalance(balance.getBalance().subtract(request.getValue()));

                    }
                    String updatedLine = objectMapper.writeValueAsString(balance);

                    lines.set(i, updatedLine);
                    break;
                }
            }


            try (OutputStream outputStream = Files.newOutputStream(new File(filePath).toPath());
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
                writer.write(String.join(System.lineSeparator(), lines));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String transPath = currentWorkingDir + "/src/main/java/repository/transactions.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(transPath, true))) {
            writer.write(objectMapper.writeValueAsString(Transaction.builder()
                    .operation(request.getOperation())
                    .currency(request.getCurrency())
                    .balance(request.getValue())
                    .denomination(request.getDenomination())
                    .date(LocalDateTime.now())
                    .cashier(request.getCashier())
                    .build()));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

