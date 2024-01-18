package com.example.StocksPortfolioApplication.service;

import com.example.StocksPortfolioApplication.dto.UserPortfolioDetailsDto;
import com.example.StocksPortfolioApplication.model.TransactionType;
import com.example.StocksPortfolioApplication.model.User;
import org.springframework.stereotype.Component;

@Component
public interface UserTransactionDetailsService {

    public String tradeApiService(User user, Integer stockId, Integer quantity, TransactionType transactionType) throws RuntimeException;

    public UserPortfolioDetailsDto getPortfolioOfUser(Integer userAccountId) throws RuntimeException;

}
