package org.itltcanz.tms.dto.priority;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PriorityDto {
    @NotBlank
    private String name;
}
