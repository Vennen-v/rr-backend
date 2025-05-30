package com.therogueroad.project.controllers;

import com.therogueroad.project.dto.UserDTO;
import com.therogueroad.project.dto.UserDTOO;
import com.therogueroad.project.models.AppRole;
import com.therogueroad.project.models.Role;
import com.therogueroad.project.models.User;
import com.therogueroad.project.repositories.RoleRepository;
import com.therogueroad.project.repositories.UserRepository;
import com.therogueroad.project.security.jwt.*;
import com.therogueroad.project.security.services.UserDetailsImpl;
import com.therogueroad.project.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

    @Value("${spring.rr.app.jwtCookieName}")
    private String jwtCookie;

    @PostMapping("/auth/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), userDetails.getDisplayName(), roles, jwtCookie.toString());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(response);
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest){
        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use"));
        }

        User user = new User(signUpRequest.getUsername(), signUpRequest.getDisplayName(), signUpRequest.getEmail(), passwordEncoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(() -> new RuntimeException(("Error: Role is not found")));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if (role.equals("admin")) {
                    Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException(("Error: Role is not found")));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(() -> new RuntimeException(("Error: Role is not found")));
                    roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

    }

    @PostMapping("/auth/signout")
    public ResponseEntity<?> signOutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new MessageResponse("You have been signed out"));
    }

    @GetMapping("/user")
    public ResponseEntity<UserDTOO> getUserDetails(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));
        return new ResponseEntity<>(modelMapper.map(user, UserDTOO.class), HttpStatus.OK);
    }

    @GetMapping("/check-cookie")
    public ResponseEntity<String> checkHttpOnlyCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            Optional<Cookie> httpOnlyCookie = Arrays.stream(cookies)
                    .filter(cookie -> jwtCookie.equals(cookie.getName()))
                    .findFirst();

            if (httpOnlyCookie.isPresent()) {
                return new ResponseEntity<>("HttpOnly cookie found", HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("HttpOnly cookie not found", HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email){

        try {
            userService.generatePasswordResetToken(email);
            return ResponseEntity.ok(new MessageResponse("Reset token sent!"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new MessageResponse("Error sending reset token"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            userService.resetPassword(token, newPassword);
            return ResponseEntity.ok(new MessageResponse("Password reset successful"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new MessageResponse("Error resetting password!"));
        }
    }
}

