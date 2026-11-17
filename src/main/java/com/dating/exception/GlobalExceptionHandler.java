package com.dating.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseEntity<ApiErrorResponse>> handleResponseStatusException(
            ResponseStatusException exception) {
        HttpStatus status = HttpStatus.valueOf(exception.getStatusCode().value());
        String message = exception.getReason() == null ? status.getReasonPhrase() : exception.getReason();
        return Mono.just(buildResponse(status, message));
    }

    @ExceptionHandler({IllegalArgumentException.class, WebExchangeBindException.class})
    public Mono<ResponseEntity<ApiErrorResponse>> handleBadRequest(Exception exception) {
        String message = exception.getMessage() != null && !exception.getMessage().isBlank()
                ? exception.getMessage()
                : "The request is invalid";
        return Mono.just(buildResponse(HttpStatus.BAD_REQUEST, message));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiErrorResponse>> handleUnexpectedException(Exception exception) {
        LOG.error("Unhandled API error", exception);
        return Mono.just(buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"));
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new ApiErrorResponse(status.name(), message));
    }
}
