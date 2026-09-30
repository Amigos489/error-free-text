package balabol.error.free.text.services;

import org.springframework.scheduling.config.Task;

public interface TaskService {
    int createTask();
    void findTaskById(int id);
    void correctTextInTask();
}
