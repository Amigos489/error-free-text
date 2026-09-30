package balabol.error.free.text.controllers;

import balabol.error.free.text.dtos.TaskCreation;
import balabol.error.free.text.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    public ResponseEntity<?> createTask(@Valid
                                        @RequestBody TaskCreation taskCreation) {
        return new ResponseEntity<>(taskService.createTask(), HttpStatus.CREATED);
    }
}
