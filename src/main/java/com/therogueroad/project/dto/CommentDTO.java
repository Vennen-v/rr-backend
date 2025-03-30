package com.therogueroad.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long commentId;
    private String content;
    private String userName;
    private String displayName;
    private String profilePic;
    private List<CommentDTO> replies;
    private List<LikeDTO> likes;
    private LocalDateTime createdAt;


}
