package com.example.StocksPortfolioApplication.service;


import com.example.StocksPortfolioApplication.dto.UserPortfolioDetailsDto;
import com.example.StocksPortfolioApplication.model.TransactionType;
import org.springframework.stereotype.Component;

@Component
public interface UserPortfolioService {

    public String makeTransaction(Integer userAccountId, Integer stockId, TransactionType transactionType, Integer quantity) throws Exception;
    public UserPortfolioDetailsDto getAllPortfoliosOfUserById(Integer userAccountId)throws Exception;
    public String makeTrading(Integer userAccountId, Integer stockId, TransactionType transactionType, Integer quantity) throws Exception;
}
