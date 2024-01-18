package com.example.StocksPortfolioApplication.controllers;

import com.example.StocksPortfolioApplication.dto.TransactionInputTypeDto;
import com.example.StocksPortfolioApplication.service.UserTransactionDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usersPortfolio")
public class UserPortfolioController {

    //    private UserPortfolioService userPortfolioService;
    private UserTransactionDetailsService userTransactionDetailsService;

    public UserPortfolioController(UserTransactionDetailsService userTransactionDetailsService) {
//        this.userPortfolioService = userPortfolioService;
        this.userTransactionDetailsService = userTransactionDetailsService;
    }

//    @PostMapping("/makeTrading")
//    public ResponseEntity<? extends Object> makeTransaction(@RequestBody TransactionInputTypeDto transactionInputTypeDto){
//        try{
//            return new ResponseEntity<>(userTransactionDetailsService.tradeApiService(transactionInputTypeDto.getUserAccountId(), transactionInputTypeDto.getStockId(),  transactionInputTypeDto.getQuantity(),transactionInputTypeDto.getTransactionType()), HttpStatus.CREATED);
//        }catch(RuntimeException E){
//        return new ResponseEntity<>(E.getMessage(),HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @GetMapping("/getPortfolio/{userId}")
//    public ResponseEntity<? extends Object> findPortfolioOfUser(@PathVariable Integer userId){
//        try{
//            return new ResponseEntity<>(userTransactionDetailsService.getPortfolioOfUser(userId),HttpStatus.ACCEPTED);
//        }catch(RuntimeException E){
//            return new ResponseEntity<>(E.getMessage(),HttpStatus.BAD_REQUEST);
//        }
//    }
}
