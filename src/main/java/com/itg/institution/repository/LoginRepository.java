package com.itg.institution.repository;

import com.itg.institution.entities.LoginEntity;
import com.itg.institution.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<LoginEntity, Long>
{
    Optional<LoginEntity> findFirstByAccessTokenOrderByIdDesc(String accessToken);

    Optional<LoginEntity> findFirstByUserAndExpiredAtAfterOrderByIdDesc(UserEntity userEntity, LocalDateTime expiredAt);

    Optional<LoginEntity> findFirstByRefreshTokenOrderByIdDesc(String refreshToken);
}