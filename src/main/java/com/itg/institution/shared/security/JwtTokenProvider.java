package com.itg.institution.shared.security;

import com.itg.institution.entities.LoginEntity;
import com.itg.institution.repository.LoginRepository;
import com.itg.institution.shared.configuration.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider
{
    private final JwtConfig jwtConfig;
    private final AppConfig appConfig;
    private final LoginRepository loginRepository;

    public String generateAccessToken(String username, String role)
    {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appConfig.getJwtExpiration() * 1000L);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtConfig.key(), SignatureAlgorithm.HS512)
                .claim("role", role)
                .compact();
    }

    public String generateRefreshToken(String username)
    {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appConfig.getRefreshExpiration() * 1000L);

        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expiryDate)
                .signWith(jwtConfig.key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateAccessToken(UserDetails userDetails, String token)
    {
        try
        {
            LocalDateTime expiry = extractExpiration(token);
            if(expiry.isBefore(LocalDateTime.now()))
            {
                return false;
            }
            Optional<LoginEntity> loginEntityOptional = loginRepository.findFirstByAccessTokenOrderByIdDesc(token);
            if(loginEntityOptional.isEmpty())
            {
                return false;
            }
            String username = extractUsername(token);
            if(username != null && userDetails != null && !username.equalsIgnoreCase(userDetails.getUsername()))
            {
                return false;
            }
            String role = null;
            if(userDetails != null)
            {
                for (GrantedAuthority authority : userDetails.getAuthorities())
                {
                    role = authority.getAuthority().replace("ROLE_", "");
                }
            }
            if(role != null && !role.equalsIgnoreCase(extractRole(token)))
            {
                return false;
            }
            LoginEntity loginEntity = loginEntityOptional.get();
            return !loginEntity.isLoggedOut();
        }
        catch (Exception e)
        {
            log.error("Error on validateAccessToken, {}", e.getMessage());
            return false;
        }
    }

    public boolean validateRefreshToken(String token)
    {
        try
        {
            if(token == null)
            {
                return false;
            }
            LocalDateTime expiry = extractExpiration(token);
            if(expiry.isBefore(LocalDateTime.now()))
            {
                return false;
            }
            Optional<LoginEntity> loginEntityOptional = loginRepository.findFirstByRefreshTokenOrderByIdDesc(token);
            if(loginEntityOptional.isEmpty())
            {
                return false;
            }
            LoginEntity loginEntity = loginEntityOptional.get();
            return !loginEntity.isLoggedOut();
        }
        catch (Exception e)
        {
            log.error("Error on validateRefreshToken, {}", e.getMessage());
            return false;
        }
    }

    public String extractRole(String token)
    {
        try
        {
            LocalDateTime expiry = extractExpiration(token);
            if(expiry.isBefore(LocalDateTime.now()))
            {
                return null;
            }
            return extractClaim(token).get("role").toString();
        }
        catch (Exception e)
        {
            log.error("Error on extractRole, {}", e.getMessage());
            throw e;
        }
    }

    public String extractUsername(String token)
    {
        try
        {
            LocalDateTime expiry = extractExpiration(token);
            if(expiry.isBefore(LocalDateTime.now()))
            {
                return null;
            }
            return extractClaim(token).getSubject();
        }
        catch (Exception e)
        {
            log.error("Error on extractUsername, {}", e.getMessage());
            throw e;
        }
    }

    public LocalDateTime extractExpiration(String token)
    {
        Instant instant = extractClaim(token).getExpiration().toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    public Claims extractClaim(String token)
    {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(jwtConfig.key())
                .build()
                .parseClaimsJws(token);
        return claimsJws.getBody();
    }
}

