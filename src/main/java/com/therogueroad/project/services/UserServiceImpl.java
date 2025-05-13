package com.therogueroad.project.services;

import com.therogueroad.project.dto.UserDTO;
import com.therogueroad.project.models.User;
import com.therogueroad.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

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

}
