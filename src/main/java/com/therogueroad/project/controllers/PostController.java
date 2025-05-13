package com.therogueroad.project.controllers;

import com.therogueroad.project.dto.PostDTO;
import com.therogueroad.project.models.User;
import com.therogueroad.project.repositories.UserRepository;
import com.therogueroad.project.security.services.UserDetailsImpl;
import com.therogueroad.project.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/posts")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO, @AuthenticationPrincipal UserDetailsImpl userDetails){
        String username = userDetails.getUsername();
        return new ResponseEntity<>(postService.createPost(postDTO, username), HttpStatus.CREATED);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> getAllPost(){
        return new ResponseEntity<>(postService.getAllPost(), HttpStatus.FOUND);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId){
        return new ResponseEntity<>(postService.getPost(postId), HttpStatus.FOUND);
    }


    @GetMapping("/posts/user/{userId}")
    public ResponseEntity<List<PostDTO>> getPostsByUserId(@PathVariable Long userId){
        return new ResponseEntity<>(postService.getPostsByUserId(userId), HttpStatus.FOUND);
    }

    @GetMapping("/posts/search")
    public ResponseEntity<List<PostDTO>> getPostBySearch(@RequestParam(name = "keyword") String keyword){
        return new ResponseEntity<>(postService.findByKeyword(keyword), HttpStatus.FOUND);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return new ResponseEntity<>("Post Successfully Deleted", HttpStatus.OK);
    }

    @DeleteMapping("/posts/user/current/{postId}")
    public ResponseEntity<String> deleteOwnPost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));
        postService.deleteOwnPost(postId, user);
        return new ResponseEntity<>("Post Successfully Deleted", HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO, @PathVariable Long postId){
        return new ResponseEntity<>(postService.updatePost(postDTO, postId), HttpStatus.OK);
    }

    @PutMapping("/posts/save/{postId}")
    public ResponseEntity<PostDTO> savePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        String username = userDetails.getUsername();
        return new ResponseEntity<>(postService.bookmarkPost(postId, username), HttpStatus.OK);
    }

    //Create Get Currents User's Post
    @GetMapping("/posts/user/current")
    public ResponseEntity<List<PostDTO>> getPostsByCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));
        return new ResponseEntity<>(postService.getPostsByCurrentUser(user), HttpStatus.FOUND);
    }

    @GetMapping("/user/bookmarks")
    public ResponseEntity<List<PostDTO>> getUserSavedPosts(@AuthenticationPrincipal UserDetailsImpl userDetails){
        String username = userDetails.getUsername();
        return new ResponseEntity<>(postService.getBookmarks(username), HttpStatus.FOUND);
    }

}
