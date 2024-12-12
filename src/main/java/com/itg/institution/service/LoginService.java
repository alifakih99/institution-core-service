package com.itg.institution.service;

import com.itg.institution.dtos.LoginDto;
import com.itg.institution.response.Response;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

public interface LoginService
{
    Response login(LoginDto loginDto) throws AccessDeniedException;

    Response refreshToken(HttpServletRequest request) throws AccessDeniedException;
}
