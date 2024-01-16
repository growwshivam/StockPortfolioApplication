package com.example.StocksPortfolioApplication.service;

import com.example.StocksPortfolioApplication.dto.StockUpdatingDto;
import com.example.StocksPortfolioApplication.model.Stock;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface StockService {
    public Optional<Stock> getStockById(Integer stockId) throws Exception;
    public List<Stock> getAllStocks()throws Exception;
    public  Optional<String> deleteStockById(Integer stockId) throws Exception;
    public  String updateStockByStockId(Integer stockId,Stock stockToBeUpdated) throws Exception;
    public  String addStock(Stock stockToBeAdded) throws Exception;

    public String updateListOfStocks(List<Stock> listOfAllStocksToBeUpdated) throws Exception;
}
