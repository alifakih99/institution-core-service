package com.itg.institution.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginResponse
{
    private String accessToken;
    private String refreshToken;
    private String expiresIn;
}
