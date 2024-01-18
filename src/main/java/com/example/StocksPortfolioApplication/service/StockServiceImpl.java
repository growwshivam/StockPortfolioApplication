package com.example.StocksPortfolioApplication.service;


import com.example.StocksPortfolioApplication.dto.StockUpdatingDto;
import com.example.StocksPortfolioApplication.model.Stock;
import com.example.StocksPortfolioApplication.repository.StockRepositroy;
import com.example.StocksPortfolioApplication.utils.GenerateStockToBeUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Service
public class StockServiceImpl  implements StockService{

    private StockRepositroy stockRepositroy;
    private GenerateStockToBeUpdate generateStockToBeUpdate;

    public StockServiceImpl(StockRepositroy stockRepositroy, GenerateStockToBeUpdate generateStockToBeUpdate) {
        this.stockRepositroy = stockRepositroy;
        this.generateStockToBeUpdate = generateStockToBeUpdate;
    }

    public Optional<Stock> getStockById(Integer stockId) throws RuntimeException {

        Optional<Stock> currentStockDetails=stockRepositroy.findStockByStockId(stockId);

        if(currentStockDetails.isPresent()){

            return currentStockDetails;
        }

        throw new RuntimeException("No Such StockId Exists in the Table");
    }

    public List<Stock> getAllStocks() throws RuntimeException {

        return stockRepositroy.findAll();
    }

    public Optional<String> deleteStockById(Integer stockId) throws RuntimeException {

        Optional<Stock> currentStockDetails=stockRepositroy.findStockByStockId(stockId);

        if(currentStockDetails.isPresent()){

            stockRepositroy.deleteStockByStockId(stockId);

            return Optional.of("Current Stock has been deleted Successfully");

        }

        throw new RuntimeException("No Such StockId Exists in the Table So Deletion is Not Possible");
    }

    public String updateStockByStockId(Integer stockId, Stock stockToBeUpdated) throws RuntimeException {

        Optional<Stock> currentStockDetails = stockRepositroy.findStockByStockId(stockId);

       if(currentStockDetails.isPresent()){

         Stock stockDetailsPrevious=currentStockDetails.get();

           stockDetailsPrevious.setClosePrice(stockToBeUpdated.getClosePrice());
         stockDetailsPrevious.setHighPrice(stockToBeUpdated.getHighPrice());
         stockDetailsPrevious.setCurrentPrice(stockToBeUpdated.getCurrentPrice());
           stockDetailsPrevious.setHighPrice(stockToBeUpdated.getHighPrice());
           stockDetailsPrevious.setLowPrice(stockToBeUpdated.getLowPrice());
           stockDetailsPrevious.setQuantity(stockToBeUpdated.getQuantity());
         stockRepositroy.save(stockDetailsPrevious);
          return "Stock has been Updated Successfully";

       }

       return addStock(stockToBeUpdated);

    }

    public String addStock(Stock stockToBeAdded) throws RuntimeException {
        stockRepositroy.save(stockToBeAdded);

        return "Stock has been Added Successfully";
    }

    public String updateListOfStocks(List<Stock> listOfAllStocksToBeUpdated) throws RuntimeException {

        for(Stock stock:listOfAllStocksToBeUpdated){

            updateStockByStockId(stock.getStockId(), stock);
        }

        return "updation of all stocks is done successfully";
    }


    public String updateAllStocks(MultipartFile file) throws IOException, RuntimeException {

        CompletableFuture<String> future = generateStockToBeUpdate.generateStockToBeUpdateAsync(file)

                .thenApplyAsync(listOfAllStocks -> {

                    try {

                        return updateListOfStocks(listOfAllStocks);

                    } catch (RuntimeException e) {

                        throw new RuntimeException(e);
                    }
                });

        try {
            return future.get();

        } catch (InterruptedException | ExecutionException e) {

            Throwable cause = (e.getCause() != null) ? e.getCause() : e;

            return "Error processing file: " + cause.getMessage();
        }
    }
}

