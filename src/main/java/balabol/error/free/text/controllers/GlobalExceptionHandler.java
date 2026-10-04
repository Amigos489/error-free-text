package balabol.error.free.text.controllers;

import balabol.error.free.text.dtos.ErrorResponse;
import balabol.error.free.text.exceptions.NotFoundException;
import balabol.error.free.text.exceptions.TextCorrectionException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFoundException(NotFoundException e,
                                               HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                request.getRequestURI());

        LOGGER.error(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException e,
                                                             HttpServletRequest request) {

        // Todo: вынести в отдельный метод получения хэш-мапы

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

        // Todo: добавить сериализацию хэш-таблицы в JSON

        ErrorResponse errorResponse = new ErrorResponse(
                errors.toString(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                request.getRequestURI());

        LOGGER.error(errors.toString());

        return new ResponseEntity<>(errorResponse,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TextCorrectionException.class)
    public void textCorrectionException(TextCorrectionException e,
                                               HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                HttpStatus.BAD_GATEWAY.value(),
                LocalDateTime.now(),
                request.getRequestURI());

        LOGGER.error(e.getMessage());
    }

    // Todo: для HttpMessageNotReadableException.class

    // Todo: для Exception.class
}
