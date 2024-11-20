package dev.stiebo.openapi_generator_sample.exception;

public class SuspiciousIpNotFoundException extends RuntimeException{
    public SuspiciousIpNotFoundException() {
        super("Suspicious IP not found");
    }
}
