package com.example.StocksPortfolioApplication.service;


import com.example.StocksPortfolioApplication.dto.StockUpdatingDto;
import com.example.StocksPortfolioApplication.model.Stock;
import com.example.StocksPortfolioApplication.repository.StockRepositroy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockServiceImpl  implements StockService{

    private StockRepositroy stockRepositroy;

    public StockServiceImpl(StockRepositroy stockRepositroy) {
        this.stockRepositroy = stockRepositroy;
    }


  public  Optional<Stock> getStockById(Integer stockId) throws Exception{
        Optional<Stock> currentStockDetails=stockRepositroy.findStockByStockId(stockId);
        if(currentStockDetails.isPresent()){
            return currentStockDetails;
        }
        throw new Exception("No Such StockId Exists in the Table");
    }

   public List<Stock> getAllStocks()throws Exception{
        return stockRepositroy.findAll();
    }

  public  Optional<String> deleteStockById(Integer stockId) throws Exception{
        Optional<Stock> currentStockDetails=stockRepositroy.findStockByStockId(stockId);
        if(currentStockDetails.isPresent()){
            stockRepositroy.deleteStockByStockId(stockId);
          return  Optional.of("Current Stock has been deleted Successfully");

        }
        throw new Exception("No Such StockId Exists in the Table So Deletion is Not Possible");
    }

  public  String updateStockByStockId(Integer stockId,Stock stockToBeUpdated) throws Exception{
       Optional<Stock> currentStockDetails=stockRepositroy.findStockByStockId(stockId);
       if(currentStockDetails.isPresent()){
         Stock stockDetailsPrevious=currentStockDetails.get();
         stockDetailsPrevious.setClosePrice(stockToBeUpdated.getClosePrice());
         stockDetailsPrevious.setHighPrice(stockToBeUpdated.getHighPrice());
         stockDetailsPrevious.setCurrentPrice(stockToBeUpdated.getCurrentPrice());
//         stockDetailsPrevious.setLastPrice(stockToBeUpdated.getLastPrice());
//         stockDetailsPrevious.setPrevClosePrice(stockToBeUpdated.getPrevClosePrice());
         stockRepositroy.save(stockDetailsPrevious);
          return "Stock has been Updated Successfully";

       }

       return addStock(stockToBeUpdated);

    }

   public  String addStock(Stock stockToBeAdded) throws Exception{
        stockRepositroy.save(stockToBeAdded);
        return "Stock has been Added Successfully";
    }

    public String updateListOfStocks(List<Stock> listOfAllStocksToBeUpdated) throws Exception{
        for(Stock stock:listOfAllStocksToBeUpdated){
            updateStockByStockId(stock.getStockId(), stock);
        }

        return "updation of all stocks is done successfully";
    }

//    public void updateStockByStockDto(Integer stockId,StockUpdatingDto stockUpdatingDto){
//
//    }

}
