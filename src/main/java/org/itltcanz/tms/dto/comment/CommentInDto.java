package org.itltcanz.tms.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentInDto {
    @NotBlank
    @Size(min = 1, max = 255, message = "CommentEntity must be between 1 and 255 characters")
    private String text;
}
