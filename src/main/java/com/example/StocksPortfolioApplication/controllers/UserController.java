package com.example.StocksPortfolioApplication.controllers;

import com.example.StocksPortfolioApplication.dto.InputUserDto;
import com.example.StocksPortfolioApplication.dto.TransactionInputTypeDto;
import com.example.StocksPortfolioApplication.model.User;
import com.example.StocksPortfolioApplication.service.UserService;
import com.example.StocksPortfolioApplication.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping ("/stockPortfolioApp/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/createUser")
    ResponseEntity<String> createUser(@Valid @RequestBody InputUserDto userToBeAdded) {

        try{

            return new ResponseEntity<>(userService.addUser(userToBeAdded), HttpStatus.CREATED);

        } catch (RuntimeException E) {

            return new ResponseEntity<>(E.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getUser/{userId}")
    ResponseEntity<? extends Object> getUser(@PathVariable Integer userId){
        try{

            return new ResponseEntity<>(userService.getUserById(userId).get(),HttpStatus.ACCEPTED);

        } catch (RuntimeException E) {

            return new ResponseEntity<>(E.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteUser/{userId}")
    ResponseEntity<? extends Object> deleteUser(@PathVariable Integer userId){
        try{
            return new ResponseEntity<>(userService.deleteUserById(userId),HttpStatus.ACCEPTED);

        } catch (RuntimeException E) {

            return new ResponseEntity<>(E.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/makeTrading")
    public ResponseEntity<? extends Object> makeTransaction(@Valid @RequestBody TransactionInputTypeDto transactionInputTypeDto) {
        try {

            return new ResponseEntity<>(userService.tradingApi(transactionInputTypeDto.getUserAccountId(), transactionInputTypeDto.getStockId(), transactionInputTypeDto.getQuantity(), transactionInputTypeDto.getTransactionType()), HttpStatus.CREATED);
        } catch (RuntimeException E) {

            return new ResponseEntity<>(E.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getPortfolio/{userId}")
    public ResponseEntity<? extends Object> findPortfolioOfUser(@PathVariable Integer userId) {
        try {

            return new ResponseEntity<>(userService.getPortfolioOfUser(userId), HttpStatus.ACCEPTED);
        } catch (RuntimeException E) {

            return new ResponseEntity<>(E.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
