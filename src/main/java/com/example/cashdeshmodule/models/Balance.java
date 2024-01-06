package com.example.cashdeshmodule.models;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;


@Data
public class Balance {
    String currency;
    BigDecimal balance;
    HashMap<Integer, Integer> denominations;
}
