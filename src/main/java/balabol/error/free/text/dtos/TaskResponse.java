package balabol.error.free.text.unit.dtos;

import balabol.error.free.text.unit.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

public record TaskResponse(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String text,

        TaskStatus status,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String messageError
) {}
