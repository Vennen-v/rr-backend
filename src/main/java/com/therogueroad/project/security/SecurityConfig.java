package com.therogueroad.project.security;

import com.therogueroad.project.models.Userr;
import com.therogueroad.project.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((requests) -> requests
                .anyRequest().authenticated());
        http.csrf(csrf -> csrf.disable());
        http.httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource){
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        UserDetails user = User.withUsername("yoshi35")
                    .password("{noop}meepmoop")
                    .roles("USER")
                    .build();
        if (!manager.userExists("yoshi35")){
            manager.createUser(user);
        }
        UserDetails user2 = User.withUsername("sward140")
                    .password("{noop}meepmoop")
                    .roles("USER")
                    .build();
        if (!manager.userExists("sward140")){
            manager.createUser(user2);
        }
        return manager;
    }


    @Bean
    public CommandLineRunner initData(UserRepository userRepository) {
        return args -> {

            if (!userRepository.existsByUserName("yoshi35")) {
                Userr user = new Userr("yoshi35", "yoshi@gmail.com", "meepmoop");
                user.setDisplayName("Yoshi Vennen");
                userRepository.save(user);
            }

            if (!userRepository.existsByUserName("sward140")) {
                Userr user2 = new Userr("sward140", "sward@gmail.com", "meepmoop");
                user2.setDisplayName("Emma Sward");
                userRepository.save(user2);
            }
        };
    }
}

