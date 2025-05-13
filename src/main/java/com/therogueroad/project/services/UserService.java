package com.therogueroad.project.services;

import com.therogueroad.project.dto.UserDTO;
import com.therogueroad.project.models.User;

import java.util.List;

public interface UserService {

    void followUser(Long userId, User userr);

    List<UserDTO> getFollowers(User user);

    List<UserDTO> getFollowing(User user);

    void unfollowUser(Long userId, User user);
}
