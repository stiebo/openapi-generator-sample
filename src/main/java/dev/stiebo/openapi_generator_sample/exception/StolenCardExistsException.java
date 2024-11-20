package dev.stiebo.openapi_generator_sample.exception;

public class StolenCardExistsException extends RuntimeException{
    public StolenCardExistsException() {
        super("Stole card already exists");
    }
}
