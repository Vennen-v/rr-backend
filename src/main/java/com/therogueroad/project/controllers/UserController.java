package com.therogueroad.project.controllers;

import com.therogueroad.project.dto.PostDTO;
import com.therogueroad.project.dto.UserDTO;
import com.therogueroad.project.models.Post;
import com.therogueroad.project.models.Userr;
import com.therogueroad.project.repositories.UserRepository;
import com.therogueroad.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/followers")
    public ResponseEntity<List<UserDTO>> getFollowers(@AuthenticationPrincipal UserDetails userDetails){
        Userr user = userRepository.findByUserName(userDetails.getUsername());
        return new ResponseEntity<>(userService.getFollowers(user), HttpStatus.FOUND);
    }

    @GetMapping("/user/following")
    public ResponseEntity<List<UserDTO>> getFollowing(@AuthenticationPrincipal UserDetails userDetails){
        Userr user = userRepository.findByUserName(userDetails.getUsername());
        return new ResponseEntity<>(userService.getFollowing(user), HttpStatus.FOUND);
    }

    @PostMapping("/user/follow/{userId}")
    public ResponseEntity<String> followUser(@PathVariable Long userId, @AuthenticationPrincipal UserDetails userDetails){
        Userr user = userRepository.findByUserName(userDetails.getUsername());
        userService.followUser(userId, user);
        return new ResponseEntity<>("User followed successfully", HttpStatus.OK);
    }

    @PostMapping("/user/unfollow/{userId}")
    public ResponseEntity<String> unfollowUser(@PathVariable Long userId, @AuthenticationPrincipal UserDetails userDetails){
        Userr user = userRepository.findByUserName(userDetails.getUsername());
        userService.unfollowUser(userId, user);
        return new ResponseEntity<>("User successfully unfollowed", HttpStatus.OK);
    }
}
