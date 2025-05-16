package com.therogueroad.project.services;

import com.therogueroad.project.dto.UserDTO;
import com.therogueroad.project.dto.UserDTOO;
import com.therogueroad.project.dto.UserResponse;
import com.therogueroad.project.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    void followUser(Long userId, User userr);

    List<UserDTO> getFollowers(User user);

    List<UserDTO> getFollowing(User user);

    void unfollowUser(Long userId, User user);

    List<UserDTO> getAllUsers();

   UserResponse findByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    List<UserDTOO> getFollowingPosts(User user);

    UserDTOO getByUsername(String username);

    void updateUserInfo(UserDTO userDTO, User user);

    void updateProfilePic(MultipartFile file, User user);
}
