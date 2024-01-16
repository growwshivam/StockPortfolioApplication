package com.example.StocksPortfolioApplication.service;


import com.example.StocksPortfolioApplication.model.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserService {

   public  Optional<User> getUserById(Integer userId) throws Exception;
   public String deleteUserById(Integer userId) throws  Exception;
   public String addUser(User user);
}
