package org.itltcanz.tms.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.itltcanz.tms.entity.RoleEntity;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserOutDto implements Serializable {
    private Integer id;
    private String email;
    private RoleEntity roleEntity;
}
