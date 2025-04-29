package org.itltcanz.tms.dto.desk;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.itltcanz.tms.entity.StatusEntity;
import org.itltcanz.tms.entity.UserEntity;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeskOutDto {
    private Integer id;
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;
    private List<UserEntity> users;
    private List<StatusEntity> statuses;
}
