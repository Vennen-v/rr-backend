package com.therogueroad.project.dto;

import com.therogueroad.project.models.Userr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {

    private Long likeId;
    private Userr user;
    private PostDTO post;
    private CommentDTO comment;
}
