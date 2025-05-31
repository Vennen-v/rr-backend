package com.therogueroad.project.services;

import com.therogueroad.project.dto.UserDTO;
import com.therogueroad.project.dto.UserDTOO;
import com.therogueroad.project.dto.UserResponse;
import com.therogueroad.project.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    void followUser(Long userId, User userr);

    List<UserDTOO> getFollowers(User user);

    List<UserDTOO> getFollowing(User user);

    void unfollowUser(Long userId, User user);

    List<UserDTO> getAllUsers();

   UserResponse findByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    List<UserDTOO> getFollowingPosts(User user);

    UserDTOO getByUsername(String username);

    void updateUserInfo(UserDTO userDTO, User user);

    void updateProfilePic(MultipartFile file, User user);

    Boolean isFollowingUser(String username, User user);

    void generatePasswordResetToken(String email);

    void generateEmailVerificationToken(String email);

    void resetPassword(String token, String newPassword);

    void verifyEmail(String token);

    void deleteOwnAccount(User user);
}
