package com.therogueroad.project.controllers;

import com.therogueroad.project.services.LikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LikesController {

    @Autowired
    private LikesService likesService;

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
}
