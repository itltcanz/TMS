package org.itltcanz.tms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Title cannot be null")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @ManyToOne
    @NotNull(message = "Author cannot be null")
    private Account author;

    @ManyToOne
    @NotNull(message = "Executor cannot be null")
    private Account executor;

    @ManyToOne
    @NotNull(message = "Status cannot be null")
    private Status status;

    @ManyToOne
    @NotNull(message = "Priority cannot be null")
    private Priority priority;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Comment> comments;
}

