package com.therogueroad.project.services;

import com.therogueroad.project.dto.PostDTO;
import com.therogueroad.project.models.Post;
import com.therogueroad.project.models.User;
import com.therogueroad.project.repositories.CommentRepository;

import com.therogueroad.project.repositories.PostRepository;
import com.therogueroad.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LikesServiceImpl implements LikesService{



    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public void likePost(Long postId, String username) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post Not Found"));
//
        User user = userRepository.findByUserName(username).orElseThrow(() -> new RuntimeException("User Not Found"));


        post.setLikes(post.getLikes() + 1);

        postRepository.save(post);

        if (user.getLikedPosts() == null || user.getLikedPosts().isEmpty()) {
            List<Post> newLikeList = new ArrayList<>();
            user.setLikedPosts(newLikeList);
            newLikeList.add(post);
        } else {
            user.getLikedPosts().add(post);
        }

        userRepository.save(user);}




    @Override
    public List<PostDTO> getCurrentUserLikes(User user) {
       List<Post> userLikes = user.getLikedPosts();
       List<PostDTO> userPostDTO = userLikes.stream().map(l -> modelMapper.map(l, PostDTO.class)).toList();
       return userPostDTO;
    }

    @Override
    public void removeLike(Long postId, User user) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post Not Found"));
        List<Post> userLikes = user.getLikedPosts();
        userLikes.remove(post);
        post.setLikes(post.getLikes() - 1);
        postRepository.save(post);
        userRepository.save(user);



    }

    @Override
    public Boolean isPostLiked(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post Not Found"));

           return user.getLikedPosts().contains(post);

    }
}
