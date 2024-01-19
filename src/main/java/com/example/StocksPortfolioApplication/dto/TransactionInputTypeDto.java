package com.example.StocksPortfolioApplication.dto;


import com.example.StocksPortfolioApplication.model.TransactionType;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;


@EqualsAndHashCode
public class TransactionInputTypeDto {
    @NotBlank
    private Integer userAccountId;

    @javax.validation.constraints.NotBlank
    private Integer stockId;

    @NotBlank
    private Integer quantity;

    @NotBlank
    private TransactionType transactionType;

    public TransactionInputTypeDto(){

    }
    public TransactionInputTypeDto(Integer userAccountId, Integer stockId, Integer quantity, TransactionType transactionType) {
        this.userAccountId = userAccountId;
        this.stockId = stockId;
        this.quantity = quantity;
        this.transactionType = transactionType;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
