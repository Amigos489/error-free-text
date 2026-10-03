package balabol.error.free.text.dtos;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        int code,
        LocalDateTime timestamp,
        String path
) {}
