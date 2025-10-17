package com.project.friendly_bill.shared.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.project.friendly_bill.shared.common.api.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FBException.class)
    public ResponseEntity<ApiResponse<?>> handleFBException(FBException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity.internalServerError().body(response);
    }
}
