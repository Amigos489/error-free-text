package balabol.error.free.text.mappers;

import balabol.error.free.text.dtos.TaskResponse;
import balabol.error.free.text.entities.Task;

public interface TaskMapper {
    TaskResponse mappingToTaskResponse(Task task);
}
