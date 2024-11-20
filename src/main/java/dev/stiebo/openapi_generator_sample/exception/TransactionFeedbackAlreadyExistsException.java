package dev.stiebo.openapi_generator_sample.exception;

public class TransactionFeedbackAlreadyExistsException extends RuntimeException{
    public TransactionFeedbackAlreadyExistsException() {
        super("Feedback for transaction already exists");
    }
}
