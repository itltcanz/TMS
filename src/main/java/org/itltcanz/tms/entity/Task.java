package org.itltcanz.tms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @ManyToOne
    @NotBlank(message = "Author cannot be empty")
    private Account author;

    @ManyToOne
    @NotBlank(message = "Executor cannot be empty")
    private Account executor;

    @ManyToOne
    @NotBlank(message = "Status cannot be empty")
    private Status status;

    @ManyToOne
    @NotBlank(message = "Priority cannot be empty")
    private Priority priority;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Comment> comments;
}

