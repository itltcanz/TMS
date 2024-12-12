package org.itltcanz.tms.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.itltcanz.tms.entity.Task;
import org.itltcanz.tms.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final TaskService taskService;
    @PostMapping
    public ResponseEntity<Task> addComment(@PathVariable Integer taskId, @RequestBody String text) {
        var task = taskService.addComment(taskId, text);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{commentId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteComment(@PathVariable @NotBlank Integer taskId, @PathVariable @NotBlank Integer commentId) {
        taskService.deleteComment(taskId, commentId);
        return ResponseEntity.noContent().build();
    }
}
