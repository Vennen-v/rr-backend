package com.therogueroad.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostNoUserDTO {
    private Long postId;
    private String title;
    private String content;
    private String displayName;
    private String profilePic;
    private String postImg;
    private Long likes = 0L;
    private List<CommentDTO> comments = new ArrayList<>();
    private Long saves = 0L;
    private LocalDateTime createdAt;
}
