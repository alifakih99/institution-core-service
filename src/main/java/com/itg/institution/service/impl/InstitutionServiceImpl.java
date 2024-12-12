package com.itg.institution.service.impl;

import com.itg.institution.dtos.InstitutionDto;
import com.itg.institution.entities.InstitutionEntity;
import com.itg.institution.repository.InstitutionRepository;
import com.itg.institution.response.InstitutionListResponse;
import com.itg.institution.response.InstitutionResponse;
import com.itg.institution.response.Response;
import com.itg.institution.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class InstitutionServiceImpl implements InstitutionService
{
    private final InstitutionRepository institutionRepository;

    @Override
    public Response getAllInstitutions(int page, int size)
    {
        try
        {
            log.info("Start Getting All Institutions");

            Pageable pageable = PageRequest.of(page, size);

            Page<InstitutionEntity> institutionEntitiesPage = institutionRepository.findAll(pageable);

            List<InstitutionEntity> institutionEntitiesList = institutionEntitiesPage.getContent();
            if(institutionEntitiesList.isEmpty())
            {
                log.info("No Institutions Found");
                return new Response().setMessage("No Institutions Found").setStatus(false).setCode(2001);
            }

            List<InstitutionResponse> institutionDtos = new ArrayList<>();
            institutionEntitiesList.stream().map(institution -> new InstitutionResponse().from(institution))
                    .forEach(institutionDtos::add);

            InstitutionListResponse institutionListResponse = new InstitutionListResponse()
                    .setCount((int) institutionEntitiesPage.getTotalElements())
                    .setInstitutions(institutionDtos);
            Response response = new Response().setMessage("Retrieving Institutions Successfully").setStatus(true).setCode(2000)
                    .setData(institutionListResponse);
            log.info("Finish Getting All Institutions");
            return response;
        }
        catch (Exception e)
        {
            log.error("Error on getAllInstitutions, {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Response getInstitutionById(long id)
    {
        try
        {
            log.info("Start Getting Institution With Id {}", id);

            Optional<InstitutionEntity> institutionEntityOptional = institutionRepository.findById(id);
            if(institutionEntityOptional.isEmpty())
            {
                log.info("No Institution Found With Id {}", id);
                return new Response().setMessage("No Institution Found With Id " + id).setStatus(false).setCode(2001);
            }

            InstitutionEntity institutionEntity = institutionEntityOptional.get();
            InstitutionResponse institutionResponse = new InstitutionResponse().from(institutionEntity);
            Response response = new Response().setMessage("Retrieving Institution Successfully").setStatus(true).setCode(2000)
                    .setData(institutionResponse);
            log.info("Finish Getting Institution By Id");
            return response;
        }
        catch (Exception e)
        {
            log.error("Error on getInstitutionById, {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Response getActiveInstitution()
    {
        try
        {
            log.info("Start Getting Active Institution");

            List<InstitutionEntity> institutionEntitiesList = institutionRepository.findByStatus(1);
            if(institutionEntitiesList.isEmpty())
            {
                log.info("No Active Institutions");
                return new Response().setMessage("No Active Institutions").setStatus(false).setCode(2001);
            }

            List<InstitutionResponse> institutionDtos = new ArrayList<>();
            institutionEntitiesList.stream().map(institution -> new InstitutionResponse().from(institution))
                    .forEach(institutionDtos::add);

            InstitutionListResponse institutionListResponse = new InstitutionListResponse()
                    .setCount(institutionEntitiesList.size())
                    .setInstitutions(institutionDtos);
            Response response = new Response().setMessage("Retrieving Active Institutions Successfully").setStatus(true).setCode(2000)
                    .setData(institutionListResponse);
            log.info("Finish Getting Active Institution");
            return response;
        }
        catch (Exception e)
        {
            log.error("Error on getActiveInstitution, {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Response saveInstitution(InstitutionDto institutionDto)
    {
        try
        {
            log.info("Start Saving Institution With Entity {}", institutionDto);

            if(institutionDto.getId() != null && institutionDto.getId() != 0)
            {
                Optional<InstitutionEntity> institutionEntityOptional = institutionRepository.findById(institutionDto.getId());
                if(institutionEntityOptional.isEmpty())
                {
                    log.info("No Institution Found With Id {}", institutionDto.getId());
                    return new Response().setMessage("No Institution Found With Id " + institutionDto.getId()).setStatus(false)
                            .setCode(2001);
                }
                InstitutionEntity institutionEntity = institutionEntityOptional.get();
                institutionEntity.setCode(Integer.parseInt(institutionDto.getCode())).setStatus(institutionDto.getStatus())
                        .setName(institutionDto.getName());
                institutionRepository.save(institutionEntity);
            }
            else
            {
                InstitutionEntity institutionEntity = new InstitutionEntity().from(institutionDto);
                institutionRepository.save(institutionEntity);
            }

            Response response = new Response().setMessage("Saving Institution Successfully").setStatus(true).setCode(2000);
            log.info("Finish Saving Institution");
            return response;
        }
        catch (Exception e)
        {
            log.error("Error on saveInstitution, {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Response deleteInstitution(long id)
    {
        try
        {
            log.info("Start Deleting Institution With Id {}", id);

            Optional<InstitutionEntity> institutionEntityOptional = institutionRepository.findById(id);
            if(institutionEntityOptional.isEmpty())
            {
                log.info("No Institution Found With Id {}", id);
                return new Response().setMessage("No Institution Found With Id " + id).setStatus(false).setCode(2001);
            }

            InstitutionEntity institutionEntity = institutionEntityOptional.get();
            institutionRepository.delete(institutionEntity);

            Response response = new Response().setMessage("Deleting Institution Successfully").setStatus(true).setCode(2000);
            log.info("Finish Deleting Institution");
            return response;
        }
        catch (Exception e)
        {
            log.error("Error on deleteInstitution, {}", e.getMessage());
            throw e;
        }
    }
}
