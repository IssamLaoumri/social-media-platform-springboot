package com.laoumri.socialmediaplatformspringboot.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Author: Issam Laoumri
 * Email: contact@issamlaoumri.com
 * Date Created: 14/01/2025
 * Version: 0.0.1
 */
@Entity
@Table(name = "reftokens")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Column(nullable = false, unique = true)
    private String refreshToken;
    @Column(nullable = false)
    private Instant expiresAt;
}
