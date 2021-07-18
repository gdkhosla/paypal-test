package com.paypal.bfs.test.bookingserv.controller.adviser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.paypal.bfs.test.bookingserv.exception.DataNotFoundException;
import com.paypal.bfs.test.bookingserv.exception.ValidationException;

@ControllerAdvice
public class ApplicationExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    /**
     * Processes generic exceptions that do not fall in below categories.
     *
     * @param ex Exception object
     * 
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleGenericExceptions(final Exception ex) {
    	ErrorResponse response = ErrorResponse.builder()
        		.exception(ex.getMessage())
        		.statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
        		.build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Error handler intercepting FileNotFound Exception. These exceptions are always thrown with
     * NOT_FOUND as status code, but the message is extracted from the exception.
     *
     * @param ex Exception
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(DataNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleDataNotFoundExceptions(final Exception ex) {
        ErrorResponse response =
                ErrorResponse.builder()
                .exception(ex.getMessage())
                .statusCode(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    /**
     * Error handler intercepting IllegalArgumentException. These exceptions are always thrown with
     * BAD_REQUEST as status code, but the message is extracted from the exception.
     *
     * @param ex Exception
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleIllegalArgumentExceptions(final Exception ex) {
        ErrorResponse response =
                ErrorResponse.builder()
                .exception(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Error handler intercepting IllegalArgumentException. These exceptions are always thrown with
     * BAD_REQUEST as status code, but the message is extracted from the exception.
     *
     * @param ex Exception
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> processValidationException(final Exception ex) {
        ErrorResponse response =
                ErrorResponse.builder()
                .exception(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
}
