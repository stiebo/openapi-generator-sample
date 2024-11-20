package dev.stiebo.openapi_generator_sample.exception;

public class ClearDataErrorException extends RuntimeException{
    public ClearDataErrorException() {
        super("Error clearing database");
    }
}
