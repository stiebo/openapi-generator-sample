package dev.stiebo.openapi_generator_sample.exception;

public class SuspiciousIpExistsException extends RuntimeException{
    public SuspiciousIpExistsException() {
        super("Suspicious IP already exists");
    }
}
