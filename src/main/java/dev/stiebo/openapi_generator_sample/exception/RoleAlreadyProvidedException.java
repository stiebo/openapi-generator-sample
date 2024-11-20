package dev.stiebo.openapi_generator_sample.exception;

public class RoleAlreadyProvidedException extends RuntimeException{
    public RoleAlreadyProvidedException() {
        super("Role already provided");
    }
}
