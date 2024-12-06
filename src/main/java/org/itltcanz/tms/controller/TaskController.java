package org.itltcanz.tms.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.itltcanz.tms.entity.Comment;
import org.itltcanz.tms.entity.Task;
import org.itltcanz.tms.service.TaskService;
import org.springframework.http.ResponseEntity;
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
        var tasks = taskService.findAll();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Integer id) {
        var task = taskService.findById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody @Valid Task task) {
        var savedTask = taskService.save(task);
        return ResponseEntity.ok(savedTask);
    }

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<Task> addComment(@PathVariable Integer taskId, @RequestBody Comment comment) {
        var task = taskService.addComment(taskId, comment);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Integer id, @RequestBody @Valid Task updatedTask) {
        var task = taskService.update(id, updatedTask);
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
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
