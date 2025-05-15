package com.therogueroad.project.services;

import com.therogueroad.project.dto.PostDTO;
import com.therogueroad.project.dto.PostResponse;
import com.therogueroad.project.models.User;

import java.util.List;

public interface PostService {

    PostDTO createPost(PostDTO postDTO, String username);

    PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    PostResponse getPostsByUserId(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    void deletePost(Long postId);

    PostDTO updatePost(PostDTO postDTO, Long postId);

    PostDTO getPost(Long postId);

    PostResponse findByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    PostDTO bookmarkPost(Long postId, String username);

    List<PostDTO> getPostsByCurrentUser(User user);

    List<PostDTO> getBookmarks(String username);

    void deleteOwnPost(Long postId, User user);
}
