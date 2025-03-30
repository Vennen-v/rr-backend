package com.therogueroad.project.dto;

import com.therogueroad.project.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {

    private Long likeId;
    private User user;
    private PostDTO post;
    private CommentDTO comment;
}
