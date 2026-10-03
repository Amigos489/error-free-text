package balabol.error.free.text.controllers;

import balabol.error.free.text.dtos.TaskCreation;
import balabol.error.free.text.entities.Task;
import balabol.error.free.text.mappers.TaskMapper;
import balabol.error.free.text.services.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskCreation taskCreation) {
        return new ResponseEntity<>(taskService.createTask(taskCreation), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findTaskById(@PathVariable("id") int id) {

        Task task = taskService.findTaskById(id);

        return new ResponseEntity<>(
                taskMapper.mappingToTaskResponse(task), HttpStatus.OK);
    }
}
