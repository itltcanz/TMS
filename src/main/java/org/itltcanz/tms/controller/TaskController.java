package org.itltcanz.tms.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.itltcanz.tms.entity.Task;
import org.itltcanz.tms.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getTasks() {
        var tasks = taskService.getTasksByAccount();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Integer taskId) {
        var task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Task> createTask(@RequestBody @Valid Task task) {
        var savedTask = taskService.save(task);
        return ResponseEntity.ok(savedTask);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Task> updateTask(@PathVariable Integer id, @RequestBody @Valid Task updatedTask) {
        var task = taskService.update(id, updatedTask);
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{taskId}/executor")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Task> updateTaskExecutor(@PathVariable Integer taskId, @RequestBody Map<String, Integer> newExecutorId) {
        var task = taskService.updateExecutor(taskId, newExecutorId.get("executor"));
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Integer taskId, @RequestBody Map<String, Integer> newStatusId) {
        var task = taskService.updateStatus(taskId, newStatusId.get("status"));
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{taskId}/priority")
    public ResponseEntity<Task> updateTaskPriority(@PathVariable Integer taskId, @RequestBody Map<String, Integer> newPriorityId) {
        var task = taskService.updatePriority(taskId, newPriorityId.get("priority"));
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
