package com.itg.institution.shared.security;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter
{

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    {
        try
        {
            if(request.getRequestURI().startsWith("/api/v1/auth"))
            {
                filterChain.doFilter(request, response);
                return;
            }

            String token = extractToken(request);
            if(token != null)
            {
                String username = jwtTokenProvider.extractUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                boolean isTokenValid = jwtTokenProvider.validateAccessToken(userDetails, token);
                if(!isTokenValid)
                {
                    throw new AccessDeniedException("Unauthorized Request");
                }

                if(SecurityContextHolder.getContext().getAuthentication() == null)
                {
                    JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response);
        }
        catch (AccessDeniedException e)
        {
            log.error("Error on doFilterInternal AccessDenied, {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        catch (Exception e)
        {
            log.error("Error on doFilterInternal Exception, {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private String extractToken(HttpServletRequest request)
    {
        String authorization = request.getHeader("Authorization");
        if(authorization == null)
        {
            return null;
        }
        return authorization.replace("Bearer ", "").replace("bearer ", "");
    }
}

