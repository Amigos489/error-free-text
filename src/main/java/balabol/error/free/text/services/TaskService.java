package balabol.error.free.text.unit.services;

import balabol.error.free.text.unit.dtos.TaskCreation;
import balabol.error.free.text.unit.entities.Task;

public interface TaskService {
    int createTask(TaskCreation taskCreation);
    Task findTaskById(int id);
    void processTask();
}
