package com.itg.institution.repository;

import com.itg.institution.entities.InstitutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstitutionRepository extends JpaRepository<InstitutionEntity, Long>
{
    List<InstitutionEntity> findByStatus(int status);
}
