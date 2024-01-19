package com.example.StocksPortfolioApplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name="stocks")

public class Stock {
    @Id
    private Integer stockId;


    private Double openPrice;


    private Double closePrice;


    private Double highPrice;


    private Double lowPrice;

//    private Double prevClosePrice;
//    private Double lastPrice;

    private Integer quantity;


    private String name;


    private Double currentPrice;


    public Stock(){

    }

    public Stock(Integer stockId, Double openPrice, Double closePrice, Double highPrice, Double lowPrice, Integer quantity, String name, Double currentPrice) {
        this.stockId = stockId;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.quantity = quantity;
        this.name = name;
        this.currentPrice = currentPrice;
    }

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }

    public Double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Double highPrice) {
        this.highPrice = highPrice;
    }

    public Double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Double lowPrice) {
        this.lowPrice = lowPrice;
    }

//    public Double getPrevClosePrice() {
//        return prevClosePrice;
//    }
//
//    public void setPrevClosePrice(Double prevClosePrice) {
//        this.prevClosePrice = prevClosePrice;
//    }
//
//    public Double getLastPrice() {
//        return lastPrice;
//    }
//
//    public void setLastPrice(Double lastPrice) {
//        this.lastPrice = lastPrice;
//    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }
}
