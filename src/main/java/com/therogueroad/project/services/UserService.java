package com.therogueroad.project.services;

import com.therogueroad.project.dto.UserDTO;
import com.therogueroad.project.models.Userr;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {

    void followUser(Long userId, Userr userr);

    List<UserDTO> getFollowers(Userr user);

    List<UserDTO> getFollowing(Userr user);

    void unfollowUser(Long userId, Userr user);
}
