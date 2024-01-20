package com.example.StocksPortfolioApplication.repositroy;


import com.example.StocksPortfolioApplication.model.User;
import com.example.StocksPortfolioApplication.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase (connection = EmbeddedDatabaseConnection.H2)
public class UserRepositroyTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private List<User> listOfAllUser=new ArrayList<>();

    @BeforeEach
    public void setUp(){
        User user1=new User("shivam","9717255304","shivam@gmail.com",9087.09);
        User user2=new User("anandu","9717255789","shiva@gmail.com",900.09);
        User user3=new User("a","9717250789","shiv@gmail.com",900.09);
        listOfAllUser= Arrays.asList(user1,user2,user3);
        testEntityManager.persist(user1);
        testEntityManager.persist(user2);
        testEntityManager.persist(user3);
    }

    @Test
    public void test_findUserByUserAccountId( ) throws SQLException{
        Assertions.assertEquals(userRepository.findUserByUserAccountId(listOfAllUser.get(0).getUserAccountId()).get(),listOfAllUser.get(0));
    }

    @Test
    public void test_findUserByUserAccountId_returnsNull() throws SQLException{
        Assertions.assertEquals(userRepository.findUserByUserAccountId(678), Optional.empty());
    }

    @Test
    public void test_findAll() throws SQLException{
        Assertions.assertEquals(listOfAllUser,userRepository.findAll());
    }

    @Test
    public void test_deleteUserByUserAccountId() throws SQLException{
        userRepository.deleteUserByUserAccountId(listOfAllUser.get(0).getUserAccountId());
        Assertions.assertEquals(userRepository.findUserByUserAccountId(listOfAllUser.get(0).getUserAccountId()),Optional.empty());
    }

    @Test
    public void test_findUserByName() throws SQLException{
        Assertions.assertEquals(userRepository.findUserByName("a").get(),listOfAllUser.get(2));
    }



}
