package balabol.error.free.text.services;

import balabol.error.free.text.dtos.TaskCreation;
import balabol.error.free.text.entities.Task;
import balabol.error.free.text.enums.LanguageType;
import balabol.error.free.text.enums.TaskStatus;
import balabol.error.free.text.exceptions.NotFoundException;
import balabol.error.free.text.repositories.TaskRepository;
import balabol.error.free.text.services.impl.TaskServiceImpl;
import balabol.error.free.text.services.impl.TextCorrectionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test TaskServiceImpl")
public class TaskServiceImplTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TextCorrectionServiceImpl textCorrectionService;

    @Mock
    private TaskRepository taskRepository;

    @Test
    @DisplayName("When method createTask Then task id")
    public void createTask_taskId() {

        int testId = 1;

        Task expectedTask = new Task(
                "text test",
                LanguageType.RU,
                TaskStatus.NEW
        );
        expectedTask.setId(testId);

        TaskCreation taskCreationTest
                = new TaskCreation("text test", "RU");

        Mockito.when(taskRepository.save(any(Task.class)))
                .thenReturn(expectedTask);

        int actualTaskId = taskService.createTask(taskCreationTest);

        Assertions.assertEquals(expectedTask.getId(), actualTaskId);
    }

    @Test
    @DisplayName("Given task with id exists When method findTaskById Then taskResponse")
    public void taskWithIdExists_findTaskById_taskResponse() {

        int testId = 1;

        Task expectedTask = new Task(
                "text test",
                LanguageType.RU,
                TaskStatus.NEW
        );
        expectedTask.setId(testId);

        Mockito.when(taskRepository.findById(testId))
                .thenReturn(Optional.of(expectedTask));

        Task actualTask = taskService.findTaskById(testId);

        Assertions.assertEquals(expectedTask, actualTask);
    }

    @Test
    @DisplayName("Given task with id not exists When method findTaskById Then notFoundException")
    public void taskWithIdNotExists_findTaskById_notFoundException() {

        int testId = 999;

        Mockito.when(taskRepository.findById(testId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class,
                () -> taskService.findTaskById(testId));
    }

    @Test
    @DisplayName("When method processTask Then void")
    void processTask_void() {

        int testId = 1;

        Task expectedTask = new Task(
                "text test",
                LanguageType.RU,
                TaskStatus.NEW
        );
        expectedTask.setId(testId);

        Mockito.when(taskRepository.findFirstByStatus(TaskStatus.NEW))
                .thenReturn(Optional.of(expectedTask));

        Mockito.when(textCorrectionService
                .correctSpellInText(expectedTask.getText(), expectedTask.getLanguage()))
                .thenReturn("corrected text test");

        Assertions.assertDoesNotThrow(() -> taskService.processTask());
        verify(taskRepository, times(1))
                .findFirstByStatus(TaskStatus.NEW);
        verify(textCorrectionService, times(1))
                .correctSpellInText(expectedTask.getText(), expectedTask.getLanguage());
    }
}
