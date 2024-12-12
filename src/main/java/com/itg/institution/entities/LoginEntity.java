package com.itg.institution.entities;

import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "user_track")
public class LoginEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @Column(name = "access_token", updatable = false)
    private String accessToken;

    @Column(name = "refresh_token", updatable = false)
    private String refreshToken;

    @Column(name = "expired_at", updatable = false)
    private LocalDateTime expiredAt;

    @Column(name = "logged_out", updatable = false)
    private boolean loggedOut;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;
}
