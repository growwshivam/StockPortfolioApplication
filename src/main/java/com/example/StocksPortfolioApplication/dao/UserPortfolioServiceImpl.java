package com.example.StocksPortfolioApplication.dao;


import com.example.StocksPortfolioApplication.dao.UserPortfolioService;
import com.example.StocksPortfolioApplication.dto.StockDetailsDto;
import com.example.StocksPortfolioApplication.dto.UserPortfolioDetailsDto;
import com.example.StocksPortfolioApplication.model.Stock;
import com.example.StocksPortfolioApplication.model.TransactionType;
import com.example.StocksPortfolioApplication.model.User;
import com.example.StocksPortfolioApplication.dao.UserPortfolio;
import com.example.StocksPortfolioApplication.dao.UserPortfolioRepositroy;
import com.example.StocksPortfolioApplication.service.StockService;
import com.example.StocksPortfolioApplication.service.UserService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserPortfolioServiceImpl implements UserPortfolioService {

    private UserPortfolioRepositroy userPortfolioRepositroy;

    private StockService stockService;
    private UserService userService;

    public UserPortfolioServiceImpl(UserPortfolioRepositroy userPortfolioRepositroy, StockService stockService, UserService userService) {
        this.userPortfolioRepositroy = userPortfolioRepositroy;
        this.stockService = stockService;
        this.userService = userService;
    }

    public String makeTransaction(Integer userAccountId, Integer stockId, TransactionType transactionType, Integer quantity) throws RuntimeException {
        Optional<UserPortfolio> currentUserPortfolio=userPortfolioRepositroy.findUserPortfolioByUserAccountIdAndStockIdAndTransactionType(userAccountId,stockId,transactionType);
        Optional<User> currentUser=userService.getUserById(userAccountId);
        Optional<Stock> currentStock=stockService.getStockById(stockId);
        if(!currentUser.isPresent()){
            throw new RuntimeException("Transaction is not Possible As No User exists in the table");
        }
        if(!currentStock.isPresent()){
            throw new RuntimeException("Transaction not possible as no such stock exists");
        }
        if(currentUserPortfolio.isPresent()){
            if(transactionType==TransactionType.SELL){
                Optional<UserPortfolio> currentUserPortfolioInBuying=userPortfolioRepositroy.findUserPortfolioByUserAccountIdAndStockIdAndTransactionType(userAccountId,stockId,TransactionType.BUY);
                if(currentUserPortfolioInBuying.get().getQuantity()<quantity){
                    throw new RuntimeException("Transaction of selling is not possible");
                }
                UserPortfolio actualUserPortfolioInBuying=currentUserPortfolioInBuying.get();
                actualUserPortfolioInBuying.setQuantity(actualUserPortfolioInBuying.getQuantity()-quantity);
                actualUserPortfolioInBuying.setTransactionCost(actualUserPortfolioInBuying.getTransactionCost()-quantity* actualUserPortfolioInBuying.getCostPerStock());
                actualUserPortfolioInBuying.setTotalTransactionCost(actualUserPortfolioInBuying.getTotalTransactionCost()+quantity* actualUserPortfolioInBuying.getCostPerStock());
                userPortfolioRepositroy.save(actualUserPortfolioInBuying);
                UserPortfolio actualUserPortfolioInSelling=currentUserPortfolio.get();
                actualUserPortfolioInSelling.setQuantity(actualUserPortfolioInSelling.getQuantity()+quantity);
                actualUserPortfolioInSelling.setTransactionCost(actualUserPortfolioInSelling.getTransactionCost()+currentStock.get().getCurrentPrice()*quantity);
                actualUserPortfolioInSelling.setTotalTransactionCost(actualUserPortfolioInSelling.getTotalTransactionCost()+currentStock.get().getCurrentPrice()*quantity-actualUserPortfolioInBuying.getCostPerStock()*quantity);
                actualUserPortfolioInBuying.setCostPerStock(actualUserPortfolioInSelling.getTransactionCost()/ actualUserPortfolioInSelling.getQuantity());
                actualUserPortfolioInSelling.setProfitAndLoss(actualUserPortfolioInSelling.getProfitAndLoss()+(currentStock.get().getCurrentPrice()-actualUserPortfolioInBuying.getCostPerStock()*quantity));
//                List<Pair<Double,Double>> currentProfitAndLossList=actualUserPortfolioInSelling.getProfitAndLossList();
//                currentProfitAndLossList.add(new Pair<>(currentStock.get().getCurrentPrice()-actualUserPortfolioInBuying.getCostPerStock()*quantity,actualUserPortfolioInBuying.getCostPerStock()*quantity));
//                actualUserPortfolioInSelling.setProfitAndLossList(currentProfitAndLossList);
                userPortfolioRepositroy.save(actualUserPortfolioInSelling);
                return "Transaction of selling stock has been successfully completed";

            }
            else{
                UserPortfolio actualUserPortfolioInBuying=currentUserPortfolio.get();
                actualUserPortfolioInBuying.setQuantity(actualUserPortfolioInBuying.getQuantity()+quantity);
                actualUserPortfolioInBuying.setTransactionCost(actualUserPortfolioInBuying.getTransactionCost()+currentStock.get().getCurrentPrice()*quantity);
                actualUserPortfolioInBuying.setTotalTransactionCost(actualUserPortfolioInBuying.getTotalTransactionCost()+quantity*currentStock.get().getCurrentPrice());
                actualUserPortfolioInBuying.setCostPerStock(actualUserPortfolioInBuying.getTransactionCost()/ actualUserPortfolioInBuying.getQuantity());
                userPortfolioRepositroy.save(actualUserPortfolioInBuying);
                return "Transaction of Buying has been successful";

            }
        }

        else if(transactionType==TransactionType.SELL){
            Optional<UserPortfolio> currentUserPortfolioInBuying=userPortfolioRepositroy.findUserPortfolioByUserAccountIdAndStockIdAndTransactionType(userAccountId,stockId,TransactionType.BUY);
            if(currentUserPortfolioInBuying.isPresent()){
                if(quantity> currentUserPortfolioInBuying.get().getQuantity()){
                    throw new RuntimeException("Transaction is not possible as quantity of selling is greater than buying");
                }
                UserPortfolio actualUserPortfolioInBuying=currentUserPortfolioInBuying.get();
                actualUserPortfolioInBuying.setQuantity(actualUserPortfolioInBuying.getQuantity()-quantity);
                actualUserPortfolioInBuying.setTransactionCost(actualUserPortfolioInBuying.getTransactionCost()-quantity* actualUserPortfolioInBuying.getCostPerStock());
                userPortfolioRepositroy.save(actualUserPortfolioInBuying);
                List<Pair<Double,Double>> newProfitAndLossList=new ArrayList<>();
                newProfitAndLossList.add(new Pair<>(currentStock.get().getCurrentPrice()*quantity-currentUserPortfolioInBuying.get().getCostPerStock()*quantity,currentUserPortfolioInBuying.get().getCostPerStock()));
                userPortfolioRepositroy.save(new UserPortfolio(userAccountId,currentUser.get().getName(),stockId,currentStock.get().getName(),TransactionType.SELL,quantity,currentStock.get().getCurrentPrice()*quantity,currentStock.get().getCurrentPrice()*quantity,currentStock.get().getCurrentPrice(),currentStock.get().getCurrentPrice()*quantity-currentUserPortfolioInBuying.get().getCostPerStock()*quantity));
                return "Transaction of selling stock has been successfully completed";
            }

            throw new RuntimeException("Transaction of selling is not possible as no such stock exists in the buying section of user");

        }
        else{
            userPortfolioRepositroy.save(new UserPortfolio(userAccountId,currentUser.get().getName(),stockId,currentStock.get().getName(),TransactionType.BUY,quantity,currentStock.get().getCurrentPrice()*quantity,currentStock.get().getCurrentPrice()*quantity,currentStock.get().getCurrentPrice(),0.0));
            return "Transaction of buying stock has been successfully completed";
        }
    }

    public UserPortfolioDetailsDto getAllPortfoliosOfUserById(Integer userAccountId) throws RuntimeException {

        List<UserPortfolio> allPortfoliosOfCurrentUser=userPortfolioRepositroy.findUserPortfolioByUserAccountId(userAccountId);
        List<StockDetailsDto> listOfAllStocksPresent=new ArrayList<>();

        Double totalPortfolioHolding=0.0;
        Double totalBuyPrice=0.0;
        Double totalProfitLoss=0.0;
        Double totalBuyingCost=0.0;
        for(UserPortfolio userPortfolio:allPortfoliosOfCurrentUser){
            if(userPortfolio.getTransactionType()==TransactionType.BUY && userPortfolio.getQuantity()>0){
                Stock currentStock=stockService.getStockById(userPortfolio.getStockId()).get();
                Double currentPriceOfStock=currentStock.getCurrentPrice();
                listOfAllStocksPresent.add(new StockDetailsDto(userPortfolio.getStockName(),userPortfolio.getStockId(),userPortfolio.getQuantity(),userPortfolio.getCostPerStock(),currentPriceOfStock,currentPriceOfStock-userPortfolio.getCostPerStock()));
                totalPortfolioHolding+=currentPriceOfStock*userPortfolio.getQuantity();
                totalBuyPrice+= userPortfolio.getTransactionCost();
            }
            else if(userPortfolio.getTransactionType()==TransactionType.SELL){
                totalProfitLoss+=userPortfolio.getProfitAndLoss();
                totalBuyingCost+=userPortfolio.getTotalTransactionCost();
            }
        }

        return new UserPortfolioDetailsDto(listOfAllStocksPresent,totalPortfolioHolding,totalBuyPrice,totalProfitLoss,(totalProfitLoss/totalBuyingCost)*100);



    }

    public String makeTrading(Integer userAccountId, Integer stockId, TransactionType transactionType, Integer quantity) throws RuntimeException {
        Optional<UserPortfolio> currentUserPortfolio = userPortfolioRepositroy.findUserPortfolioByUserAccountIdAndStockIdAndTransactionType(userAccountId, stockId, transactionType);
        Optional<User> currentUser = userService.getUserById(userAccountId);
        Optional<Stock> currentStock = stockService.getStockById(stockId);

        if (!currentUser.isPresent()) {
            throw new RuntimeException("Transaction is not possible as no user exists in the table");
        }

        if (!currentStock.isPresent()) {
            throw new RuntimeException("Transaction not possible as no such stock exists");
        }

        if (currentUserPortfolio.isPresent()) {
            return handleExistingPortfolio(currentUserPortfolio.get(), currentStock.get(), transactionType, quantity);
        } else {
            return handleNewPortfolio(currentUser.get(), currentStock.get(), transactionType, quantity);
        }
    }

    private String handleExistingPortfolio(UserPortfolio existingPortfolio, Stock currentStock, TransactionType transactionType, Integer quantity) throws RuntimeException {
        switch (transactionType) {
            case SELL:
                return handleSellTransaction(existingPortfolio, currentStock, quantity);
            case BUY:
                return handleBuyTransaction(existingPortfolio, currentStock, quantity);
            default:
                throw new RuntimeException("Invalid transaction type");
        }
    }

    private String handleNewPortfolio(User currentUser, Stock currentStock, TransactionType transactionType, Integer quantity) throws RuntimeException {
        switch (transactionType) {
            case BUY:
                return handleBuyTransactionForNewPortfolio(currentUser, currentStock, quantity); case SELL:
                return handleSellTransactionForNewPortfolio(currentUser,currentStock,quantity);
            default:
                throw new RuntimeException("Cannot perform " + transactionType + " on a non-existing portfolio");
        }
    }

    private String handleSellTransaction(UserPortfolio existingPortfolio, Stock currentStock, Integer quantity) throws RuntimeException {

        Optional<UserPortfolio> existingPortfolioInBuying= userPortfolioRepositroy.findUserPortfolioByUserAccountIdAndStockIdAndTransactionType(existingPortfolio.getUserAccountId(), currentStock.getStockId(), TransactionType.BUY);

            if(!existingPortfolioInBuying.isPresent()){
                throw new RuntimeException("Transaction not possible as user has not buy any of these stocks");
            }
            handleBuyTransactionWhenSelling(existingPortfolioInBuying.get(),currentStock,quantity);
            existingPortfolio.setQuantity(existingPortfolio.getQuantity()+quantity);
            existingPortfolio.setTransactionCost(existingPortfolio.getTransactionCost()+quantity* currentStock.getCurrentPrice());
            existingPortfolio.setTotalTransactionCost(existingPortfolio.getTotalTransactionCost()+quantity* existingPortfolioInBuying.get().getCostPerStock());
            existingPortfolio.setProfitAndLoss(existingPortfolio.getProfitAndLoss()+(currentStock.getCurrentPrice()-existingPortfolioInBuying.get().getCostPerStock())*quantity);

            userPortfolioRepositroy.save(existingPortfolio);



        return "Transaction of selling stock has been successfully completed";
    }

    private String handleBuyTransaction(UserPortfolio existingPortfolio, Stock currentStock, Integer quantity) throws RuntimeException {
        existingPortfolio.setQuantity(existingPortfolio.getQuantity() + quantity);
        existingPortfolio.setTransactionCost(existingPortfolio.getTransactionCost()+ currentStock.getCurrentPrice()*quantity);
        existingPortfolio.setTotalTransactionCost(existingPortfolio.getTotalTransactionCost()+currentStock.getCurrentPrice()*quantity);
        existingPortfolio.setCostPerStock(existingPortfolio.getTransactionCost()/ (1.0*existingPortfolio.getQuantity()));
        userPortfolioRepositroy.save(existingPortfolio);
        return "Transaction of buying stock has been successfully completed";
    }

    private String handleBuyTransactionForNewPortfolio(User currentUser, Stock currentStock, Integer quantity) throws RuntimeException {
        UserPortfolio newPortfolio = new UserPortfolio(currentUser.getUserAccountId(), currentUser.getName(), currentStock.getStockId(), currentStock.getName(), TransactionType.BUY, quantity, currentStock.getCurrentPrice() * quantity, currentStock.getCurrentPrice() * quantity, currentStock.getCurrentPrice(), 0.0);
        userPortfolioRepositroy.save(newPortfolio);
        return "Transaction of buying stock has been successfully completed";
    }

    private String handleBuyTransactionWhenSelling(UserPortfolio userPortfolio, Stock currentStock, Integer quantity) throws RuntimeException {
        if(userPortfolio.getQuantity()<quantity){
            throw new RuntimeException("Transaction of selling of stock is not possible");
        }
        userPortfolio.setQuantity(userPortfolio.getQuantity()-quantity);
        userPortfolio.setTransactionCost(userPortfolio.getTransactionCost() - userPortfolio.getCostPerStock()*quantity);
        userPortfolio.setTotalTransactionCost(userPortfolio.getTotalTransactionCost()+currentStock.getCurrentPrice()*quantity);
        userPortfolio.setCostPerStock(userPortfolio.getTransactionCost()/ (1.0* userPortfolio.getQuantity()));
        userPortfolioRepositroy.save(userPortfolio);
        return "Successful  of the selling stock";

    }

    private String handleSellTransactionForNewPortfolio(User currentUser, Stock currentStock, Integer quantity) throws RuntimeException {
        Optional<UserPortfolio> existingPortfolioInBuying= userPortfolioRepositroy.findUserPortfolioByUserAccountIdAndStockIdAndTransactionType(currentUser.getUserAccountId(), currentStock.getStockId(), TransactionType.BUY);

        if(!existingPortfolioInBuying.isPresent()){
            throw new RuntimeException("Transaction not possible as user has not buy any of these stocks");
            }
            handleBuyTransactionWhenSelling(existingPortfolioInBuying.get(),currentStock,quantity);
            Double profitNLoss=(currentStock.getCurrentPrice()-existingPortfolioInBuying.get().getCostPerStock())*quantity;
            UserPortfolio newPortfolio = new UserPortfolio(currentUser.getUserAccountId(), currentUser.getName(), currentStock.getStockId(), currentStock.getName(), TransactionType.SELL, quantity, currentStock.getCurrentPrice() * quantity, existingPortfolioInBuying.get().getCostPerStock() * quantity, currentStock.getCurrentPrice() ,profitNLoss);
            userPortfolioRepositroy.save(newPortfolio);


        return "Transaction of selling stock has been successfully completed";
    }
}
