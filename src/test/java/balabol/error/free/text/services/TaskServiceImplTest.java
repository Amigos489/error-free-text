package balabol.error.free.text.services;

import balabol.error.free.text.dtos.TaskCreation;
import balabol.error.free.text.entities.Task;
import balabol.error.free.text.enums.LanguageType;
import balabol.error.free.text.enums.TaskStatus;
import balabol.error.free.text.repositories.TaskRepository;
import balabol.error.free.text.services.impl.TaskServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test TaskServiceImpl")
public class TaskServiceImplTest {

    @InjectMocks
    private TaskServiceImpl taskService;

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
                = new TaskCreation("text test", LanguageType.RU);

        Mockito.when(taskRepository.save(any(Task.class)))
                .thenReturn(expectedTask);

        int actualTaskId = taskService.createTask(taskCreationTest);

        Assertions.assertEquals(expectedTask.getId(), actualTaskId);
    }
}
