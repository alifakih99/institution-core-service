package com.itg.institution.shared.security;

import com.itg.institution.entities.UserEntity;
import com.itg.institution.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService
{
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
    {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);
        if(userEntityOptional.isEmpty())
        {
            throw new UsernameNotFoundException("Username " + username + " Not Found");
        }
        UserEntity userEntity = userEntityOptional.get();
        return new org.springframework.security.core.userdetails.User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                true,
                true,
                true,
                true,
                getAuthorities(userEntity.getRole()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role)
    {
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(role));
        return roles;
    }
}

