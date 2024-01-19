package com.example.StocksPortfolioApplication.service;

import com.example.StocksPortfolioApplication.model.Stock;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;
import java.util.Optional;

@Component
public interface StockService {
    public Optional<Stock> findStockById(Integer stockId) throws RuntimeException;

    public List<Stock> getAllStocksList() throws RuntimeException;

    public Optional<String> deleteStock(Integer stockId) throws RuntimeException;

    public String updateStock(Integer stockId, Stock updatedStock) throws RuntimeException;

    public String addNewStock(Stock newStock) throws RuntimeException;

    public String updateStocksList(List<Stock> updatedStocks) throws RuntimeException;

    public String updateStocksFromFile(MultipartFile file) throws IOException, RuntimeException;
}
