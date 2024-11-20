package dev.stiebo.openapi_generator_sample.exception;

public class TransactionNotFoundException extends RuntimeException{
    public TransactionNotFoundException() {
        super("Transaction not found");
    }
}
