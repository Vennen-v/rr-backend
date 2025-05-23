package com.therogueroad.project.controllers;

import com.therogueroad.project.dto.LikeDTO;
import com.therogueroad.project.dto.PostDTO;
import com.therogueroad.project.models.User;
import com.therogueroad.project.repositories.UserRepository;
import com.therogueroad.project.security.services.UserDetailsImpl;
import com.therogueroad.project.services.LikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LikesController {

    @Autowired
    private LikesService likesService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/posts/like/{postId}")
    public ResponseEntity<String> likePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        String username = userDetails.getUsername();
        likesService.likePost(postId, username);
        return new ResponseEntity<>("Post has been liked", HttpStatus.OK);
    }

//    @PostMapping("/comments/like/{commentId}")
//    public ResponseEntity<String> likeComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        String username = userDetails.getUsername();
//        likesService.likeComment(commentId, username);
//        return new ResponseEntity<>("Comment has been liked", HttpStatus.OK);
//    }

    @GetMapping("/user/likes")
    public ResponseEntity<List<PostDTO>> getUserLikes(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));
        return new ResponseEntity<>(likesService.getCurrentUserLikes(user), HttpStatus.OK);
    }

    @DeleteMapping("/delete/like/{postId}")
    public ResponseEntity<String> removeOwnLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));
        likesService.removeLike(postId, user);
        return new ResponseEntity<>("Like Removed Successfully", HttpStatus.OK);
    }

    @GetMapping("/isLiked/{postId}")
    public ResponseEntity<Boolean> isPostLiked(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));

        return new ResponseEntity<>(likesService.isPostLiked(postId, user), HttpStatus.OK);
    }
}
