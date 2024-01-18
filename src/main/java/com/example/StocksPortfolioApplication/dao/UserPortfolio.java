package com.example.StocksPortfolioApplication.dao;


import com.example.StocksPortfolioApplication.model.TransactionType;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.Pair;

import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name="users_portofolio")
public class UserPortfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Integer userAccountId;
    private String userName;


    @NotNull
    private Integer stockId;
    private String stockName;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private Integer quantity;

    private Double transactionCost;

    private Double  totalTransactionCost;
    private Double costPerStock;





    private Double profitAndLoss;

    public UserPortfolio(){

    }

//    public UserPortfolio(Integer userAccountId, String userName, Integer stockId, String stockName, TransactionType transactionType, Integer quantity, Double transactionCost, Double totalTransactionCost, Double costPerStock, List<Pair<Double, Double>> profitAndLossList, Double profitAndLoss) {
//        this.userAccountId = userAccountId;
//        this.userName = userName;
//        this.stockId = stockId;
//        this.stockName = stockName;
//        this.transactionType = transactionType;
//        this.quantity = quantity;
//        this.transactionCost = transactionCost;
//        this.totalTransactionCost = totalTransactionCost;
//        this.costPerStock = costPerStock;
//        this.profitAndLossList = profitAndLossList;
//        this.profitAndLoss = profitAndLoss;
//    }


    public UserPortfolio(Integer userAccountId, String userName, Integer stockId, String stockName, TransactionType transactionType, Integer quantity, Double transactionCost, Double totalTransactionCost, Double costPerStock, Double profitAndLoss) {
        this.userAccountId = userAccountId;
        this.userName = userName;
        this.stockId = stockId;
        this.stockName = stockName;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.transactionCost = transactionCost;
        this.totalTransactionCost = totalTransactionCost;
        this.costPerStock = costPerStock;
        this.profitAndLoss = profitAndLoss;
    }


    public Double getTransactionCost() {
        return transactionCost;
    }

    public void setTransactionCost(Double transactionCost) {
        this.transactionCost = transactionCost;
    }

    public Double getTotalTransactionCost() {
        return totalTransactionCost;
    }

    public void setTotalTransactionCost(Double totalTransactionCost) {
        this.totalTransactionCost = totalTransactionCost;
    }

//    public List<Pair<Double, Double>> getProfitAndLossList() {
//        return profitAndLossList;
//    }
//
//    public void setProfitAndLossList(List<Pair<Double, Double>> profitAndLossList) {
//        this.profitAndLossList = profitAndLossList;
//    }

    public Double getProfitAndLoss() {
        return profitAndLoss;
    }

    public void setProfitAndLoss(Double profitAndLoss) {
        this.profitAndLoss = profitAndLoss;
    }

    public Double getCostPerStock() {
        return costPerStock;
    }

    public void setCostPerStock(Double costPerStock) {
        this.costPerStock = costPerStock;
    }

    public Integer getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Integer userAccountId) {
        this.userAccountId = userAccountId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
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


}
