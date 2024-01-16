package com.example.StocksPortfolioApplication.controllers;

import com.example.StocksPortfolioApplication.model.User;
import com.example.StocksPortfolioApplication.service.UserService;
import com.example.StocksPortfolioApplication.service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/createUser")
    ResponseEntity<String> createUser(@RequestBody User userToBeAdded){
        try{

            return new ResponseEntity<>(userService.addUser(userToBeAdded), HttpStatus.CREATED);

        }catch(Exception E){
            return new ResponseEntity<>(E.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getUser/{userId}")
    ResponseEntity<? extends Object> getUser(@PathVariable Integer userId){
        try{
            //User user=userService.getUserById(userId).orElseThrow(()->new Exception("No Such User Exits in the table"));
            return new ResponseEntity<>(userService.getUserById(userId).get(),HttpStatus.ACCEPTED);
        }catch(Exception E){
            return new ResponseEntity<>(E.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteUser/{userId}")
    ResponseEntity<? extends Object> deleteUser(@PathVariable Integer userId){
        try{
            return new ResponseEntity<>(userService.deleteUserById(userId),HttpStatus.ACCEPTED);
        }catch(Exception E){
            return new ResponseEntity<>(E.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
