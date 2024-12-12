package com.itg.institution.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class InstitutionListResponse
{
    private int count;
    private List<InstitutionResponse> institutions;
}
