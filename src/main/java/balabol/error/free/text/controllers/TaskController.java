package balabol.error.free.text.unit.controllers;

import balabol.error.free.text.unit.dtos.TaskCreation;
import balabol.error.free.text.unit.entities.Task;
import balabol.error.free.text.unit.mappers.TaskMapper;
import balabol.error.free.text.unit.services.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class.getName());

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskCreation taskCreation) {

        LOGGER.info("method createTask received request with parameters: " +
                        "text with length = {}, language = {}",
                taskCreation.text().length(), taskCreation.language());

        ResponseEntity<?> response = new ResponseEntity<>(
                taskService.createTask(taskCreation),
                HttpStatus.CREATED);

        LOGGER.info("method createTask returned response with code = {}",
                response.getStatusCode());

        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findTaskById(@PathVariable("id") int id) {

        LOGGER.info("method findTaskById received request with parameters: id = {}", id);

        Task task = taskService.findTaskById(id);

        ResponseEntity<?> response = new ResponseEntity<>(
                taskMapper.mappingToTaskResponse(task),
                HttpStatus.OK);

        LOGGER.info("method findTaskById returned response with code = {}",
                response.getStatusCode());

        return response;
    }
}
