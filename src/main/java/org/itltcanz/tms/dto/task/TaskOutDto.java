package org.itltcanz.tms.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.itltcanz.tms.dto.account.AccountOutDto;
import org.itltcanz.tms.dto.comment.CommentOutDto;
import org.itltcanz.tms.entity.Priority;
import org.itltcanz.tms.entity.Status;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class TaskOutDto implements Serializable {
    private Integer id;
    @NotBlank(message = "Title cannot be empty")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private AccountOutDto author;
    private AccountOutDto executor;
    private Status status;
    private Priority priority;
    private List<CommentOutDto> comments;
    private LocalDateTime creationDate;
}
