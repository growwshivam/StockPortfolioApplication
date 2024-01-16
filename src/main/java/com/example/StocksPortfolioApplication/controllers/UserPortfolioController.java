package com.example.StocksPortfolioApplication.controllers;

import com.example.StocksPortfolioApplication.dto.TransactionInputTypeDto;
import com.example.StocksPortfolioApplication.service.UserPortfolioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usersPortfolio")
public class UserPortfolioController {

    private UserPortfolioService userPortfolioService;

    public UserPortfolioController(UserPortfolioService userPortfolioService) {
        this.userPortfolioService = userPortfolioService;
    }

    @PostMapping("/makeTrading")
    public ResponseEntity<? extends Object> makeTransaction(@RequestBody TransactionInputTypeDto transactionInputTypeDto){
        try{
            return new ResponseEntity<>(userPortfolioService.makeTrading(transactionInputTypeDto.getUserAccountId(), transactionInputTypeDto.getStockId(), transactionInputTypeDto.getTransactionType(), transactionInputTypeDto.getQuantity()), HttpStatus.CREATED);
        }catch(Exception E){
        return new ResponseEntity<>(E.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getPortfolio/{userId}")
    public ResponseEntity<? extends Object> findPortfolioOfUser(@PathVariable Integer userId){
        try{
            return new ResponseEntity<>(userPortfolioService.getAllPortfoliosOfUserById(userId),HttpStatus.ACCEPTED);
        }catch(Exception E){
            return new ResponseEntity<>("No problem",HttpStatus.BAD_REQUEST);
        }
    }
}
