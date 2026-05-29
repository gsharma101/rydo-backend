package com.gaurav.rydo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<Map<String, String>> handleApiException(
          ApiException ex
  ) {

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                    Map.of(
                            "message",
                            ex.getMessage()
                    )
            );
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Map<String, String>> handleAccessDeniedException(
          AccessDeniedException ex
  ) {

    return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(
                    Map.of(
                            "message",
                            "Access Denied"
                    )
            );
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGenericException(
          Exception ex
  ) {

    return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                    Map.of(
                            "message",
                            ex.getMessage()
                    )
            );
  }
}