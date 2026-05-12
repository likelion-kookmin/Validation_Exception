package com.example.crudtest.exception.custom;

public class BoardNotFoundException extends RuntimeException {
    public BoardNotFoundException(String message){
        super(message);
    }
}
