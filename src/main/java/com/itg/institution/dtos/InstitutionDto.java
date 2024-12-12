package com.itg.institution.dtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class InstitutionDto
{
    private Long id;

    @NotNull(message = "Code Field Should Be Provided")
    @Size(max = 5, message = "Code Value Should Be Of Length 5 Max")
    private String code;

    @NotNull(message = "Name Field Should Be Provided")
    @NotEmpty(message = "Name Field Should Not Be Empty")
    @Size(max = 50, message = "Name Value Should Be Of Length 50 Max")
    private String name;

    @NotNull(message = "Status Field Should Be Provided")
    private Integer status;
}
