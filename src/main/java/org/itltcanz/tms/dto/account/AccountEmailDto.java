package org.itltcanz.tms.dto.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountEmailDto {
    @NotBlank
    private String email;
}
