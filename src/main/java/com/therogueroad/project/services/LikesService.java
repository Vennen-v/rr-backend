package com.therogueroad.project.services;

import com.therogueroad.project.dto.PostDTO;
import com.therogueroad.project.models.User;

import java.util.List;


public interface LikesService {
   void likePost(Long postId, String username);
//   CommentDTO likeComment(Long commentId, String username);
   List<PostDTO> getCurrentUserLikes(User user);
   void removeLike(Long postId, User user);

    Boolean isPostLiked(Long postId, User user);
}
