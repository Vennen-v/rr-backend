package com.therogueroad.project.controllers;

import com.therogueroad.project.dto.LikeDTO;
import com.therogueroad.project.models.Userr;
import com.therogueroad.project.repositories.UserRepository;
import com.therogueroad.project.services.LikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<String> likePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        likesService.likePost(postId, username);
        return new ResponseEntity<>("Post has been liked", HttpStatus.OK);
    }

    @PostMapping("/comments/like/{commentId}")
    public ResponseEntity<String> likeComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        likesService.likeComment(commentId, username);
        return new ResponseEntity<>("Comment has been liked", HttpStatus.OK);
    }

    @GetMapping("/user/likes")
    public ResponseEntity<List<LikeDTO>> getUserLikes(@AuthenticationPrincipal UserDetails userDetails) {
        Userr user = userRepository.findByUserName(userDetails.getUsername());
        return new ResponseEntity<>(likesService.getCurrentUserLikes(user), HttpStatus.FOUND);
    }

    @DeleteMapping("/delete/like/{likeId}")
    public ResponseEntity<String> removeOwnLike(@PathVariable Long likeId, @AuthenticationPrincipal UserDetails userDetails){
        Userr user = userRepository.findByUserName(userDetails.getUsername());
        likesService.removeLike(likeId, user);
        return new ResponseEntity<>("Like Removed Successfully", HttpStatus.OK);
    }
}
