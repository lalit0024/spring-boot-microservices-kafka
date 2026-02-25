package com.example.inventoryservice.controller;

public class TestLamba  implements MyLambda {


    public static void main(String[] args) {

      TestLamba.executeLambda((message , number) -> {return "Hello from lambda!"+message.toUpperCase()+"<>"+number;}); 
      TestLamba.executeLambdaWithMessage((message, number) -> {return "Hello from lambda with message!"+message+"<>"+number;}, "This is a message");  
   
      TestLamba testLamba = new TestLamba();
      testLamba.execute("Test message", 42);
      testLamba.defaultMethod();
      testLamba.anotherDefaultMethod();
   
    }


    public static void executeLambda(MyLambda lambda) {
     String result = lambda.execute("lalit Kumar Singh",5);
        System.out.println(result);
        lambda.defaultMethod();
        lambda.anotherDefaultMethod();
    }

    public static void executeLambdaWithMessage(MyLambda lambda, String message) {
        System.out.println("Executing lambda with message: " + message);
        String result = lambda.execute("!!!!",10);
        System.out.println(result);
        lambda.defaultMethod();
        lambda.anotherDefaultMethod();
    }


    @Override
    public String execute(String message, int number) {

System.out.println("This is an overridden execute method in the TestLamba class.");     
      //  throw new UnsupportedOperationException("Unimplemented method 'execute'");
      return "This is an overridden execute method in the TestLamba class.";        
    }

    @Override
    public void defaultMethod() {
        System.out.println("This is an overridden default method in the TestLamba class.");     
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'defaultMethod'");
    }
    @Override
    public void anotherDefaultMethod() {
        System.out.println("This is an overridden default method 2in the TestLamba class.");
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'anotherDefaultMethod'");
    }
}
