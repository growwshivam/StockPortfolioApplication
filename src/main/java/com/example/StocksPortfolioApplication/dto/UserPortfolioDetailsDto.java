package com.example.StocksPortfolioApplication.dto;


import org.springframework.stereotype.Component;

import java.util.List;


public class UserPortfolioDetailsDto {

    private List<StockDetailsDto> Holdings;
    private Double TotalPortfolioHolding;
    private Double TotalBuyPrice;
    private Double profitAndLoss;
    private Double profitAndLossPercentage;

    public UserPortfolioDetailsDto(){

    }
    public UserPortfolioDetailsDto(List<StockDetailsDto> holdings, Double totalPortfolioHolding, Double totalBuyPrice, Double profitAndLoss, Double profitAndLossPercentage) {
        Holdings = holdings;
        TotalPortfolioHolding = totalPortfolioHolding;
        TotalBuyPrice = totalBuyPrice;
        this.profitAndLoss = profitAndLoss;
        this.profitAndLossPercentage = profitAndLossPercentage;
    }

    public List<StockDetailsDto> getHoldings() {
        return Holdings;
    }

    public void setHoldings(List<StockDetailsDto> holdings) {
        Holdings = holdings;
    }

    public Double getTotalPortfolioHolding() {
        return TotalPortfolioHolding;
    }

    public void setTotalPortfolioHolding(Double totalPortfolioHolding) {
        TotalPortfolioHolding = totalPortfolioHolding;
    }

    public Double getTotalBuyPrice() {
        return TotalBuyPrice;
    }

    public void setTotalBuyPrice(Double totalBuyPrice) {
        TotalBuyPrice = totalBuyPrice;
    }

    public Double getProfitAndLoss() {
        return profitAndLoss;
    }

    public void setProfitAndLoss(Double profitAndLoss) {
        this.profitAndLoss = profitAndLoss;
    }

    public Double getProfitAndLossPercentage() {
        return profitAndLossPercentage;
    }

    public void setProfitAndLossPercentage(Double profitAndLossPercentage) {
        this.profitAndLossPercentage = profitAndLossPercentage;
    }
}
