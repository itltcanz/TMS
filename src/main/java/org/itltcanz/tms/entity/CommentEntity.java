package org.itltcanz.tms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @ManyToOne
    private UserEntity author;
    @NotBlank
    @Size(min = 1, max = 255, message = "CommentEntity must be between 1 and 255 characters")
    private String text;
    @ManyToOne
    private TaskEntity task;

    public CommentEntity(UserEntity author, String text) {
        this.author = author;
        this.text = text;
    }
}