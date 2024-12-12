package com.itg.institution.service;

import com.itg.institution.dtos.InstitutionDto;
import com.itg.institution.response.Response;

public interface InstitutionService
{
    Response getAllInstitutions(int page, int size);

    Response getInstitutionById(long id);

    Response getActiveInstitution();

    Response saveInstitution(InstitutionDto institutionDto);

    Response deleteInstitution(long id);
}
