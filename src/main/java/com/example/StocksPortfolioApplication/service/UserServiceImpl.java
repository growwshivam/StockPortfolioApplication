package com.example.StocksPortfolioApplication.service;

import com.example.StocksPortfolioApplication.model.User;
import com.example.StocksPortfolioApplication.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl  implements UserService{

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

  public  Optional<User> getUserById(Integer userAccountId) throws Exception{
        Optional<User> currentUserDetails=userRepository.findUserByUserAccountId(userAccountId);
        if(currentUserDetails.isPresent()){
            return currentUserDetails;
        }
        throw new Exception("No Such User AccountId Does not exists in the user table");
    }

    public String deleteUserById(Integer userAccountId)throws Exception{
        Optional<User> currentUserDetails=userRepository.findUserByUserAccountId(userAccountId);
        if(currentUserDetails.isPresent()){
            userRepository.deleteUserByUserAccountId(userAccountId);
            return "Deletion of User has been successfully completed";
        }

        throw new Exception("No Such User AccountId didnot exists in the user table So deletion is not possible");
    }

   public String addUser(User userToAdded){
        userRepository.save(userToAdded);
        return "User has been added successfully to the table";
    }

}
