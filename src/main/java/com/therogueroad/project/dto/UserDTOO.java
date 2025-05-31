package com.therogueroad.project.dto;



import com.therogueroad.project.models.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.internal.bytebuddy.asm.Advice;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserDTOO {

    private Long id;
    private String userName;
    private String displayName;
    private String email;
    private String bio;
    private String profilePic;
    private List<PostNoUserDTO> userPosts;
    private List<UserDTO> followers;
    private boolean emailVerified;
    private LocalDateTime createdAt;


}
