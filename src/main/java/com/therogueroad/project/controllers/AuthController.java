package com.therogueroad.project.controllers;

import com.therogueroad.project.dto.UserDTO;
import com.therogueroad.project.models.Userr;
import com.therogueroad.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/user")
    public ResponseEntity<UserDTO> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        Userr user = userRepository.findByUserName(userDetails.getUsername());

        return new ResponseEntity<>(modelMapper.map(user, UserDTO.class), HttpStatus.OK);
    }
}
