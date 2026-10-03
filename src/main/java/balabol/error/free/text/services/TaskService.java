package balabol.error.free.text.services;

import balabol.error.free.text.dtos.TaskCreation;
import balabol.error.free.text.dtos.TaskResponse;
import balabol.error.free.text.entities.Task;

public interface TaskService {
    int createTask(TaskCreation taskCreation);
    Task findTaskById(int id);
    void correctTextInTask();
}
