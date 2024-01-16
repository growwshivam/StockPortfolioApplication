package com.example.StocksPortfolioApplication.helperfunction;


import com.example.StocksPortfolioApplication.dto.StockUpdatingDto;
import com.example.StocksPortfolioApplication.model.Stock;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class GenerateStockToBeUpdate {

    private MultipartFile file;

    public GenerateStockToBeUpdate(){

    }
    public GenerateStockToBeUpdate(MultipartFile file) {
        this.file = file;
    }

  public   List<Stock> generateListOfStockToBeUpdated(MultipartFile file) throws IOException{
        try(InputStream inputStream = file.getInputStream()){
            List<String> lines = org.apache.commons.io.IOUtils.readLines(inputStream, "UTF-8");
            boolean firstLine=true;
            List<String> columnNames= new ArrayList<>();
            List<Stock> listOfStocksToBeUpdate=new ArrayList<>();
            for(String line:lines){
                if(firstLine){
                    firstLine=false;
                    String[] stringArray= line.split(",");
                    columnNames= Arrays.asList(stringArray);
                }
                else{
                    String[] stringArray= line.split(",");
                    List<String> tempList=Arrays.asList(stringArray);
                    Stock stockUpdatingDto=new Stock();
                    stockUpdatingDto.setStockId(Integer.parseInt(tempList.get(columnNames.indexOf("stockId"))));
                    stockUpdatingDto.setName(tempList.get(columnNames.indexOf("stockName")));
                    stockUpdatingDto.setClosePrice(Double.parseDouble(tempList.get(columnNames.indexOf("CLOSE"))));
                    stockUpdatingDto.setHighPrice(Double.parseDouble(tempList.get(columnNames.indexOf("HIGH"))));
                    stockUpdatingDto.setLowPrice(Double.parseDouble(tempList.get(columnNames.indexOf("LOW"))));
                    stockUpdatingDto.setOpenPrice(Double.parseDouble(tempList.get(columnNames.indexOf("OPEN"))));
                    stockUpdatingDto.setQuantity(Integer.parseInt(tempList.get(columnNames.indexOf("TOTTRDQTY"))));
                    stockUpdatingDto.setCurrentPrice(Double.parseDouble(tempList.get(columnNames.indexOf("CLOSE"))));
                    listOfStocksToBeUpdate.add(stockUpdatingDto);
                }
            }
    return listOfStocksToBeUpdate;
        }catch(IOException e){
            throw new IOException("File reading not possible");
        }

    }
}
