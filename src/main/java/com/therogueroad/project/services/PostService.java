package com.therogueroad.project.services;

import com.therogueroad.project.dto.PostDTO;

import java.util.List;

public interface PostService {

    PostDTO createPost(PostDTO postDTO, String username);

    List<PostDTO> getAllPost();

    List<PostDTO> getPostsByUserId(Long userId);

    void deletePost(Long postId);

    PostDTO updatePost(PostDTO postDTO, Long postId);

    PostDTO getPost(Long postId);

    PostDTO findByKeyword(String keyword);

    void bookmarkPost(Long postId, String username);
}
