package com.example.StocksPortfolioApplication.controllers;

import com.example.StocksPortfolioApplication.dto.StockUpdatingDto;
import com.example.StocksPortfolioApplication.helperfunction.GenerateStockToBeUpdate;
import com.example.StocksPortfolioApplication.model.Stock;
import com.example.StocksPortfolioApplication.service.StockService;
import com.example.StocksPortfolioApplication.service.StockServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
        }catch(Exception E){
           return new ResponseEntity<>(HttpStatusCode.valueOf(404));
        }
    }

    @GetMapping("/getStockDetails/{stockId}")
    ResponseEntity<Stock> getStockById(@PathVariable Integer stockId){
        try{
            return new ResponseEntity<>(stockService.getStockById(stockId).get(),HttpStatusCode.valueOf(404));}
        catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @PostMapping("/createStock")
    ResponseEntity<String> createStock(@RequestBody   Stock stockToBeAdded){
        try{
            return new ResponseEntity<>(stockService.addStock(stockToBeAdded),HttpStatusCode.valueOf(201));
        }catch(Exception E){
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @PutMapping ("/updateStock/{stockId}")
    ResponseEntity<String> updateStockById(@PathVariable Integer stockId,@RequestBody Stock stockToBeUpdated){
        try{
            return new ResponseEntity<>(stockService.updateStockByStockId(stockId,stockToBeUpdated),HttpStatus.ACCEPTED);
        }catch(Exception E){
            return new ResponseEntity<>(E.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/uploadCSV")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException{
        // Check if the file is not empty
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a CSV file.");
        }

        // Process the CSV file (you can implement your logic here)
        // For example, you can read the content of the CSV file using libraries like OpenCSV.

        // Placeholder response message
//        String responseMessage = "File uploaded successfully: " + file.getOriginalFilename();
//        return ResponseEntity.ok(responseMessage);
        try  {

           List<Stock> listOfAllStocksToBeUpdated= generateStockToBeUpdate.generateListOfStockToBeUpdated(file);
           return new ResponseEntity<>(stockService.updateListOfStocks(listOfAllStocksToBeUpdated),HttpStatus.ACCEPTED);
        } catch (IOException e) {
            return new ResponseEntity<>("Error reading the file", HttpStatus.INTERNAL_SERVER_ERROR);
        }catch(Exception E){
            return new ResponseEntity<>("Error in updation of stocks",HttpStatus.NOT_IMPLEMENTED);
        }
    }



}
