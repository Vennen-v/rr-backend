package com.therogueroad.project.services;

import com.therogueroad.project.dto.CommentDTO;
import com.therogueroad.project.models.Comment;
import com.therogueroad.project.models.Post;
import com.therogueroad.project.models.User;
import com.therogueroad.project.repositories.CommentRepository;
import com.therogueroad.project.repositories.PostRepository;
import com.therogueroad.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CommentDTO createComment(Long postId, CommentDTO commentDTO) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post Not Found"));

        User user = new User("yoshi35", "yoshi@gmail.com", "meepmoop");
        user.setDisplayName("Yoshi Vennen");
        userRepository.save(user);

        Comment comment = modelMapper.map(commentDTO, Comment.class);

        comment.setPost(post);
        comment.setContent(commentDTO.getContent());
        comment.setUser(user);
        comment.setDisplayName(user.getDisplayName());
        comment.setProfilePic(user.getProfilePic());
        comment.setUserName(user.getUserName());
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);

        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    public CommentDTO createCommentReply(Long commentId, CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment Not Found"));

        User user = new User("yoshi35", "yoshi@gmail.com", "meepmoop");
        user.setDisplayName("Yoshi Vennen");
        userRepository.save(user);

        Comment reply = modelMapper.map(commentDTO, Comment.class);

        reply.setContent(commentDTO.getContent());
        reply.setUser(user);
        reply.setDisplayName(user.getDisplayName());
        reply.setProfilePic(user.getProfilePic());
        reply.setUserName(user.getUserName());
        reply.setCreatedAt(LocalDateTime.now());

        comment.addCommentReply(reply);

        commentRepository.save(reply);

        return modelMapper.map(reply, CommentDTO.class);
    }

    @Override
    public List<CommentDTO> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        List<CommentDTO> commentDTOS = comments.stream().map(c -> modelMapper.map(c, CommentDTO.class)).toList();
        return commentDTOS;
    }

    @Override
    public CommentDTO findComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment Not Found"));
        return modelMapper.map(comment, CommentDTO.class);
    }
}
