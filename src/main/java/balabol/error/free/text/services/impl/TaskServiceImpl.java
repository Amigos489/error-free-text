package balabol.error.free.text.services.impl;

import balabol.error.free.text.dtos.TaskCreation;
import balabol.error.free.text.entities.Task;
import balabol.error.free.text.enums.LanguageType;
import balabol.error.free.text.enums.TaskStatus;
import balabol.error.free.text.exceptions.TaskNotFoundException;
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

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(TaskServiceImpl.class.getName());

    private final String TASK_NOT_FOUND_MESSAGE = "task with id = %d not found";

    private final String NO_TASKS_FOUND_TO_PROCESS_MESSAGE = "no tasks found to process";

    private final TextCorrectionService textCorrectionService;

    private final TaskRepository taskRepository;

    @Override
    public int createTask(TaskCreation taskCreation) {

        LOGGER.info("method createTask has started working with parameters: " +
                        "text with length = {}, language = {}",
                taskCreation.text().length(), taskCreation.language());

        Task createdTask = new Task(
                taskCreation.text(),
                LanguageType.valueOf(taskCreation.language()),
                TaskStatus.NEW
        );

        createdTask = taskRepository.save(createdTask);

        LOGGER.info("method createTask has finished work, " +
                        "task with id = {} created", createdTask.getId());

        return createdTask.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Task findTaskById(int id) {

        LOGGER.info("method findTaskById has started working with parameters: id = {}", id);

        Task task = taskRepository
                .findById(id)
                .orElseThrow(() -> new TaskNotFoundException(
                        String.format(TASK_NOT_FOUND_MESSAGE, id)));

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
            Optional<Task> taskOptional = taskRepository.findFirstByStatus(TaskStatus.NEW);
            if (taskOptional.isEmpty()) {
                LOGGER.warn(NO_TASKS_FOUND_TO_PROCESS_MESSAGE);
                return;
            }
            task = taskOptional.get();

            String correctedText = textCorrectionService
                    .correctSpellInText(task.getText(), task.getLanguage());

            taskRepository.updateTextAndStatus(task, correctedText, TaskStatus.COMPLECTED);
        } catch (TextCorrectionException e) {
            taskRepository.updateStatusAndErrorMessage(
                    task, TaskStatus.COMPLETED_WITH_ERROR, e.getMessage());
            LOGGER.error(e.getMessage());
        } finally {
            LOGGER.info("method processTask has finished work");
        }

    }
}
