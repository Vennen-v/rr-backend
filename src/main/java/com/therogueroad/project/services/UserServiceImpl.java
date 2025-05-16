package com.therogueroad.project.services;

import com.therogueroad.project.dto.UserDTO;
import com.therogueroad.project.dto.UserDTOO;
import com.therogueroad.project.dto.UserResponse;
import com.therogueroad.project.models.User;
import com.therogueroad.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Override
    public List<UserDTO> getFollowing(User user) {
        List<User> userFollowing = user.getFollowing();

        return userFollowing.stream().map(uf -> modelMapper.map(uf, UserDTO.class)).toList();
    }


    @Override
    public List<UserDTO> getFollowers(User user) {
        List<User> userFollowers = user.getFollowers();

        return userFollowers.stream().map(uf -> modelMapper.map(uf, UserDTO.class)).toList();
    }


    @Override
    public List<UserDTOO> getFollowingPosts(User user) {
        List<User> userFollowing = user.getFollowing();

        return userFollowing.stream().map(uf -> modelMapper.map(uf, UserDTOO.class)).toList();
    }

    @Override
    public UserDTOO getByUsername(String username) {
        User user = userRepository.findByUserName(username).orElseThrow(() -> new RuntimeException("User Not Found"));

        return modelMapper.map(user, UserDTOO.class);
    }

    @Override
    public void updateUserInfo(UserDTO userDTO, User user) {


        user.setDisplayName(userDTO.getDisplayName());
        user.setUserName(userDTO.getUserName());
        user.setEmail(userDTO.getEmail());
        user.setBio(userDTO.getBio());

        userRepository.save(user);

      modelMapper.map(user, UserDTO.class);

    }

    @Override
    public void updateProfilePic(MultipartFile file, User user) {

        String fileUrl = fileService.saveFileToAWSS3Bucket(file);
        user.setProfilePic(fileUrl);

        userRepository.save(user);
    }

    @Override
    public void followUser(Long userId, User user) {
        User toFollow = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));

        if(!toFollow.equals(user)) {
            user.getFollowing().add(toFollow);
            toFollow.getFollowers().add(user);

            userRepository.save(user);
            userRepository.save(toFollow);
        } else {
           throw new RuntimeException("You cant follow yourself");
        }

    }

    @Override
    public void unfollowUser(Long userId, User user) {
        User toUnfollow = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));


        user.getFollowing().remove(toUnfollow);
        toUnfollow.getFollowers().remove(user);

            userRepository.save(user);
            userRepository.save(toUnfollow);



    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserDTO> userDTOS = users.stream().map(p -> modelMapper.map(p, UserDTO.class)).toList();

        return userDTOS;
    }

    @Override
    public UserResponse findByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
//        List<User> foundUsers = userRepository.findByUserNameContainingIgnoreCase(keyword);

//        List<User> foundUsers = userRepository.findByUserNameOrDisplayNameContainingIgnoreCase(keyword, keyword);
//        List<UserDTO> userDTOS = foundUsers.stream().map(u -> modelMapper.map(u, UserDTO.class)).toList();
//
//        List<User> foundUsersDisplay = userRepository.findByDisplayNameContainingIgnoreCase(keyword);
//        List<UserDTO> userDisplayDTOS = foundUsersDisplay.stream().map(p -> modelMapper.map(p, UserDTO.class)).toList();
//
//        List<UserDTO>allSearchUsers = new ArrayList<>();
//        allSearchUsers.addAll(userDTOS);
//        allSearchUsers.addAll(userDisplayDTOS);

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<User> userPage = userRepository.findByUserNameOrDisplayNameContainingIgnoreCase(keyword, keyword, pageDetails);

        List<User> users = userPage.getContent();
        List<UserDTO> userDTOS = users.stream()
                .map(u -> modelMapper.map(u, UserDTO.class))
                .toList();

        UserResponse userResponse = new UserResponse();
        userResponse.setContent(userDTOS);
        userResponse.setPageNumber(userPage.getNumber());
        userResponse.setPageSize(userPage.getSize());
        userResponse.setTotalElements(userPage.getTotalElements());
        userResponse.setTotalPages(userPage.getTotalPages());
        userResponse.setLastPage(userPage.isLast());

return userResponse;
    }



}
