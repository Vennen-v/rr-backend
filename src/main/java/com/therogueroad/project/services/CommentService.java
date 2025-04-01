package com.therogueroad.project.services;

import com.therogueroad.project.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(Long postId, CommentDTO commentDTO, String username);

    CommentDTO createCommentReply(Long commentId, CommentDTO commentDTO, String username);

    List<CommentDTO> getAllComments();

    CommentDTO findComment(Long commentId);
}
