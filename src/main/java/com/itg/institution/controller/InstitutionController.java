package com.itg.institution.controller;

import com.itg.institution.dtos.InstitutionDto;
import com.itg.institution.response.Response;
import com.itg.institution.service.InstitutionService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/institution")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class InstitutionController
{
    private final InstitutionService institutionService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'USER')")
    public ResponseEntity<Response> getAllInstitutions(@RequestParam("page") int page,
                                                       @RequestParam("size") int size)
    {
        Response response = institutionService.getAllInstitutions(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'USER')")
    public ResponseEntity<Response> getInstitutionById(@PathVariable("id") long id)
    {
        Response response = institutionService.getInstitutionById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'USER')")
    public ResponseEntity<Response> getActiveInstitution()
    {
        Response response = institutionService.getActiveInstitution();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'USER')")
    public ResponseEntity<Response> saveInstitution(@Valid @RequestBody InstitutionDto institutionDto)
    {
        Response response = institutionService.saveInstitution(institutionDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'USER')")
    public ResponseEntity<Response> deleteInstitution(@RequestParam("id") long id)
    {
        Response response = institutionService.deleteInstitution(id);
        return ResponseEntity.ok(response);
    }
}
