package com.itg.institution.entities;

import com.itg.institution.dtos.InstitutionDto;
import javax.persistence.*;

import com.itg.institution.response.InstitutionResponse;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "institution")
public class InstitutionEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "institution_id", nullable = false, unique = true)
    private long id;

    @Column(name = "institution_code", nullable = false, length = 5)
    private int code;

    @Column(name = "institution_name", nullable = false, length = 50)
    private String name;

    @Column(name = "institution_status", nullable = false)
    private int status;

    @CreationTimestamp
    @Column(name = "institution_created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "institution_updated_at", insertable = false)
    private LocalDateTime updatedAt;

    public InstitutionEntity from(InstitutionDto institutionDto)
    {
        return new InstitutionEntity().setCode(Integer.parseInt(institutionDto.getCode()))
                .setName(institutionDto.getName())
                .setStatus(institutionDto.getStatus());
    }
}
