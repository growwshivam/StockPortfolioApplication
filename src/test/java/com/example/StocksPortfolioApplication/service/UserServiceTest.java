package com.example.StocksPortfolioApplication.service;


import com.example.StocksPortfolioApplication.dto.InputUserDto;
import com.example.StocksPortfolioApplication.dto.StockDetailsDto;
import com.example.StocksPortfolioApplication.dto.UserPortfolioDetailsDto;
import com.example.StocksPortfolioApplication.model.TransactionType;
import com.example.StocksPortfolioApplication.model.User;
import com.example.StocksPortfolioApplication.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Spy
    private UserRepository userRepository;
    @Spy
    private UserTransactionDetailsService userTransactionDetailsService;

    @InjectMocks
    private UserServiceImpl userServiceImpl;


    @BeforeEach
    public void setUp() {
        List<User> listOfUsers = new ArrayList<>();
        for (Integer i = 0; i < 5; i++) {
            listOfUsers.add(new User("shivam", "9717255304", "@gmail.com", i * 100.0));
        }

    }

    @Test
    public void test_addUser() throws RuntimeException {

        InputUserDto createToBeCreate = new InputUserDto("9717255304", "SHIVAM", "@GMAIL.COM", 345.67);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        String result = userServiceImpl.addUser(createToBeCreate);

        verify(userRepository).save(userArgumentCaptor.capture());

        System.out.println(userArgumentCaptor.getAllValues());

        Assertions.assertEquals(userArgumentCaptor.getValue().getName(), "SHIVAM");

        Assertions.assertEquals(userArgumentCaptor.getValue().getPhoneNumber(), "9717255304");

        Assertions.assertEquals(result, "User has been added successfully to the table");
    }


    @Test
    public void test_getUserById_WhenInputIsValid() throws RuntimeException {

        Integer idToBeCheck = 2;
        User currentUser = new User("Shivam", "9717255304", "@gmail.com", 567.89);
        when(userRepository.findUserByUserAccountId(2)).thenReturn(Optional.of(currentUser));

        Optional<User> resultOfQuery = userServiceImpl.getUserById(2);
        verify(userRepository, times(1)).findUserByUserAccountId(2);

        Assertions.assertEquals(resultOfQuery.get().getName(), "Shivam");
        Assertions.assertEquals(resultOfQuery.get().getBalance(), 567.89);

    }


    @Test
    public void test_getUserById_WhenInputIsNotValid() throws RuntimeException {
        when(userRepository.findUserByUserAccountId(6))
                .thenThrow(new RuntimeException("No Such user exists in the table"));

        Assertions.assertThrows(RuntimeException.class,
                () -> userServiceImpl.getUserById(6),
                "No Such user exists in the table");
    }

    @Test
    public void test_deleteUserById_WhenInputIsValid() throws RuntimeException {

        User currentUser = new User("Shivam", "9717255304", "@gmail.com", 567.89);

        when(userRepository.findUserByUserAccountId(4)).thenReturn(Optional.of(currentUser));
        doNothing().when(userRepository).deleteUserByUserAccountId(4);

        String resultOfDelete = userServiceImpl.deleteUserById(4);
        verify(userRepository, times(1)).deleteUserByUserAccountId(4);

        Assertions.assertEquals(resultOfDelete, "Deletion of User has been successfully completed");
    }

    @Test
    public void test_deleteUserById_WhenInputIsNotValid() throws RuntimeException {
        when(userRepository.findUserByUserAccountId(10))
                .thenThrow(new RuntimeException("No Such User AccountId did not exist in the user table So deletion is not possible"));

        Assertions.assertThrows(RuntimeException.class,
                () -> userServiceImpl.deleteUserById(10),
                "No Such User AccountId did not exist in the user table So deletion is not possible");
    }




    @Test
    public void test_updateUser_WhenUserAlreadyExists() throws RuntimeException {

        User currentUser = new User("Shivam", "9717255304", "@gmail.com", 567.89);

        when(userRepository.findUserByUserAccountId(4)).thenReturn(Optional.of(currentUser));

        currentUser.setPhoneNumber("8826621521");
        currentUser.setEmail("shivam@gmail.com");
        currentUser.setBalance(1345.67);


        InputUserDto inputUpdate = new InputUserDto("8826621521", "Shivam", "shivam@gmail.com", 1345.67);
        String result = userServiceImpl.updateUser(4, inputUpdate);

        ArgumentCaptor<User> argumentsUser = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(argumentsUser.capture());
        Assertions.assertEquals(argumentsUser.getValue().getEmail(), "shivam@gmail.com");
        Assertions.assertEquals(argumentsUser.getValue().getBalance(), 1345.67);

        Assertions.assertEquals(result, "Updation Operation has been successfully");

    }


    @Test
    public void test_updateUser_WhenUserNotExists() throws RuntimeException {
        when(userRepository.findUserByUserAccountId(4)).thenReturn(Optional.ofNullable(null));
        InputUserDto inputUpdate = new InputUserDto("8826621521", "Shivam", "shivam@gmail.com", 9087.09);
//        User currentUser = new User("Shivam", "8826621521", "SHivam@gmail.com", 9087.09);

        ArgumentCaptor<User> argumentsUser = ArgumentCaptor.forClass(User.class);
        String result = userServiceImpl.updateUser(4, inputUpdate);

        verify(userRepository, times(1)).save(argumentsUser.capture());
        Assertions.assertEquals(argumentsUser.getValue().getEmail(), "shivam@gmail.com");
        Assertions.assertEquals(argumentsUser.getValue().getBalance(), 9087.09);
        Assertions.assertEquals(result, "User has been added successfully to the table");

    }

    @Test
    public void test_tradingApi_Buying_WhenInputIsValid() throws RuntimeException {
        User currentUser = new User("Shivam", "9717255304", "@gmail.com", 567.89);
        when(userRepository.findUserByUserAccountId(4)).thenReturn(Optional.of(currentUser));

        when(userTransactionDetailsService.tradeApiService(currentUser, 500002, 16, TransactionType.BUY)).thenReturn("Transaction of buying of stocks has been created successfully");
        currentUser.setBalance(currentUser.getBalance() - 16 * 2.34);

        when(userRepository.save(currentUser)).thenReturn(currentUser);

        String resultString = userServiceImpl.tradingApi(4, 500002, 16, TransactionType.BUY);

        verify(userRepository, times(1)).save(currentUser);
        verify(userRepository, times(1)).findUserByUserAccountId(4);
        verify(userTransactionDetailsService, times(1)).tradeApiService(currentUser, 500002, 16, TransactionType.BUY);

        Assertions.assertEquals(resultString, "Transaction of buying of stocks has been created successfully");

    }

    @Test
    public void test_tradingApi_Selling_WhenInputIsValid() throws RuntimeException {

        User currentUser = new User("Shivam", "9717255304", "@gmail.com", 567.89);

        when(userRepository.findUserByUserAccountId(4)).thenReturn(Optional.of(currentUser));

        when(userTransactionDetailsService.tradeApiService(currentUser, 500002, 8, TransactionType.SELL)).thenReturn("Transaction of selling of stocks has been created successfully");

        currentUser.setBalance(currentUser.getBalance() + 8 * 3.45);

        when(userRepository.save(currentUser)).thenReturn(currentUser);

        String resultString = userServiceImpl.tradingApi(4, 500002, 8, TransactionType.SELL);

        verify(userRepository, times(1)).save(currentUser);
        verify(userRepository, times(1)).findUserByUserAccountId(4);
        verify(userTransactionDetailsService, times(1)).tradeApiService(currentUser, 500002, 8, TransactionType.SELL);

        Assertions.assertEquals(resultString, "Transaction of selling of stocks has been created successfully");
    }

    @Test
    public void test_tradingApi_Selling_WhenUserIdIsNotValid() throws RuntimeException {
        when(userRepository.findUserByUserAccountId(8))
                .thenThrow(new RuntimeException("No such user exists in the table"));

        Assertions.assertThrows(RuntimeException.class,
                () -> userServiceImpl.tradingApi(8, 500002, 15, TransactionType.SELL),
                "No such user exists in the table");
    }

    @Test
    public void test_tradingApi_Buying_WhenUserIdIsNotValid() throws RuntimeException {
        when(userRepository.findUserByUserAccountId(8))
                .thenThrow(new RuntimeException("No such user exists in the table"));

        Assertions.assertThrows(RuntimeException.class,
                () -> userServiceImpl.tradingApi(8, 500002, 15, TransactionType.BUY),
                "No such user exists in the table");
    }


    @Test
    public void test_tradingApi_Buying_WhenQuantityIsNotValid() throws RuntimeException {
        User currentUser = new User("Shivam", "9717255304", "@gmail.com", 567.89);

        when(userRepository.findUserByUserAccountId(4)).thenReturn(Optional.of(currentUser));

        when(userTransactionDetailsService.tradeApiService(currentUser, 500002, 15, TransactionType.BUY))
                .thenThrow(new RuntimeException("Insufficient Balance to done transaction"));

        Assertions.assertThrows(RuntimeException.class, () -> userServiceImpl.tradingApi(4, 500002, 15, TransactionType.BUY),
                "Insufficient Balance to done transaction");
    }

    @Test
    public void test_tradingApi_Selling_WhenQuantityIsNotValid() throws RuntimeException {

        User currentUser = new User("Shivam", "9717255304", "@gmail.com", 567.89);

        when(userRepository.findUserByUserAccountId(4)).thenReturn(Optional.of(currentUser));

        when(userTransactionDetailsService.tradeApiService(currentUser, 500002, 1500, TransactionType.SELL)).thenThrow(new MockitoException("Selling transaction not possible as no of stocks to sell is more than current stocks present in user portfolio"));

        Assertions.assertEquals(Assertions.assertThrows(RuntimeException.class, () -> {
            String result = userServiceImpl.tradingApi(4, 500002, 1500, TransactionType.SELL);
        }).getMessage(), "Selling transaction not possible as no of stocks to sell is more than current stocks present in user portfolio");

    }

    @Test
    public void test_getPortfolioOfUser_WhenInputIsValid() throws RuntimeException {
        User currentUser = new User("Shivam", "9717255304", "@gmail.com", 567.89);

        when(userRepository.findUserByUserAccountId(4)).thenReturn(Optional.of(currentUser));

        List<StockDetailsDto> listOfStockDetails = new ArrayList<>();

        listOfStockDetails.add(new StockDetailsDto("HDFC", 500002, 18, 26.00, 34.00, 8.00));

        listOfStockDetails.add(new StockDetailsDto("ABC", 500004, 16, 23.00, 34.00, 11.00));

        UserPortfolioDetailsDto currentUserPortfolioDetailsDto = new UserPortfolioDetailsDto(listOfStockDetails, 4456.0, 4900.0, 456.0, 5.92);

        when(userTransactionDetailsService.getPortfolioOfUser(4)).thenReturn(currentUserPortfolioDetailsDto);

        UserPortfolioDetailsDto testUserPortfolioDetailsDto = userServiceImpl.getPortfolioOfUser(4);

        Assertions.assertEquals(testUserPortfolioDetailsDto.getTotalBuyPrice(), 4900.00);

        Assertions.assertEquals(testUserPortfolioDetailsDto.getProfitAndLoss(), 456.0);

    }

    @Test
    public void test_getPortfolioOfUser_WhenInputIsNotValid() throws RuntimeException {
        when(userRepository.findUserByUserAccountId(14)).thenThrow(new MockitoException("No such user exists in the table"));

        Assertions.assertEquals(Assertions.assertThrows(RuntimeException.class, () -> {
            UserPortfolioDetailsDto result = userServiceImpl.getPortfolioOfUser(14);
        }).getMessage(), "No such user exists in the table");
    }
}
