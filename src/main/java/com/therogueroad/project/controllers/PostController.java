package com.therogueroad.project.controllers;

import com.therogueroad.project.dto.PostDTO;
import com.therogueroad.project.dto.PostResponse;
import com.therogueroad.project.models.User;
import com.therogueroad.project.repositories.UserRepository;
import com.therogueroad.project.security.services.UserDetailsImpl;
import com.therogueroad.project.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/posts")
    public ResponseEntity<PostDTO> createPost(@RequestPart("title") String title, @RequestPart("content") String content,  @RequestPart("file") MultipartFile file, PostDTO postDTO, @AuthenticationPrincipal UserDetailsImpl userDetails){
        String username = userDetails.getUsername();
        return new ResponseEntity<>(postService.createPost(title, content, file, postDTO, username), HttpStatus.CREATED);
    }

//    @GetMapping("/posts")
//    public ResponseEntity<List<PostDTO>> getAllPost(){
//        return new ResponseEntity<>(postService.getAllPost(), HttpStatus.FOUND);
//    }

    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getAllPost(
            @RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "12", required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "postId", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "desc", required = false) String sortOrder
    ){
        return new ResponseEntity<>(postService.getAllPost(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId){
        return new ResponseEntity<>(postService.getPost(postId), HttpStatus.OK);
    }


    @GetMapping("/posts/user/{userId}")
    public ResponseEntity<PostResponse> getPostsByUserId(@PathVariable Long userId,
                                                          @RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                          @RequestParam(name = "pageSize", defaultValue = "12", required = false) Integer pageSize,
                                                          @RequestParam(name = "sortBy", defaultValue = "postId", required = false) String sortBy,
                                                          @RequestParam(name = "sortOrder", defaultValue = "desc", required = false) String sortOrder
                                                          ){
        return new ResponseEntity<>(postService.getPostsByUserId(userId, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    @GetMapping("/posts/search")
    public ResponseEntity<PostResponse> getPostBySearch(@RequestParam(name = "keyword") String keyword,
                                                        @RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                        @RequestParam(name = "pageSize", defaultValue = "12", required = false) Integer pageSize,
                                                        @RequestParam(name = "sortBy", defaultValue = "postId", required = false) String sortBy,
                                                        @RequestParam(name = "sortOrder", defaultValue = "desc", required = false) String sortOrder){
        return new ResponseEntity<>(postService.findByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
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
    public ResponseEntity<PostDTO> updatePost(@RequestPart(value = "title", required = false) String title, @RequestPart(value = "content", required = false) String content,  @RequestPart(value = "file", required = false) MultipartFile file,  @PathVariable Long postId){
        return new ResponseEntity<>(postService.updatePost(title, content, file, postId), HttpStatus.OK);
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
        return new ResponseEntity<>(postService.getPostsByCurrentUser(user), HttpStatus.OK);
    }

    @GetMapping("/user/bookmarks")
    public ResponseEntity<List<PostDTO>> getUserSavedPosts(@AuthenticationPrincipal UserDetailsImpl userDetails){
        String username = userDetails.getUsername();
        return new ResponseEntity<>(postService.getBookmarks(username), HttpStatus.OK);
    }

}
