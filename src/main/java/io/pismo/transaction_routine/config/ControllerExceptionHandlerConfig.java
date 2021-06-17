package io.pismo.transaction_routine.config;

import io.pismo.transaction_routine.config.exception.EntityAlreadyExistExeception;
import io.pismo.transaction_routine.config.exception.EntityNotFoundExeception;
import io.pismo.transaction_routine.entrypoint.http.response.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
class ControllerExceptionHandlerConfig extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> clienteException(Exception e, HttpServletRequest req) {
        final ApiResponse apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    @ExceptionHandler({EntityAlreadyExistExeception.class, EntityNotFoundExeception.class})
    public ResponseEntity<ApiResponse> businessException(Exception e, HttpServletRequest req) {
        final ApiResponse apiResponse = new ApiResponse(HttpStatus.PRECONDITION_FAILED, e.getMessage());
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(apiResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        final String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .map(field -> field.getField() + " " + (field.getDefaultMessage()))
                .findFirst().orElse(ex.getMessage());

        ApiResponse err = new ApiResponse(status, errorMsg);
        return new ResponseEntity<>(err, status);
    }

}
