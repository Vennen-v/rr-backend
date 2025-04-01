package com.therogueroad.project.services;

import com.therogueroad.project.dto.LikeDTO;


public interface LikesService {
   LikeDTO likePost(Long postId, String username);
   LikeDTO likeComment(Long commentId, String username);
}
