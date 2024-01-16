package com.example.StocksPortfolioApplication.dto;

import org.springframework.stereotype.Component;


public class StockDetailsDto {


    private String stockName;
    private Integer stockId;
    private Integer quantity;
    private Double buyPrice;
    private Double currentPrice;

    private Double diffInCurrentAndBuyPrice;

    public StockDetailsDto(){

    }
    public StockDetailsDto(String stockName, Integer stockId, Integer quantity, Double buyPrice, Double currentPrice, Double diffInCurrentAndBuyPrice) {
        this.stockName = stockName;
        this.stockId = stockId;
        this.quantity = quantity;
        this.buyPrice = buyPrice;
        this.currentPrice = currentPrice;
        this.diffInCurrentAndBuyPrice = diffInCurrentAndBuyPrice;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
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

    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Double getDiffInCurrentAndBuyPrice() {
        return diffInCurrentAndBuyPrice;
    }

    public void setDiffInCurrentAndBuyPrice(Double diffInCurrentAndBuyPrice) {
        this.diffInCurrentAndBuyPrice = diffInCurrentAndBuyPrice;
    }
}
