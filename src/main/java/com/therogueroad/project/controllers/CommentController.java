package com.therogueroad.project.controllers;

import com.therogueroad.project.dto.CommentDTO;
import com.therogueroad.project.models.Userr;
import com.therogueroad.project.repositories.UserRepository;
import com.therogueroad.project.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/comments/posts/{postId}")
    public ResponseEntity<CommentDTO> createComment(@PathVariable Long postId, @RequestBody CommentDTO commentDTO, @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        return new ResponseEntity<>(commentService.createComment(postId, commentDTO, username), HttpStatus.CREATED);
    }

    @PostMapping("/comments/reply/{commentId}")
    public ResponseEntity<CommentDTO> createCommentReply(@PathVariable Long commentId, @RequestBody CommentDTO commentDTO, @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        return new ResponseEntity<>(commentService.createCommentReply(commentId, commentDTO, username), HttpStatus.CREATED);
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentDTO>> getAllComments(){
        return new ResponseEntity<>(commentService.getAllComments(), HttpStatus.FOUND);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentDTO> findCommentById(@PathVariable Long commentId){
        return new ResponseEntity<>(commentService.findComment(commentId), HttpStatus.FOUND);
    }

    @DeleteMapping("/comments/delete/user/{commentId}")
    public ResponseEntity<String> deleteOwnComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails){
        Userr user = userRepository.findByUserName(userDetails.getUsername());
        commentService.deleteOwnComment(commentId, user);
        return new ResponseEntity<>("Comment Successfully Deleted", HttpStatus.OK);
    }

    @DeleteMapping("/comments/delete/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return new ResponseEntity<>("Comment Successfully Deleted", HttpStatus.OK);
    }
}
