package com.itg.institution.controller;

import com.itg.institution.dtos.LoginDto;
import com.itg.institution.response.Response;
import com.itg.institution.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class LoginController
{
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Response> login(@Valid @RequestBody LoginDto loginDto) throws AccessDeniedException
    {
        Response response = loginService.login(loginDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Response> refreshToken(HttpServletRequest request) throws AccessDeniedException
    {
        Response response = loginService.refreshToken(request);
        return ResponseEntity.ok(response);
    }
}
