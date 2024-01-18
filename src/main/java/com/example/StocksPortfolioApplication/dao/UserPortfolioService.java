package com.example.StocksPortfolioApplication.dao;


import com.example.StocksPortfolioApplication.dto.UserPortfolioDetailsDto;
import com.example.StocksPortfolioApplication.model.TransactionType;
import org.springframework.stereotype.Component;

@Component
public interface UserPortfolioService {

    public String makeTransaction(Integer userAccountId, Integer stockId, TransactionType transactionType, Integer quantity) throws RuntimeException;

    public UserPortfolioDetailsDto getAllPortfoliosOfUserById(Integer userAccountId) throws RuntimeException;

    public String makeTrading(Integer userAccountId, Integer stockId, TransactionType transactionType, Integer quantity) throws RuntimeException;
}
