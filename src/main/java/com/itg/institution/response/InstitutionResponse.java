package com.itg.institution.response;

import com.itg.institution.entities.InstitutionEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class InstitutionResponse
{
    private long id;
    private int code;
    private String name;
    private int status;

    public InstitutionResponse from(InstitutionEntity institutionEntity)
    {
        return new InstitutionResponse().setId(institutionEntity.getId())
                .setCode(institutionEntity.getCode())
                .setName(institutionEntity.getName())
                .setStatus(institutionEntity.getStatus());
    }
}
