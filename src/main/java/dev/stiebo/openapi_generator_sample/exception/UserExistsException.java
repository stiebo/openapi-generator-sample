package dev.stiebo.openapi_generator_sample.exception;

public class UserExistsException extends RuntimeException {
    public UserExistsException() {
        super("User already exists");
    }
}
