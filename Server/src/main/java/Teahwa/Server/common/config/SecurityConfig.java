package Teahwa.Server.common.config;

import Teahwa.Server.auth.jwt.JwtAccessDeniedHandler;
import Teahwa.Server.auth.jwt.JwtAuthenticationEntryPoint;
import Teahwa.Server.auth.jwt.JwtFilter;
import Teahwa.Server.auth.repository.RefreshTokenRepository;
import Teahwa.Server.auth.util.CookieUtil;
import Teahwa.Server.auth.util.JwtUtil;
import Teahwa.Server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //JwtFilter 추가
        JwtFilter jwtFilter = new JwtFilter(jwtUtil, cookieUtil, refreshTokenRepository, userRepository);

        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable()) // CORS 설정
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(jwtAuthenticationEntryPoint);
                    exception.accessDeniedHandler(jwtAccessDeniedHandler);
                })
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/signup", "/api/signin").permitAll()
                        .requestMatchers("/article","/article/", "/article/detail/**").permitAll()
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}