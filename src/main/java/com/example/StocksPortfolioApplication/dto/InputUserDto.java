package com.example.StocksPortfolioApplication.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;


public class InputUserDto {
    @NotBlank
    private String phoneNumber;

    private String name;

    @NotEmpty
    private String email;
    private Double balance = 0.0;

    public InputUserDto(String phoneNumber, String name, String email, Double balance) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.email = email;
        this.balance = balance;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
