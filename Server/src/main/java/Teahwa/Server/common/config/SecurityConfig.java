package Teahwa.Server.common.config;

import Teahwa.Server.auth.jwt.JwtFilter;
import Teahwa.Server.auth.repository.RefreshTokenRepository;
import Teahwa.Server.auth.util.CookieUtil;
import Teahwa.Server.auth.util.JwtUtil;
import Teahwa.Server.common.constants.ErrorCode;
import Teahwa.Server.common.exceptions.GeneralException;
import Teahwa.Server.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final CorsFilter corsFilter;

    // WHITELIST
    private static final String[] PUBLIC_WHITELIST = {
            "/article",
            "/article/",
            "/article/detail/**",
            "/api/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {

        //JwtFilter 추가
        JwtFilter jwtFilter = new JwtFilter(jwtUtil, cookieUtil, refreshTokenRepository, userRepository);

        return http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable()) // CORS 설정
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/api/signup", "/api/signin").permitAll()
                                .requestMatchers("/article", "/article/**").permitAll()
                                .anyRequest().authenticated()
                )
                //jwtfilter, login 추가
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(formLogin -> formLogin.disable())
                .logout(logout -> logout.disable())
                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig
                                .authenticationEntryPoint(unauthorizedEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler)).build();
    }

    private final AuthenticationEntryPoint unauthorizedEntryPoint = (request, response, authException) -> {
        handleException(response, new GeneralException(ErrorCode.UNAUTHORIZED));
    };

    private final AccessDeniedHandler accessDeniedHandler = (request, response, accessDeniedException) -> {
        handleException(response, new GeneralException(ErrorCode.FORBIDDEN_ACCESS));
    };

    private void handleException(HttpServletResponse response, GeneralException exception) throws IOException {
        response.setStatus(exception.getErrorCode().getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(exception.toErrorResponse()));
        writer.flush();
    }
}