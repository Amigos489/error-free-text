package balabol.error.free.text.unit.mappers;

import balabol.error.free.text.unit.dtos.TaskResponse;
import balabol.error.free.text.unit.entities.Task;

public interface TaskMapper {
    TaskResponse mappingToTaskResponse(Task task);
}
