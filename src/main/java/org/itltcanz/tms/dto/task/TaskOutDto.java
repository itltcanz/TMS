package org.itltcanz.tms.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.itltcanz.tms.dto.user.UserOutDto;
import org.itltcanz.tms.dto.comment.CommentOutDto;
import org.itltcanz.tms.entity.PriorityEntity;
import org.itltcanz.tms.entity.StatusEntity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskOutDto implements Serializable {

    private Integer id;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @NotBlank(message = "Description cannot be empty")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private UserOutDto author;

    private UserOutDto executor;

    private StatusEntity statusEntity;

    private PriorityEntity priorityEntity;

    private List<CommentOutDto> comments = new ArrayList<>();

    private LocalDateTime creationDate;
}
