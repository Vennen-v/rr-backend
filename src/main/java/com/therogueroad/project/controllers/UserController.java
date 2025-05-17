package com.therogueroad.project.controllers;

import com.therogueroad.project.dto.UserDTO;
import com.therogueroad.project.dto.UserDTOO;
import com.therogueroad.project.dto.UserResponse;
import com.therogueroad.project.models.User;
import com.therogueroad.project.repositories.UserRepository;
import com.therogueroad.project.security.jwt.MessageResponse;
import com.therogueroad.project.security.services.UserDetailsImpl;
import com.therogueroad.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<UserDTOO> getUserByUserName(@PathVariable String username){
        return new ResponseEntity<>(userService.getByUsername(username), HttpStatus.OK);
    }

    @PutMapping("/user/update")
    public ResponseEntity<MessageResponse> updateUserInfo(@RequestBody UserDTO userDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));

        if (userRepository.existsByUserName(userDTO.getUserName()) && !Objects.equals(user.getUserName(), userDTO.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken"));
        }

        if (userRepository.existsByEmail(userDTO.getEmail()) && !Objects.equals(user.getEmail(), userDTO.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use"));
        }



        userService.updateUserInfo(userDTO, user);
        return ResponseEntity.ok(new MessageResponse("User info updated successfully!"));
    }

    @PutMapping("/user/update/pic")
    public ResponseEntity<MessageResponse> updatesProfilePic(@RequestParam MultipartFile file, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));
        userService.updateProfilePic(file, user);

        return ResponseEntity.ok(new MessageResponse("User profile pic updated successfully!"));
    }

    @GetMapping("/users/search")
    public ResponseEntity<UserResponse> getUsersBySearch(@RequestParam(name = "keyword") String keyword,
                                                         @RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                         @RequestParam(name = "pageSize", defaultValue = "20", required = false) Integer pageSize,
                                                         @RequestParam(name = "sortBy", defaultValue = "userId", required = false) String sortBy,
                                                         @RequestParam(name = "sortOrder", defaultValue = "desc", required = false) String sortOrder
                                                        ){
        return new ResponseEntity<>(userService.findByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    @GetMapping("/user/followers")
    public ResponseEntity<List<UserDTOO>> getFollowers(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));
        return new ResponseEntity<>(userService.getFollowers(user), HttpStatus.OK);
    }

    @GetMapping("/user/following")
    public ResponseEntity<List<UserDTOO>> getFollowing(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));
        return new ResponseEntity<>(userService.getFollowing(user), HttpStatus.OK);
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
        return new ResponseEntity<>(userService.getFollowingPosts(user), HttpStatus.OK);
    }


}
