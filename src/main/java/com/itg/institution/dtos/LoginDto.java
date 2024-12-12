package com.itg.institution.dtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDto
{
    @NotNull(message = "Username Field Should Be Provided")
    @NotEmpty(message = "Username Field Should Not Be Empty")
    private String username;

    @NotNull(message = "Password Field Should Be Provided")
    @NotEmpty(message = "Password Field Should Not Be Empty")
    private String password;
}
