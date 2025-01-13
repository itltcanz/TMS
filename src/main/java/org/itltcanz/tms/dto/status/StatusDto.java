package org.itltcanz.tms.dto.status;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatusDto {
    @NotBlank
    private String name;
}
