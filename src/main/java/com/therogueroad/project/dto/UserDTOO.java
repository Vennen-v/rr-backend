package com.therogueroad.project.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserDTOO {

    private Long id;
    private String userName;
    private String displayName;
    private String email;
    private List<PostNoUserDTO> userPosts;
    private String profilePic;
}
