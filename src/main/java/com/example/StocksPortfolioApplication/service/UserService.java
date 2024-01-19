package com.example.StocksPortfolioApplication.service;


import com.example.StocksPortfolioApplication.dto.InputUserDto;
import com.example.StocksPortfolioApplication.dto.UserPortfolioDetailsDto;
import com.example.StocksPortfolioApplication.model.TransactionType;
import com.example.StocksPortfolioApplication.model.User;
import com.example.StocksPortfolioApplication.model.UserTransactionDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Component
public interface UserService {

   public Optional<User> getUserById(Integer userId) throws RuntimeException;

   public String deleteUserById(Integer userId) throws RuntimeException;

   public String addUser(InputUserDto user);

   public String updateUser(Integer userId, InputUserDto userUpdate) throws RuntimeException;

   public String tradingApi(Integer userAccountId, Integer stockId, Integer quantity, TransactionType transactionType) throws RuntimeException;

   public UserPortfolioDetailsDto getPortfolioOfUser(Integer userAccountId) throws RuntimeException;


}
