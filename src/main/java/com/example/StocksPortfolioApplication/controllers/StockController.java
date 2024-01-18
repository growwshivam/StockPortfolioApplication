package com.example.StocksPortfolioApplication.controllers;

import com.example.StocksPortfolioApplication.utils.GenerateStockToBeUpdate;
import com.example.StocksPortfolioApplication.model.Stock;
import com.example.StocksPortfolioApplication.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/stocks")
public class StockController {

    private StockService stockService;
    private GenerateStockToBeUpdate generateStockToBeUpdate;

    public StockController(StockService stockService, GenerateStockToBeUpdate generateStockToBeUpdate) {
        this.stockService = stockService;
        this.generateStockToBeUpdate = generateStockToBeUpdate;
    }

    @GetMapping("/getAllStocks")
    ResponseEntity<List<Stock>> getAllStocks(){
        try{
            return new ResponseEntity<>(stockService.getAllStocks(), HttpStatusCode.valueOf(200));
        } catch (RuntimeException E) {
           return new ResponseEntity<>(HttpStatusCode.valueOf(404));
        }
    }

    @GetMapping("/getStockDetails/{stockId}")
    ResponseEntity<? extends Object> getStockById(@PathVariable Integer stockId) throws RuntimeException {

        try{
            return new ResponseEntity<>(stockService.getStockById(stockId).get(), HttpStatus.ACCEPTED);
        } catch (RuntimeException ex) {

            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping("/createStock")
    ResponseEntity<String> createStock(@RequestBody   Stock stockToBeAdded){
        try{
            return new ResponseEntity<>(stockService.addStock(stockToBeAdded),HttpStatusCode.valueOf(201));
        } catch (RuntimeException E) {
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @PutMapping ("/updateStock/{stockId}")
    ResponseEntity<String> updateStockById(@PathVariable Integer stockId,@RequestBody Stock stockToBeUpdated){
        try{
            return new ResponseEntity<>(stockService.updateStockByStockId(stockId,stockToBeUpdated),HttpStatus.ACCEPTED);
        } catch (RuntimeException E) {
            return new ResponseEntity<>(E.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/uploadCSV")
    public ResponseEntity<String> handleFileUploadAsync(@RequestParam("file") MultipartFile file) throws IOException, RuntimeException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload correct format file.");
        }

        try {
            return ResponseEntity.status(200).body(stockService.updateAllStocks(file));
        } catch (IOException io) {
            return ResponseEntity.status(408).body(io.getMessage());
        } catch (RuntimeException E) {
            return ResponseEntity.badRequest().body(E.getMessage());
        }

    }


}
