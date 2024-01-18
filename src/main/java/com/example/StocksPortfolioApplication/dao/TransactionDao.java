package com.example.StocksPortfolioApplication.dao;

public class TransactionDao {
    private Double closePrice;
    private Double openPrice;
    private Integer quantity;

    public TransactionDao(Double closePrice, Double openPrice, Integer quantity) {
        this.closePrice = closePrice;
        this.openPrice = openPrice;
        this.quantity = quantity;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }

    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
