package com.textoit.raspberry.exceptions;

public class MovieListNotFoundException extends RuntimeException{

    public MovieListNotFoundException(String message){
        super(message);
    }
}
