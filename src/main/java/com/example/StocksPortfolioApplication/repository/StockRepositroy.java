package com.example.StocksPortfolioApplication.repository;


import com.example.StocksPortfolioApplication.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepositroy extends JpaRepository<Stock,Integer> {

    List<Stock> findAll();
    Optional<Stock> findStockByStockId(Integer stockId);

    Optional<Stock> findStockByName(String name);


    void deleteStockByStockId(Integer stockId);

}
