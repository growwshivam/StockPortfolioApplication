package com.example.StocksPortfolioApplication.repository;



import com.example.StocksPortfolioApplication.model.TransactionType;
import com.example.StocksPortfolioApplication.model.UserTransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository

public interface UserTransactionDetailsRepositroy extends JpaRepository<UserTransactionDetails, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM users_transaction_details WHERE user_account_id=:userAccountId")
    public List<UserTransactionDetails> findUserTransactionDetailsByUserAccountId(@Param("userAccountId") Integer userAccountId);

    public Optional<UserTransactionDetails> findUserTransactionDetailsByUserAccountIdAndStockIdAndTransactionType(Integer userAccountId, Integer stockId, TransactionType transactionType);

    @Query(nativeQuery = true, value = "SELECT SUM(quantity) AS total_quantity FROM users_transaction_details WHERE user_account_id = :userAccountId AND stock_id = :stockId AND transaction_type = :transactionType GROUP BY user_account_id, stock_id, transaction_type")
    Optional<Integer> getTotalQuantity(@Param("userAccountId") Integer userAccountId, @Param("stockId") Integer stockId, @Param("transactionType") TransactionType transactionType);

    @Query(nativeQuery = true, value = "SELECT SUM(quantity*stock_price) as total_transaction_cost FROM users_transaction_details WHERE user_account_id = :userAccountId AND stock_id = :stockId AND transaction_type = :transactionType GROUP BY user_account_id, stock_id, transaction_type")
    Optional<Double> getTotalTransaction(@Param("userAccountId") Integer userAccountId, @Param("stockId") Integer stockId, @Param("transactionType") TransactionType transactionType);

    @Query(nativeQuery = true, value = "SELECT DISTINCT(stock_id) as list_stocks FROM users_transaction_details WHERE user_account_id = :userAccountId")
    List<Integer> getAllStocks(@Param("userAccountId") Integer userAccountId);


//    @Query(nativeQuery = true, value = "SELECT stock.close_price,stock.open_price,user.quantity FROM stocks stock   join users_transaction_details user on stock.stock_id=user.stock_id")
//    List<TransactionDao> getJoinOperation();
}
