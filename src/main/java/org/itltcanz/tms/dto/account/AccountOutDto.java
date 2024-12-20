package org.itltcanz.tms.dto.account;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.itltcanz.tms.entity.Role;

@Data
@NoArgsConstructor
public class AccountOutDto {
    private Integer id;
    private String email;
    private Role role;
}
