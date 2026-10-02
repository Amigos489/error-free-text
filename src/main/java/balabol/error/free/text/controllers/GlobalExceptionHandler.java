package balabol.error.free.text.controllers;

import balabol.error.free.text.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFoundException(NotFoundException e) {

        logger.error(e.getMessage());

        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, List<String>> errors = new HashMap<>();

        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            if (errors.get(error.getField()) == null) {

                List<String> defaultMessages = new ArrayList<>();

                defaultMessages.add(error.getDefaultMessage());

                errors.put(error.getField(), defaultMessages);
            } else {

                List<String> defaultMessages = new ArrayList<>();

                defaultMessages.add(error.getDefaultMessage());
            }
        }

        // Todo: добавить logger.error

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
