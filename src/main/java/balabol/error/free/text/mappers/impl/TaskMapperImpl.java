package balabol.error.free.text.unit.mappers.impl;

import balabol.error.free.text.unit.dtos.TaskResponse;
import balabol.error.free.text.unit.entities.Task;
import balabol.error.free.text.unit.mappers.TaskMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskMapperImpl implements TaskMapper {
    @Override
    public TaskResponse mappingToTaskResponse(Task task) {

        TaskResponse taskResponse = null;

        switch (task.getStatus()) {
            case NEW -> {
                taskResponse = new TaskResponse(
                        null,
                        task.getStatus(),
                        null
                );
            }
            case COMPLECTED -> {
                taskResponse = new TaskResponse(
                        task.getText(),
                        task.getStatus(),
                        null
                );
            }
            case COMPLETED_WITH_ERROR -> {
                taskResponse = new TaskResponse(
                        null,
                        task.getStatus(),
                        task.getErrorMessage()
                );
            }
        }

        return taskResponse;
    }
}
