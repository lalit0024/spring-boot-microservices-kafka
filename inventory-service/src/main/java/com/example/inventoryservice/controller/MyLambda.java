package com.example.inventoryservice.controller;

@FunctionalInterface
public interface MyLambda {

   
     String execute(String message, int number);

   

    default void defaultMethod() {
        System.out.println("This is a default method in the MyLambda interface.");
    }

    default void anotherDefaultMethod() {
        System.out.println("This is another default method in the MyLambda interface.");
    }
    
}
