package com.example.StocksPortfolioApplication.utils;


import com.example.StocksPortfolioApplication.model.Stock;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class GenerateStockToBeUpdate {

    private MultipartFile file;

    public GenerateStockToBeUpdate() {

    }

    public GenerateStockToBeUpdate(MultipartFile file) {
        this.file = file;
    }

    @Async
    public CompletableFuture<List<Stock>> generateStockToBeUpdateAsync(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            List<String> lines = org.apache.commons.io.IOUtils.readLines(inputStream, "UTF-8");
            boolean firstLine = true;
            List<String> columnNames = new ArrayList<>();
            List<Stock> listOfStocksToBeUpdate = new ArrayList<>();
            for (String line : lines) {
                if (firstLine) {
                    firstLine = false;
                    String[] stringArray = line.split(",");
                    columnNames = Arrays.asList(stringArray);
                } else {
                    String[] stringArray = line.split(",");
                    List<String> tempList = Arrays.asList(stringArray);
                    Stock stockUpdating = new Stock();
                    stockUpdating.setStockId(Integer.parseInt(tempList.get(columnNames.indexOf("SC_CODE"))));
                    stockUpdating.setName(tempList.get(columnNames.indexOf("SC_NAME")));
                    stockUpdating.setClosePrice(Double.parseDouble(tempList.get(columnNames.indexOf("CLOSE"))));
                    stockUpdating.setHighPrice(Double.parseDouble(tempList.get(columnNames.indexOf("HIGH"))));
                    stockUpdating.setLowPrice(Double.parseDouble(tempList.get(columnNames.indexOf("LOW"))));
                    stockUpdating.setOpenPrice(Double.parseDouble(tempList.get(columnNames.indexOf("OPEN"))));
                    stockUpdating.setQuantity(Integer.parseInt(tempList.get(columnNames.indexOf("NO_OF_SHRS"))));
                    stockUpdating.setCurrentPrice(Double.parseDouble(tempList.get(columnNames.indexOf("HIGH"))) + Double.parseDouble(tempList.get(columnNames.indexOf("LOW"))) / 2);
                    listOfStocksToBeUpdate.add(stockUpdating);
                }
            }
            return CompletableFuture.completedFuture(listOfStocksToBeUpdate);
        } catch (IOException e) {
            CompletableFuture<List<Stock>> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;
        }
    }
}
