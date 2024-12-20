package org.itltcanz.tms.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.itltcanz.tms.dto.task.TaskInDto;
import org.itltcanz.tms.dto.task.TaskOutDto;
import org.itltcanz.tms.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Create a new task", description = "This endpoint allows you to create a new task with title, description, author, executor, status, and priority.")
    public ResponseEntity<TaskOutDto> addTask(@RequestBody @Valid TaskInDto taskInDto) {
        var taskOutDto = taskService.createTask(taskInDto);
        return ResponseEntity.ok(taskOutDto);
    }

    @GetMapping
    @Operation(summary = "Get list of tasks", description = "This endpoint returns a paginated list of tasks.")
    public ResponseEntity<Page<TaskOutDto>> getTasks(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "creationDate") String sortBy,
        @RequestParam(defaultValue = "desc") String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<TaskOutDto> taskOutDtos = taskService.getTasks(pageable);
        return ResponseEntity.ok(taskOutDtos);
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "Get task by ID", description = "This endpoint allows you to get the task details by ID.")
    public ResponseEntity<TaskOutDto> getTaskById(@PathVariable Integer taskId) {
        var taskOutDto = taskService.getTaskById(taskId);
        return ResponseEntity.ok(taskOutDto);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Update an existing task", description = "This endpoint allows you to update an existing task.")
    public ResponseEntity<TaskOutDto> updateTask(@PathVariable Integer id, @RequestBody @Valid TaskInDto taskInDto) {
        var taskOurDto = taskService.update(id, taskInDto);
        return ResponseEntity.ok(taskOurDto);
    }

    @PatchMapping("/{taskId}/executor")
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Update the executor of a task", description = "This endpoint allows you to update the executor of a task.")
    public ResponseEntity<TaskOutDto> updateTaskExecutor(@PathVariable Integer taskId, @RequestBody Map<String, Integer> newExecutorId) {
        var task = taskService.updateExecutor(taskId, newExecutorId.get("executor"));
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{taskId}/status")
    @Operation(summary = "Update the status of a task", description = "This endpoint allows you to update the status of a task.")
    public ResponseEntity<TaskOutDto> updateTaskStatus(@PathVariable Integer taskId, @RequestBody Map<String, Integer> newStatusId) {
        var task = taskService.updateStatus(taskId, newStatusId.get("status"));
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{taskId}/priority")
    @Operation(summary = "Update the priority of a task", description = "This endpoint allows you to update the priority of a task.")
    public ResponseEntity<TaskOutDto> updateTaskPriority(@PathVariable Integer taskId, @RequestBody Map<String, Integer> newPriorityId) {
        var task = taskService.updatePriority(taskId, newPriorityId.get("priority"));
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Delete a task", description = "This endpoint allows you to delete a task by ID.")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
