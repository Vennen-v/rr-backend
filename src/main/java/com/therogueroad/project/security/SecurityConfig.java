package com.therogueroad.project.security;

import com.therogueroad.project.models.AppRole;
import com.therogueroad.project.models.Role;
import com.therogueroad.project.models.User;
import com.therogueroad.project.repositories.RoleRepository;
import com.therogueroad.project.repositories.UserRepository;
import com.therogueroad.project.security.jwt.AuthEntryPointJwt;
import com.therogueroad.project.security.jwt.AuthTokenFilter;
import com.therogueroad.project.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

import java.util.Set;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((auth) -> auth.requestMatchers("/api/**").permitAll()
                .anyRequest().authenticated());


        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource){
//        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
//        UserDetails user = org.springframework.security.core.userdetails.User.withUsername("yoshi35")
//                    .password("{noop}meepmoop")
//                    .roles("USER")
//                    .build();
//        if (!manager.userExists("yoshi35")){
//            manager.createUser(user);
//        }
//        UserDetails user2 = org.springframework.security.core.userdetails.User.withUsername("sward140")
//                    .password("{noop}meepmoop")
//                    .roles("USER")
//                    .build();
//        if (!manager.userExists("sward140")){
//            manager.createUser(user2);
//        }
//        return manager;
//    }



    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {

            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> {
                        Role newUserRole = new Role(AppRole.ROLE_USER);
                        return roleRepository.save(newUserRole);
                    });


            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
                        return roleRepository.save(newAdminRole);
                    });

            Set<Role> userRoles = Set.of(userRole);
            Set<Role> adminRoles = Set.of(userRole, adminRole);

            if (!userRepository.existsByUserName("yoshi35")) {
                User user = new User("yoshi35","Yoshi Vennen", "yoshi@gmail.com", passwordEncoder().encode("meepmoop"));
                userRepository.save(user);
            }

            if (!userRepository.existsByUserName("sward140")) {
                User user2 = new User("sward140", "Emma S", "sward@gmail.com", passwordEncoder().encode("meepmoop"));
                userRepository.save(user2);
            }

            userRepository.findByUserName("yoshi35").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepository.save(user);
            });

            userRepository.findByUserName("sward140").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepository.save(user);
            });
        };
    }
}

