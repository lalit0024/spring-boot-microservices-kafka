package com.example.inventoryservice.controller;

public sealed  class SealedClasstest permits MainClass{

    public void display() {
        System.out.println("This is a method in the SealedClasstest class.");
    }
    
}
