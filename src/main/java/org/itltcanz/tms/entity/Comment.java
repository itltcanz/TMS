package org.itltcanz.tms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private Account author;
    @NotBlank
    @Size(min = 1, max = 255, message = "Comment must be between 1 and 255 characters")
    private String text;

    public Comment(Account author, String text) {
        this.author = author;
        this.text = text;
    }
}
