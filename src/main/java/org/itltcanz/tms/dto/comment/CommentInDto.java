package org.itltcanz.tms.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.itltcanz.tms.dto.account.AccountInDto;

@Data
@NoArgsConstructor
public class CommentInDto {
    private AccountInDto author;
    @NotBlank
    @Size(min = 1, max = 255, message = "Comment must be between 1 and 255 characters")
    private String text;
}
