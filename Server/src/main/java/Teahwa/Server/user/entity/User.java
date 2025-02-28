package Teahwa.Server.user.entity;

import jakarta.persistence.*;
        import lombok.*;
        import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Entity
@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String password;

}
