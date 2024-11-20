package dev.stiebo.openapi_generator_sample.exception;

public class StolenCardNotFoundException extends RuntimeException{
    public StolenCardNotFoundException() {
        super("Stolen card not found");
    }
}
