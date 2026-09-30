package balabol.error.free.text.services.impl;

import balabol.error.free.text.repositories.TaskRepository;
import balabol.error.free.text.services.TaskService;
import balabol.error.free.text.services.TextCorrectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(TaskServiceImpl.class.getName());

    private final TextCorrectionService textCorrectionService;

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TextCorrectionService textCorrectionService,
                           TaskRepository taskRepository) {
        this.textCorrectionService = textCorrectionService;
        this.taskRepository = taskRepository;
    }

    @Override
    public int createTask() {
        return 0;
    }

    @Override
    public void findTaskById(int id) {

    }

    @Override
    public void correctTextInTask() {

    }
}
