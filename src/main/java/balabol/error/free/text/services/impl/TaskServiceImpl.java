package balabol.error.free.text.services.impl;

import balabol.error.free.text.dtos.TaskCreation;
import balabol.error.free.text.dtos.TaskResponse;
import balabol.error.free.text.entities.Task;
import balabol.error.free.text.enums.TaskStatus;
import balabol.error.free.text.exceptions.NotFoundException;
import balabol.error.free.text.exceptions.TextCorrectionException;
import balabol.error.free.text.mappers.TaskMapper;
import balabol.error.free.text.repositories.TaskRepository;
import balabol.error.free.text.services.TaskService;
import balabol.error.free.text.services.TextCorrectionService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class.getName());

    private final String TASK_NOT_FOUND_MESSAGE = "task with id = %d not found";

    private final TextCorrectionService textCorrectionService;

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    @Override
    public int createTask(TaskCreation taskCreation) {

        LOGGER.info("method createTask has started working with parameters: {}",
                taskCreation.toString());

        Task createdTask = new Task(
                taskCreation.text(),
                taskCreation.language(),
                TaskStatus.NEW
        );

        createdTask = taskRepository.save(createdTask);

        LOGGER.info("method createTask has finished work, task with id = {} created",
                createdTask.getId());

        return createdTask.getId();
    }

    @Override
    public Task findTaskById(int id) {

        LOGGER.info("method findTaskById has started working with parameters: id = {}", id);

        Task task = taskRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format(TASK_NOT_FOUND_MESSAGE, id)
                ));

        LOGGER.info("method findTaskById has finished work");

        return task;
    }

    @Override
    @Async
    @Scheduled(initialDelayString = "${spring.scheduling.fixed.delay}",
            fixedRateString = "${spring.scheduling.fixed.rate}")
    public void correctTextInTask() {

        LOGGER.info("method correctTextInTask has started working");

        Task task = taskRepository.findFirstByStatus(TaskStatus.NEW);

        // Todo: Создать проверку если null,
        //  выходим из метода и логгируем WARN,
        //  что нет задач

        if (task == null) {
            return;
        }

        String correctedText = null;

        try {
            correctedText = textCorrectionService.correctErrorsInText(task.getText());
        } catch (TextCorrectionException e) {
            taskRepository.updateStatus(task, TaskStatus.COMPLETED_WITH_ERROR);
            return;
        }

        taskRepository.updateTextAndStatus(task, correctedText, TaskStatus.COMPLECTED);

        LOGGER.info("method correctTextInTask has finished work");
    }
}
