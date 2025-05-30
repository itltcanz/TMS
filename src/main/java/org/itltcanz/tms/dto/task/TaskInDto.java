package org.itltcanz.tms.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.itltcanz.tms.dto.user.UserInDto;
import org.itltcanz.tms.dto.comment.CommentInDto;
import org.itltcanz.tms.dto.priority.PriorityDto;
import org.itltcanz.tms.dto.status.StatusIdDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class TaskInDto {
    @NotBlank(message = "Title cannot be empty")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    private UserInDto executor;
    private StatusIdDto status;
    private PriorityDto priority;
    private List<CommentInDto> comments = new ArrayList<>();
    private LocalDateTime creationDate;
}
