package com.therogueroad.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {

    private Long likeId;
    private Long postId;
    private Long commentId;
    private String userName;
    private String displayName;
}
