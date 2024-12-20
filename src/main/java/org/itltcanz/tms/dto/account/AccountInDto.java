package org.itltcanz.tms.dto.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountInDto {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
