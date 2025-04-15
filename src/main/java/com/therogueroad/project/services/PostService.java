package com.therogueroad.project.services;

import com.therogueroad.project.dto.PostDTO;
import com.therogueroad.project.models.Userr;

import java.util.List;

public interface PostService {

    PostDTO createPost(PostDTO postDTO, String username);

    List<PostDTO> getAllPost();

    List<PostDTO> getPostsByUserId(Long userId);

    void deletePost(Long postId);

    PostDTO updatePost(PostDTO postDTO, Long postId);

    PostDTO getPost(Long postId);

    PostDTO findByKeyword(String keyword);

    PostDTO bookmarkPost(Long postId, String username);

    List<PostDTO> getPostsByCurrentUser(Userr user);

    List<PostDTO> getBookmarks(String username);
}
