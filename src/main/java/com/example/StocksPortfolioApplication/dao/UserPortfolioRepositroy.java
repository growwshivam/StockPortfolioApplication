package com.example.StocksPortfolioApplication.dao;

import com.example.StocksPortfolioApplication.model.TransactionType;
import com.example.StocksPortfolioApplication.dao.UserPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPortfolioRepositroy extends JpaRepository<UserPortfolio,Integer> {


    Optional<UserPortfolio> findUserPortfolioByUserAccountIdAndStockId(Integer userAccountId,Integer stockId);
    Optional<UserPortfolio> findUserPortfolioByUserAccountIdAndStockIdAndTransactionType(Integer userAccountId, Integer stockId, TransactionType transactionType);

    List<UserPortfolio> findUserPortfolioByUserAccountId(Integer userAccountId);

    void deleteUserPortfolioByUserAccountIdAndStockId(Integer userAccountId,Integer stockId);



}
