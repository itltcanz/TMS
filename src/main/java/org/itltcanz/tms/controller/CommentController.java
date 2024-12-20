package org.itltcanz.tms.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.itltcanz.tms.dto.task.TaskOutDto;
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
    public ResponseEntity<TaskOutDto> addComment(@PathVariable Integer taskId, @RequestBody String text) {
        var taskOutDto = taskService.addComment(taskId, text);
        return ResponseEntity.ok(taskOutDto);
    }

    @PatchMapping("/{commentId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<TaskOutDto> updateComment(
        @PathVariable @NotBlank Integer taskId,
        @PathVariable @NotBlank Integer commentId,
        @RequestBody String text) {
        var taskOutDto = taskService.updateComment(taskId, commentId, text);
        return ResponseEntity.ok(taskOutDto);
    }

    @DeleteMapping("/{commentId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteComment(
        @PathVariable @NotBlank Integer taskId,
        @PathVariable @NotBlank Integer commentId) {
        taskService.deleteComment(taskId, commentId);
        return ResponseEntity.noContent().build();
    }
}
