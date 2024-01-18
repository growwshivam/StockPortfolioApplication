package com.example.StocksPortfolioApplication.service;

import com.example.StocksPortfolioApplication.dto.StockUpdatingDto;
import com.example.StocksPortfolioApplication.model.Stock;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;
import java.util.Optional;

@Component
public interface StockService {
    public Optional<Stock> getStockById(Integer stockId) throws RuntimeException;

    public List<Stock> getAllStocks() throws RuntimeException;

    public Optional<String> deleteStockById(Integer stockId) throws RuntimeException;

    public String updateStockByStockId(Integer stockId, Stock stockToBeUpdated) throws RuntimeException;

    public String addStock(Stock stockToBeAdded) throws RuntimeException;

    public String updateListOfStocks(List<Stock> listOfAllStocksToBeUpdated) throws RuntimeException;

    public String updateAllStocks(MultipartFile file) throws IOException, RuntimeException;
}
