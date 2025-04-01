package com.therogueroad.project.services;

import com.therogueroad.project.dto.LikeDTO;
import com.therogueroad.project.models.Comment;
import com.therogueroad.project.models.Like;
import com.therogueroad.project.models.Post;
import com.therogueroad.project.models.Userr;
import com.therogueroad.project.repositories.CommentRepository;
import com.therogueroad.project.repositories.LikeRepository;
import com.therogueroad.project.repositories.PostRepository;
import com.therogueroad.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikesServiceImpl implements LikesService{

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public LikeDTO likePost(Long postId, String username) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post Not Found"));

        Userr user = userRepository.findByUserName(username);

        List<Like> userLikes = user.getLikedPosts();

        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        like.setUserName(user.getUserName());
        like.setDisplayName(user.getDisplayName());


        likeRepository.save(like);

        userLikes.add(like);

        return modelMapper.map(like, LikeDTO.class);
    }

    @Override
    public LikeDTO likeComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Post Not Found"));

        Userr user = userRepository.findByUserName(username);

        List<Like> userLikes = user.getLikedPosts();

        Like like = new Like();
        like.setComment(comment);
        like.setUser(user);
        like.setUserName(user.getUserName());
        like.setDisplayName(user.getDisplayName());


        likeRepository.save(like);

        userLikes.add(like);

        return modelMapper.map(like, LikeDTO.class);
    }
}
