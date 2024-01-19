package com.example.StocksPortfolioApplication.model;


import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.Objects;

@Table(name = "users_transaction_details")
@Entity
public class UserTransactionDetails {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer userTransactionDetailsId;

    private Integer userAccountId;
    private Integer stockId;
    private LocalTime timeStamp;
    private TransactionType transactionType;
    private Integer quantity;
    private Double stockPrice;

    public UserTransactionDetails() {

    }

    public UserTransactionDetails(Integer userAccountId, Integer stockId, LocalTime timeStamp, TransactionType transactionType, Integer quantity, Double stockPrice) {
        this.userAccountId = userAccountId;
        this.stockId = stockId;
        this.timeStamp = timeStamp;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.stockPrice = stockPrice;
    }

    public Integer getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Integer userAccountId) {
        this.userAccountId = userAccountId;
    }

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public LocalTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(Double stockPrice) {
        this.stockPrice = stockPrice;
    }


}
