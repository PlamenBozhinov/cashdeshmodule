package com.example.cashdeshmodule.models;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;

@Data
public class Request {
    private String operation;
    private String currency;
    private String cashier;
    private BigDecimal value;
    private HashMap<Integer, Integer> denomination;
}
