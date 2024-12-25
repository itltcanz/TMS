package org.itltcanz.tms.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.itltcanz.tms.dto.task.TaskOutDto;
import org.itltcanz.tms.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "Add comment to the task.", description = "This endpoint allows you add comment to the task.")
    public ResponseEntity<TaskOutDto> addComment(
        @PathVariable @NotNull Integer taskId,
        @RequestBody @NotBlank String text) {
        var taskOutDto = taskService.addComment(taskId, text);
        return ResponseEntity.ok(taskOutDto);
    }

    @PatchMapping("/{commentId}")
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Update comment in the task.", description = "This endpoint allows you update comment in the task.")
    public ResponseEntity<TaskOutDto> updateComment(
        @PathVariable @NotNull Integer taskId,
        @PathVariable @NotNull Integer commentId,
        @RequestBody @NotBlank @Size(max = 255, message = "Comment must be between 1 and 255 characters") String text) {
        var taskOutDto = taskService.updateComment(taskId, commentId, text);
        return ResponseEntity.ok(taskOutDto);
    }

    @DeleteMapping("/{commentId}")
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Delete comment in the task.", description = "This endpoint allows you delete comment in the task.")
    public ResponseEntity<Void> deleteComment(
        @PathVariable @NotBlank Integer taskId,
        @PathVariable @NotBlank Integer commentId) {
        taskService.deleteComment(taskId, commentId);
        return ResponseEntity.noContent().build();
    }
}
