package org.itltcanz.tms.dto.account;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.itltcanz.tms.entity.Role;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AccountOutDto implements Serializable {
    private Integer id;
    private String email;
    private Role role;
}
