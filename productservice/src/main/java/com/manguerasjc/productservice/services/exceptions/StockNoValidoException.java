package com.manguerasjc.productservice.services.exceptions;

public class StockNoValidoException extends RuntimeException{
    public StockNoValidoException(String message){
        super(message);
    }
}
