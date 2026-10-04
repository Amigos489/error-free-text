package balabol.error.free.text.dtos;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse(
        String messageMessage,
        int errorCode,
        LocalDateTime timestamp,
        String path
) {}
