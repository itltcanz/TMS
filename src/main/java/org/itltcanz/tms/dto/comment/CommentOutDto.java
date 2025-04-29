package org.itltcanz.tms.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.itltcanz.tms.dto.user.UserOutDto;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CommentOutDto implements Serializable {
    private Integer id;
    private UserOutDto author;
    @NotBlank
    @Size(min = 1, max = 255, message = "CommentEntity must be between 1 and 255 characters")
    private String text;
}
