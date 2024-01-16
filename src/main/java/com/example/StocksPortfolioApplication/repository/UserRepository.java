package com.example.StocksPortfolioApplication.repository;


import com.example.StocksPortfolioApplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findUserByUserAccountId(Integer userAccountId);
    List<User> findAll();
    Optional<User> findUserByName(String Name);

    void deleteUserByUserAccountId(Integer userAccountId);
}
