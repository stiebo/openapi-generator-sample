package dev.stiebo.openapi_generator_sample.exception;

public class UnableToLockAdminException extends RuntimeException {
    public UnableToLockAdminException() {
        super("Cannot lock admin");
    }
}
