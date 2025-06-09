package co.ke.tucode.configjwt;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import co.ke.tucode.systemuser.entities.Africana_User;
import co.ke.tucode.systemuser.entities.Role;
import co.ke.tucode.systemuser.repositories.Africana_UserRepository;
import co.ke.tucode.systemuser.services.Africana_UserService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final Africana_UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector).servletPath("/");

        http
            .cors()
            .and()
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(mvc.pattern("/login_request"),
                                 mvc.pattern("/post_service"),
                                 mvc.pattern("/api/clients/files/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions().disable()) // for H2 console
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                            "http://localhost:3000",
                            "http://192.168.43.134:3000",
                            "https://www.housing.tucode.co.ke",
                            "https://www.capdo.org",
                            "https://www.boreshamaisha.tucode.co.ke",
                            "https://www.ebooks.tucode.co.ke")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    public CommandLineRunner createInitialUser(Africana_UserRepository userRepository) {
        return args -> {
            String defaultEmail = "admin@tucode.co.ke";
            if (userRepository.findByEmail(defaultEmail).isEmpty()) {
                Africana_User user = Africana_User.builder()
                        .username("admin")
                        .email(defaultEmail)
                        .password(passwordEncoder().encode("Password@2906"))
                        .role(Role.ADMIN)
                        .user_signature("initial_signature")
                        .build();

                userRepository.save(user);
                System.out.println("✔ Default admin user created.");
            } else {
                System.out.println("ℹ Default admin user already exists.");
            }
        };
    }
}
