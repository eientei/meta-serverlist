package ru.metaconference.serverlist.controller.data;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by user on 2017-09-03.
 */

@Data
public class UserCreateRequest {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @Email
    private String email;
}
