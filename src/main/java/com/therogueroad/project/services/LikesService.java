package com.therogueroad.project.services;

import com.therogueroad.project.dto.LikeDTO;
import com.therogueroad.project.models.User;

import java.util.List;


public interface LikesService {
   LikeDTO likePost(Long postId, String username);
   LikeDTO likeComment(Long commentId, String username);
   List<LikeDTO> getCurrentUserLikes(User user);
   void removeLike(Long likeId, User user);
}
