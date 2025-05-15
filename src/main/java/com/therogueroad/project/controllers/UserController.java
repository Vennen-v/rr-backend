package com.therogueroad.project.controllers;

import com.therogueroad.project.dto.UserDTO;
import com.therogueroad.project.dto.UserDTOO;
import com.therogueroad.project.dto.UserResponse;
import com.therogueroad.project.models.User;
import com.therogueroad.project.repositories.UserRepository;
import com.therogueroad.project.security.services.UserDetailsImpl;
import com.therogueroad.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.FOUND);
    }

    @GetMapping("/users/search")
    public ResponseEntity<UserResponse> getUsersBySearch(@RequestParam(name = "keyword") String keyword,
                                                         @RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                         @RequestParam(name = "pageSize", defaultValue = "20", required = false) Integer pageSize,
                                                         @RequestParam(name = "sortBy", defaultValue = "userId", required = false) String sortBy,
                                                         @RequestParam(name = "sortOrder", defaultValue = "desc", required = false) String sortOrder
                                                        ){
        return new ResponseEntity<>(userService.findByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.FOUND);
    }

    @GetMapping("/user/followers")
    public ResponseEntity<List<UserDTO>> getFollowers(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));
        return new ResponseEntity<>(userService.getFollowers(user), HttpStatus.FOUND);
    }

    @GetMapping("/user/following")
    public ResponseEntity<List<UserDTO>> getFollowing(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));
        return new ResponseEntity<>(userService.getFollowing(user), HttpStatus.FOUND);
    }

    @PostMapping("/user/follow/{userId}")
    public ResponseEntity<String> followUser(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));
        userService.followUser(userId, user);
        return new ResponseEntity<>("User followed successfully", HttpStatus.OK);
    }

    @PostMapping("/user/unfollow/{userId}")
    public ResponseEntity<String> unfollowUser(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));
        userService.unfollowUser(userId, user);
        return new ResponseEntity<>("User successfully unfollowed", HttpStatus.OK);
    }

    @GetMapping("/user/following/posts")
    public ResponseEntity<List<UserDTOO>> getFollowersPosts(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));
        return new ResponseEntity<>(userService.getFollowingPosts(user), HttpStatus.FOUND);
    }


}
