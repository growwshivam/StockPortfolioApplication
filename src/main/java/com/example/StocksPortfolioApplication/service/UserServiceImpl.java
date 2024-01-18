package com.example.StocksPortfolioApplication.service;

import com.example.StocksPortfolioApplication.dto.InputUserDto;
import com.example.StocksPortfolioApplication.dto.UserPortfolioDetailsDto;
import com.example.StocksPortfolioApplication.model.Stock;
import com.example.StocksPortfolioApplication.model.TransactionType;
import com.example.StocksPortfolioApplication.model.User;
import com.example.StocksPortfolioApplication.model.UserTransactionDetails;
import com.example.StocksPortfolioApplication.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl  implements UserService{

    private UserRepository userRepository;
    private UserTransactionDetailsService userTransactionDetailsService;

    public UserServiceImpl(UserRepository userRepository, UserTransactionDetailsService userTransactionDetailsService) {
        this.userRepository = userRepository;

        this.userTransactionDetailsService = userTransactionDetailsService;
    }

    public Optional<User> getUserById(Integer userAccountId) throws RuntimeException {

        Optional<User> currentUserDetails=userRepository.findUserByUserAccountId(userAccountId);

        if(currentUserDetails.isPresent()){

            return currentUserDetails;
        }

        throw new RuntimeException("No Such User AccountId Does not exists in the user table");
    }

    public String deleteUserById(Integer userAccountId) throws RuntimeException {

        Optional<User> currentUserDetails=userRepository.findUserByUserAccountId(userAccountId);

        if(currentUserDetails.isPresent()){

            userRepository.deleteUserByUserAccountId(userAccountId);

            return "Deletion of User has been successfully completed";
        }

        throw new RuntimeException("No Such User AccountId didnot exists in the user table So deletion is not possible");
    }

    public String addUser(InputUserDto userToAdded) {

        userRepository.save(new User(userToAdded.getName(), userToAdded.getPhoneNumber(), userToAdded.getEmail(), userToAdded.getBalance()));

        return "User has been added successfully to the table";
    }


    public void updateUser(User user) {
        userRepository.save(user);
    }

    public String tradingApi(Integer userAccountId, Integer stockId, Integer quantity, TransactionType transactionType) throws RuntimeException {

        Optional<User> currentTransactionMakingUser = userRepository.findUserByUserAccountId(userAccountId);

        if (currentTransactionMakingUser.isEmpty()) {

            throw new RuntimeException("No such user exists in the table");
        }


        userTransactionDetailsService.tradeApiService(currentTransactionMakingUser.get(), stockId, quantity, transactionType);

        userRepository.save(currentTransactionMakingUser.get());

        switch (transactionType) {
            case BUY:

                return "Transaction of buying of stocks has been created successfully";
            case SELL:

                return "Transaction of selling of stocks has been created successfully";
            default:

                return "Transaction failure";
        }
    }

    public UserPortfolioDetailsDto getPortfolioOfUser(Integer userAccountId) throws RuntimeException {

        Optional<User> currentTransactionMakingUser = userRepository.findUserByUserAccountId(userAccountId);

        if (currentTransactionMakingUser.isEmpty()) {

            throw new RuntimeException("No such user exists in the table");
        }

        return userTransactionDetailsService.getPortfolioOfUser(userAccountId);
    }


}
