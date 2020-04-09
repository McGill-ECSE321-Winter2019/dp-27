package ca.mcgill.cooperator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class BaseController {

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<String> handleAllServiceExceptions(IllegalArgumentException ex) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Exception> handleAllRuntimeExceptions(RuntimeException ex) {
        ex.printStackTrace();
        return new ResponseEntity<Exception>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
