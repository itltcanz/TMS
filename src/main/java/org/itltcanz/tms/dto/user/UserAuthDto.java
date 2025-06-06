package org.itltcanz.tms.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserAuthDto {
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 8, max = 255, message = "Password must be between 1 and 255 characters")
    private String password;
}
