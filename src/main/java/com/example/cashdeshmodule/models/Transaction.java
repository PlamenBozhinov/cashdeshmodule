package com.example.cashdeshmodule.models;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;

@Data
@Builder
public class Transaction {
    private String cashier;
    private String currency;
    private String operation;
    private HashMap<Integer, Integer> denomination;
    private BigDecimal balance;
    private LocalDateTime date;
}
