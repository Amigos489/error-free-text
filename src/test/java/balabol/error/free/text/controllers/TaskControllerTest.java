package balabol.error.free.text.controllers;

import balabol.error.free.text.dtos.TaskCreation;
import balabol.error.free.text.dtos.TaskResponse;
import balabol.error.free.text.entities.Task;
import balabol.error.free.text.enums.LanguageType;
import balabol.error.free.text.enums.TaskStatus;
import balabol.error.free.text.exceptions.NotFoundException;
import balabol.error.free.text.mappers.TaskMapper;
import balabol.error.free.text.services.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Test TaskController")
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private TaskMapper taskMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    @DisplayName("When method createTask Then status created and task id")
    public void createTask_statusCreatedAndTaskId() throws Exception {

        TaskCreation taskCreationTest
                = new TaskCreation(
                        "textTest",
                LanguageType.RU
        );

        int testId = 1;

        Mockito.when(taskService.createTask(taskCreationTest))
                .thenReturn(testId);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsBytes(taskCreationTest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(testId));
        verify(taskService, times(1))
                .createTask(taskCreationTest);
    }

    @Test
    @DisplayName("Given task with id exists When method findTaskById Then status ok and taskResponse")
    public void taskWithIdExists_findTaskById_statusOkAndTaskResponse() throws Exception {

        int testId = 1;

        TaskResponse taskResponse = new TaskResponse(
                null,
                TaskStatus.NEW,
                null
        );

        Task task = new Task("text test", LanguageType.RU, TaskStatus.NEW);

        Mockito.when(taskService.findTaskById(testId))
                .thenReturn(task);

        Mockito.when(taskMapper.mappingToTaskResponse(task)).thenReturn(taskResponse);

        mockMvc.perform(get("/api/tasks/{id}", testId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status")
                        .value(String.valueOf(TaskStatus.NEW)));
        verify(taskService, times(1))
                .findTaskById(testId);
    }

    @Test
    @DisplayName("Given task with id not exists When method findTaskById Then status not found")
    public void taskWithIdNotExists_findTaskById_statusNotFound() throws Exception {

        int testId = 999;

        Mockito.when(taskService.findTaskById(testId))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/api/tasks/{id}", testId))
                .andExpect(status().isNotFound());
        verify(taskService, times(1))
                .findTaskById(testId);
    }
}
