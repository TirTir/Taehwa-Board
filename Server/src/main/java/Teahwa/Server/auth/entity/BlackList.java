package Teahwa.Server.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 500)
    private String accessToken;

    @Column(nullable = false)
    private LocalDateTime expiration;

    public BlackList(String accessToken, LocalDateTime expiration) {
        this.accessToken = accessToken;
        this.expiration = expiration;
    }
}