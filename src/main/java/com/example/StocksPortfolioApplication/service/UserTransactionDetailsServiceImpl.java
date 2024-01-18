package com.example.StocksPortfolioApplication.service;

import com.example.StocksPortfolioApplication.dto.StockDetailsDto;
import com.example.StocksPortfolioApplication.dto.UserPortfolioDetailsDto;
import com.example.StocksPortfolioApplication.model.*;
import com.example.StocksPortfolioApplication.repository.UserTransactionDetailsRepositroy;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
public class UserTransactionDetailsServiceImpl implements UserTransactionDetailsService {
    private UserTransactionDetailsRepositroy userTransactionDetailsRepositroy;

    private StockService stockService;

    public UserTransactionDetailsServiceImpl(UserTransactionDetailsRepositroy userTransactionDetailsRepositroy, StockService stockService) {
        this.userTransactionDetailsRepositroy = userTransactionDetailsRepositroy;

        this.stockService = stockService;
    }


    public String tradeApiService(User user, Integer stockId, Integer quantity, TransactionType transactionType) throws RuntimeException {

        Optional<Stock> currentStock = stockService.getStockById(stockId);

        if (!currentStock.isPresent()) {

            throw new RuntimeException("Transaction not possible as no such stock exists");
        }

        switch (transactionType) {

            case BUY:
                return handleBuyingTransaction(user, stockId, quantity, transactionType, currentStock.get());
            case SELL:

                return handleSellingTransaction(user, stockId, quantity, transactionType, currentStock.get());
            default:

                throw new RuntimeException("Invalid transaction type");
        }

    }

    private String handleBuyingTransaction(User currentUser, Integer stockId, Integer quantity, TransactionType transactionType, Stock currentStock) throws RuntimeException {

        if (currentUser.getBalance() < (currentStock.getCurrentPrice() * quantity)) {
            throw new RuntimeException("Insufficient Balance to done transaction");
        }

        userTransactionDetailsRepositroy.save(new UserTransactionDetails(currentUser.getUserAccountId(), stockId, LocalTime.now(), transactionType, quantity, currentStock.getCurrentPrice()));

        return "Transaction of buying of stocks has been created successfully";
    }


    private String handleSellingTransaction(User currentUser, Integer stockId, Integer quantity, TransactionType transactionType, Stock currentStock) throws RuntimeException {

        Optional<Integer> totalBuyQuantity = userTransactionDetailsRepositroy.getTotalQuantity(currentUser.getUserAccountId(), stockId, TransactionType.BUY);

        Optional<Integer> totalSellBuyQuantity = userTransactionDetailsRepositroy.getTotalQuantity(currentUser.getUserAccountId(), stockId, TransactionType.SELL);

        if (totalBuyQuantity.get() <= ((totalSellBuyQuantity.orElse(0)) + quantity)) {

            throw new RuntimeException("Selling transaction not possible as no of stocks to sell is more than current stocks present in user portfolio");
        }

        currentUser.setBalance(currentUser.getBalance() + currentStock.getCurrentPrice() * quantity);

        userTransactionDetailsRepositroy.save(new UserTransactionDetails(currentUser.getUserAccountId(), stockId, LocalTime.now(), transactionType, quantity, currentStock.getCurrentPrice()));

        return "Transaction of selling of stocks has been created successfully";
    }


    public UserPortfolioDetailsDto getPortfolioOfUser(Integer userAccountId) throws RuntimeException {
        List<StockDetailsDto> listOfStockDetails = new ArrayList<>();

        List<Integer> uniqueStockid = userTransactionDetailsRepositroy.getAllStocks(userAccountId);

        Double totalProftAndLoss = 0.0;
        Double totalBuyPrice = 0.0;
        Double totalPortfolioHolding = 0.0;
        Double totalBuyPriceSellTransaction = 0.0;


        for (Integer stockId : uniqueStockid) {

            Stock currentStock = stockService.getStockById(stockId).get();


            Optional<Integer> totalSellingQuantity = userTransactionDetailsRepositroy.getTotalQuantity(userAccountId, currentStock.getStockId(), TransactionType.SELL);

            Optional<Double> totalBuySellingPrice = userTransactionDetailsRepositroy.getTotalTransaction(userAccountId, currentStock.getStockId(), TransactionType.SELL);

            Optional<Integer> totalBuyingQuantity = userTransactionDetailsRepositroy.getTotalQuantity(userAccountId, currentStock.getStockId(), TransactionType.BUY);

            Optional<Double> totalBuyingPrice = userTransactionDetailsRepositroy.getTotalTransaction(userAccountId, currentStock.getStockId(), TransactionType.BUY);

            Integer originalSellingQuantity = totalSellingQuantity.orElse(0);

            Double originalTotalBuySellingPrice = totalBuySellingPrice.orElse(0.0);

            Integer originalBuyingQuantity = totalBuyingQuantity.orElse(0);

            Double originalTotalBuyingPrice = totalBuyingPrice.orElse(0.0);

            Double avgBuyingPrice;
            if (originalBuyingQuantity == 0) avgBuyingPrice = 0.0;
            else avgBuyingPrice = originalTotalBuyingPrice / originalBuyingQuantity;

            Double avgSellingBuyingPrice;
            if (originalSellingQuantity == 0) avgSellingBuyingPrice = 0.0;
            else avgSellingBuyingPrice = originalTotalBuySellingPrice / originalSellingQuantity;

            totalProftAndLoss += (originalSellingQuantity) * (avgSellingBuyingPrice - avgBuyingPrice);

            Integer remainingQuantity = (originalBuyingQuantity - originalSellingQuantity);

            Double currentPriceOfStock = currentStock.getCurrentPrice();

            listOfStockDetails.add(new StockDetailsDto(currentStock.getName(), currentStock.getStockId(), remainingQuantity, avgBuyingPrice, currentStock.getCurrentPrice(), currentStock.getCurrentPrice() - avgBuyingPrice));

            totalPortfolioHolding += remainingQuantity * currentStock.getCurrentPrice();

            totalBuyPrice += originalBuyingQuantity * avgBuyingPrice;

            totalBuyPriceSellTransaction += originalSellingQuantity * avgBuyingPrice;
        }

        Double profitAndLossPercentage;

        if (totalBuyPriceSellTransaction == 0.0) profitAndLossPercentage = 0.0;
        else profitAndLossPercentage = (totalProftAndLoss / totalBuyPriceSellTransaction) * 100.0;

        return new UserPortfolioDetailsDto(listOfStockDetails, totalPortfolioHolding, totalBuyPrice, totalProftAndLoss, profitAndLossPercentage);
    }


}