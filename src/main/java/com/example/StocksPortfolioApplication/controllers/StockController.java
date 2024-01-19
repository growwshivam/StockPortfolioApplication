package com.example.StocksPortfolioApplication.controllers;

import com.example.StocksPortfolioApplication.dto.InputStockDto;
import com.example.StocksPortfolioApplication.utils.GenerateStockToBeUpdate;
import com.example.StocksPortfolioApplication.model.Stock;
import com.example.StocksPortfolioApplication.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping ("/stockPortfolioApp/stocks")
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
            return new ResponseEntity<>(stockService.getAllStocksList(), HttpStatusCode.valueOf(200));
        } catch (RuntimeException E) {
           return new ResponseEntity<>(HttpStatusCode.valueOf(404));
        }
    }

    @GetMapping("/getStockDetails/{stockId}")
    ResponseEntity<? extends Object> getStockById(@PathVariable Integer stockId) throws RuntimeException {

        try{
            return new ResponseEntity<>(stockService.findStockById(stockId).get(), HttpStatus.ACCEPTED);
        } catch (RuntimeException ex) {

            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping("/createStock")
    ResponseEntity<String> createStock(@Valid @RequestBody InputStockDto stockToBeAdded) throws RuntimeException {
        try{

            return new ResponseEntity<>(stockService.addNewStock(new Stock(stockToBeAdded.getId(), stockToBeAdded.getOpenPrice(), stockToBeAdded.getClosePrice(), stockToBeAdded.getHighPrice(), stockToBeAdded.getLowPrice(), stockToBeAdded.getQuantity(), stockToBeAdded.getName(), stockToBeAdded.getCurrentPrice())), HttpStatusCode.valueOf(201));
        } catch (RuntimeException E) {

            return new ResponseEntity<>(E.getMessage(), HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @PutMapping ("/updateStock/{stockId}")
    ResponseEntity<String> updateStockById(@PathVariable Integer stockId, @Valid @RequestBody InputStockDto stockToBeUpdated) {

        try{

            return new ResponseEntity<>(stockService.updateStock(stockId, new Stock(stockToBeUpdated.getId(), stockToBeUpdated.getOpenPrice(), stockToBeUpdated.getClosePrice(), stockToBeUpdated.getHighPrice(), stockToBeUpdated.getLowPrice(), stockToBeUpdated.getQuantity(), stockToBeUpdated.getName(), stockToBeUpdated.getCurrentPrice())), HttpStatus.ACCEPTED);
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
            return ResponseEntity.status(200).body(stockService.updateStocksFromFile(file));
        } catch (IOException io) {
            return ResponseEntity.status(408).body(io.getMessage());
        } catch (RuntimeException E) {
            return ResponseEntity.badRequest().body(E.getMessage());
        }

    }


}
