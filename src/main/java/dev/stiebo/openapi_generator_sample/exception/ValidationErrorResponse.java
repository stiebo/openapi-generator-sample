package dev.stiebo.openapi_generator_sample.exception;

import java.time.OffsetDateTime;
import java.util.List;

public record ValidationErrorResponse(
        OffsetDateTime timestamp,
        int status,
        String error,
        List<FieldError> fieldErrors,
        String path
) {
    public record FieldError(
            String fieldName,
            String errorMessage
    ){

    }
}
