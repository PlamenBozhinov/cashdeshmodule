package com.example.cashdeshmodule.service.contracts;

import com.example.cashdeshmodule.models.Balance;

import java.util.List;

public interface BalanceCheckService {
    List<Balance> getCurrentBalance();
}
