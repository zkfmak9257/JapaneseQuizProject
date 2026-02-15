package com.team.jpquiz.global.error;

import com.team.jpquiz.common.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(
            CustomException ex,
            HttpServletRequest request

    ) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse body = ErrorResponse.of(
                errorCode.getCode(),
                errorCode.getMessage(),
                errorCode.getStatus().value(),
                request.getRequestURI()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(body);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ErrorResponse> handleValidationException(
            Exception ex,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
        ErrorResponse body = ErrorResponse.of(
                errorCode.getCode(),
                errorCode.getMessage(),
                errorCode.getStatus().value(),
                request.getRequestURI()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        ErrorResponse body = ErrorResponse.of(
                errorCode.getCode(),
                errorCode.getMessage(),
                errorCode.getStatus().value(),
                request.getRequestURI()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(body);
    }
}
