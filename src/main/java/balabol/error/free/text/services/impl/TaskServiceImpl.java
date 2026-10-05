package balabol.error.free.text.services.impl;

import balabol.error.free.text.dtos.TaskCreation;
import balabol.error.free.text.entities.Task;
import balabol.error.free.text.enums.TaskStatus;
import balabol.error.free.text.exceptions.NoTasksForProcessingException;
import balabol.error.free.text.exceptions.NotFoundException;
import balabol.error.free.text.exceptions.TextCorrectionException;
import balabol.error.free.text.repositories.TaskRepository;
import balabol.error.free.text.services.TaskService;
import balabol.error.free.text.services.TextCorrectionService;
import lombok.AllArgsConstructor;
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

    private final String NO_TASKS_FOUND_TO_PROCESS_MESSAGE = "no tasks found to process";

    private final TextCorrectionService textCorrectionService;

    private final TaskRepository taskRepository;

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
    public void processTask() {

        LOGGER.info("method processTask has started working");

        Task task = null;

        try {
            task = findTaskForProcessing();

            correctedTextInTask(task);
        } catch (TextCorrectionException e) {
            taskRepository.updateStatusAndErrorMessage(
                    task,
                    TaskStatus.COMPLETED_WITH_ERROR,
                    e.getMessage()
            );
            return;
        } catch (NoTasksForProcessingException e) {
            return;
        }

        LOGGER.info("method processTask has finished work");
    }

    @Override
    public Task findTaskForProcessing() {

        LOGGER.info("method findTaskForProcessing has started working");

        Task task = taskRepository
                .findFirstByStatus(TaskStatus.NEW)
                .orElseThrow(() -> {
                    LOGGER.warn(NO_TASKS_FOUND_TO_PROCESS_MESSAGE);
                    return new NoTasksForProcessingException(
                            NO_TASKS_FOUND_TO_PROCESS_MESSAGE
                    );
        });

        LOGGER.info("method findTaskForProcessing has finished work");

        return task;
    }

    @Override
    public void correctedTextInTask(Task task) {
        try {
            LOGGER.info("method correctedTextInTask has started working");

            String uncorrectedText = task.getText();

            String correctedText = textCorrectionService.correctErrorsInText(uncorrectedText);

            taskRepository.updateTextAndStatus(task, correctedText, TaskStatus.COMPLECTED);

            LOGGER.info("method correctedTextInTask has finished work");
        } catch (TextCorrectionException e) {
            LOGGER.error("error processing task text with id = {}",
                    task.getId());
            throw e;
        }
    }
}
