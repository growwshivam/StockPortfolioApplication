package com.example.StocksPortfolioApplication.service;


import com.example.StocksPortfolioApplication.model.Stock;
import com.example.StocksPortfolioApplication.repository.StockRepositroy;
import com.example.StocksPortfolioApplication.utils.GenerateStockToBeUpdate;
import org.assertj.core.internal.Bytes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {

    @InjectMocks
    private StockServiceImpl stockServiceImpl;

    @Mock
    private StockRepositroy stockRepositroy;
    @Spy
    private GenerateStockToBeUpdate generateStockToBeUpdate;
    private List<Stock> listOfStocks = new ArrayList<>();

    @BeforeEach
    void setUp() {

        for (int i = 0; i < 5; i++) {
            listOfStocks.add(new Stock(i, i * 2.0, i * 3.0, i * 1.5, i * 2.3, 15, "ABC", i * (1.5)));
        }

    }

    @Test
    public void testCreateStockApi() throws RuntimeException {
        Stock currentStockToBeCreate = new Stock(4, 8.0, 12.0, 6.0, 9.2, 15, "ABC", 6.0);

        when(stockRepositroy.save(currentStockToBeCreate)).thenReturn(currentStockToBeCreate);
        String result = stockServiceImpl.addStock(currentStockToBeCreate);
        verify(stockRepositroy, times(1)).save(currentStockToBeCreate);

        Assertions.assertEquals(result, "Stock has been Added Successfully");
    }

    @Test
    public void testGetStockApi_WhenInputIsValid() throws RuntimeException {
        Stock currentStockToBeGet = new Stock(4, 8.0, 12.0, 6.0, 9.2, 15, "ABC", 6.0);
        when(stockRepositroy.findStockByStockId(4)).thenReturn(Optional.of(currentStockToBeGet));

        Optional<Stock> getTestStock = stockServiceImpl.getStockById(4);
        verify(stockRepositroy, times(1)).findStockByStockId(4);


    }

    @Test
    public void testGetStockApi_WhenInputIsNotValid() throws RuntimeException {
        when(stockRepositroy.findStockByStockId(10)).thenThrow(new RuntimeException("No Such stock exists in the table"));
        try {
            Optional<Stock> newStock = stockServiceImpl.getStockById(10);
        } catch (RuntimeException E) {
            verify(stockRepositroy, times(1)).findStockByStockId(10);
            Assertions.assertEquals("No Such stock exists in the table", E.getMessage());
        }
    }

    @Test
    public void testGetAllStockApi() throws RuntimeException {
        when(stockRepositroy.findAll()).thenReturn(listOfStocks);
        List<Stock> getAllTestStock = stockServiceImpl.getAllStocks();
        verify(stockRepositroy, times(1)).findAll();
        Assertions.assertEquals(getAllTestStock.size(), 5);
    }

    @Test
    public void testUpdateStockApi() throws RuntimeException {
        Stock intialStock = new Stock(4, 8.0, 12.0, 6.0, 9.2, 15, "ABC", 6.0);

        when(stockRepositroy.findStockByStockId(4)).thenReturn(Optional.of(intialStock));
        intialStock.setStockId(4);
        intialStock.setQuantity(15);
        intialStock.setCurrentPrice(13.90);
        intialStock.setLowPrice(11.56);
        intialStock.setHighPrice(45.00);
        intialStock.setLowPrice(12.0);
        intialStock.setClosePrice(32.09);

        when(stockRepositroy.save(intialStock)).thenReturn(intialStock);

        String resultOfUpdation = stockServiceImpl.updateStockByStockId(4, intialStock);

        verify(stockRepositroy, times(1)).findStockByStockId(4);

        verify(stockRepositroy, times(1)).save(intialStock);

        Assertions.assertEquals("Stock has been Updated Successfully", resultOfUpdation);

    }


    @Test
    public void testUpdateOfAllStocksApi() throws IOException, RuntimeException {
        String fileName = "EQ150124 copy.CSV";
        Path filePath = Paths.get(fileName);
        byte[] newFileContent = new byte[0];
        try {
            newFileContent = Files.readAllBytes(filePath);
        } catch (IOException E) {
            throw new IOException("No Such file exits in the folder");
        }
        MultipartFile mockFile = new MockMultipartFile(
                "file",        // Name of the file field in the form
                fileName,       // Original file name
                "text/plain",   // Content type
                newFileContent
        );


        Stock stock1 = new Stock(50001, 10.5, 12.3, 9.8, 11.2, 150, "Stock_ABC", 11.8);

        Stock stock2 = new Stock(50002, 9.7, 11.8, 8.5, 10.2, 180, "Stock_XYZ", 10.5);

        Stock stock3 = new Stock(50003, 11.2, 13.5, 10.5, 12.8, 200, "Stock_PQR", 12.0);

        List<Stock> listOfAllStocks = Arrays.asList(stock1, stock2, stock3);

        when(generateStockToBeUpdate.generateStockToBeUpdateAsync(mockFile))
                .thenReturn(CompletableFuture.completedFuture(listOfAllStocks));

        String resultString = stockServiceImpl.updateAllStocks(mockFile);

        verify(generateStockToBeUpdate, times(1)).generateStockToBeUpdateAsync(mockFile);

        Assertions.assertEquals("updation of all stocks is done successfully", resultString);

    }


}
