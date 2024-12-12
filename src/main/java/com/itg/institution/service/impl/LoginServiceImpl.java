package com.itg.institution.service.impl;

import com.itg.institution.dtos.LoginDto;
import com.itg.institution.entities.LoginEntity;
import com.itg.institution.entities.UserEntity;
import com.itg.institution.repository.LoginRepository;
import com.itg.institution.repository.UserRepository;
import com.itg.institution.response.LoginResponse;
import com.itg.institution.response.Response;
import com.itg.institution.service.LoginService;
import com.itg.institution.shared.configuration.AppConfig;
import com.itg.institution.shared.security.CustomPasswordEncoder;
import com.itg.institution.shared.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService
{
    private final UserRepository userRepository;
    private final LoginRepository loginRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final CustomPasswordEncoder customPasswordEncoder;
    private final AppConfig appConfig;

    @Override
    public Response login(LoginDto loginDto) throws AccessDeniedException
    {
        try
        {
            String username = loginDto.getUsername();
            String password = loginDto.getPassword();
            Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);
            if(userEntityOptional.isEmpty())
            {
                throw new AccessDeniedException("Incorrect Credentials");
            }
            UserEntity userEntity = userEntityOptional.get();
            password = isPasswordValid(password, userEntity.getPassword());
            if(password == null)
            {
                throw new AccessDeniedException("Incorrect Credentials");
            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            String accessToken;
            String refreshToken;
            Optional<LoginEntity> loginEntityOptional = loginRepository.findFirstByUserAndExpiredAtAfterOrderByIdDesc(userEntity, LocalDateTime.now());
            if(loginEntityOptional.isPresent())
            {
                LoginEntity loginEntity = loginEntityOptional.get();
                accessToken = loginEntity.getAccessToken();
                refreshToken = loginEntity.getRefreshToken();
            }
            else
            {
                accessToken = jwtTokenProvider.generateAccessToken(userEntity.getUsername(), userEntity.getRole());
                refreshToken = jwtTokenProvider.generateRefreshToken(userEntity.getUsername());
                LoginEntity loginEntity = new LoginEntity().setUser(userEntity).setAccessToken(accessToken)
                        .setRefreshToken(refreshToken).setExpiredAt(LocalDateTime.now().plusSeconds(appConfig.getJwtExpiration()))
                        .setLoggedOut(false);
                loginRepository.save(loginEntity);
            }
            String expiredAfter = jwtTokenProvider.extractExpiration(accessToken).toString();

            LoginResponse loginResponse = new LoginResponse().setAccessToken(accessToken)
                    .setRefreshToken(refreshToken)
                    .setExpiresIn(expiredAfter);
            return new Response().setMessage("Login Successfully").setStatus(true).setCode(2000).setData(loginResponse);
        }
        catch (Exception e)
        {
            log.error("Error on login, {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Response refreshToken(HttpServletRequest request) throws AccessDeniedException
    {
        try
        {
            String refreshToken = request.getHeader("refresh-token");
            boolean isRefreshValid = jwtTokenProvider.validateRefreshToken(refreshToken);
            if(!isRefreshValid)
            {
                throw new AccessDeniedException("Unauthorized");
            }
            String username = jwtTokenProvider.extractUsername(refreshToken);
            Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);
            UserEntity userEntity = userEntityOptional.get();
            String accessToken = jwtTokenProvider.generateAccessToken(userEntity.getUsername(), userEntity.getRole());
            LoginEntity loginEntity = new LoginEntity().setUser(userEntity).setAccessToken(accessToken)
                    .setRefreshToken(refreshToken).setExpiredAt(LocalDateTime.now().plusSeconds(appConfig.getJwtExpiration())).setLoggedOut(false);
            loginRepository.save(loginEntity);
            String expiredAfter = jwtTokenProvider.extractExpiration(accessToken).toString();

            LoginResponse loginResponse = new LoginResponse().setAccessToken(accessToken)
                    .setRefreshToken(refreshToken)
                    .setExpiresIn(expiredAfter);
            return new Response().setMessage("Token Refreshed Successfully").setStatus(true).setCode(2000).setData(loginResponse);
        }
        catch (Exception e)
        {
            log.error("Error on refreshToken, {}", e.getMessage());
            throw e;
        }
    }

    private String isPasswordValid(String password, String hashedPassword)
    {
        boolean matches = customPasswordEncoder.matches(password, hashedPassword);
        if(matches)
        {
            return password;
        }
        return null;
    }
}
