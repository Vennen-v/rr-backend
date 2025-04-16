package com.therogueroad.project.services;

import com.therogueroad.project.dto.PostDTO;
import com.therogueroad.project.models.Like;
import com.therogueroad.project.models.Post;
import com.therogueroad.project.models.Userr;
import com.therogueroad.project.repositories.PostRepository;
import com.therogueroad.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService{

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PostDTO createPost(PostDTO postDTO, String username) {
        Post post = modelMapper.map(postDTO, Post.class);

//        User user = new User("yoshi35", "yoshi@gmail.com", "meepmoop");
//        user.setDisplayName("Yoshi Vennen");
//        userRepository.save(user);

        Userr user = userRepository.findByUserName(username);


        List<Post> userPost = user.getUserPosts();

        post.setTitle(postDTO.getTitle());
        post.setUser(user);
        post.setUserName(user.getUserName());
        post.setDisplayName(user.getDisplayName());
        post.setProfilePic(user.getProfilePic());
        post.setContent(postDTO.getContent());
        post.setCreatedAt(LocalDateTime.now());

        postRepository.save(post);

        userPost.add(post);

        return modelMapper.map(post, PostDTO.class);
    }

    @Override
    public List<PostDTO> getAllPost() {
        List<Post> posts = postRepository.findAll();

        List<PostDTO> postDTOS = posts.stream().map(p -> modelMapper.map(p, PostDTO.class)).toList();

        return postDTOS;
    }

    @Override
    public List<PostDTO> getPostsByUserId(Long userId) {
         Userr user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User Not Found"));

         List<Post> posts = user.getUserPosts();

        List<PostDTO> postDTOS = posts.stream().map(p -> modelMapper.map(p, PostDTO.class)).toList();

        return postDTOS;
    }

    @Override
    public void deletePost(Long postId) {
        Post deletedPost = postRepository.findById(postId).orElseThrow(()-> new RuntimeException("Post Not Found"));
        postRepository.delete(deletedPost);
    }

    @Override
    public void deleteOwnPost(Long postId, Userr user) {
        Post deletedPost = postRepository.findById(postId).orElseThrow(()-> new RuntimeException("Post Not Found"));

        if (deletedPost.getUser().equals(user)){
        postRepository.delete(deletedPost);
        } else {
            throw new RuntimeException("User does not match");
        }
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, Long postId) {
       Post post = postRepository.findById(postId).orElseThrow(()-> new RuntimeException("Post Not Found"));

       post.setPostId(postId);
       post.setTitle(postDTO.getTitle());
       post.setContent(postDTO.getContent());
       Like like = new Like();
       like.setPost(post);

       postRepository.save(post);

       return modelMapper.map(post, PostDTO.class);

    }

    @Override
    public PostDTO getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new RuntimeException("Post Not Found"));
        return modelMapper.map(post, PostDTO.class);

    }

    @Override
    public List<PostDTO> findByKeyword(String keyword) {
        List<Post> foundPosts = postRepository.findByTitleContainingIgnoreCase(keyword);
        List<PostDTO> postDTOS = foundPosts.stream().map(p -> modelMapper.map(p, PostDTO.class)).toList();
        return postDTOS;
    }

    @Override
    public PostDTO bookmarkPost(Long postId, String username) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new RuntimeException("Post Not Found"));

        Userr user = userRepository.findByUserName(username);

        System.out.println(username);


        post.setSaves(post.getSaves() + 1);

        postRepository.save(post);

        if (user.getSavedPosts() == null || user.getSavedPosts().isEmpty()) {
            List<Post> newSavedList = new ArrayList<>();
            user.setSavedPosts(newSavedList);
            newSavedList.add(post);
        } else {
            user.getSavedPosts().add(post);
        }

        userRepository.save(user);

        return modelMapper.map(post, PostDTO.class);
    }

    @Override
    public List<PostDTO> getBookmarks(String username) {
        Userr user = userRepository.findByUserName(username);

        List<Post> userSavedPosts = user.getSavedPosts();

        List<PostDTO> postDTOS = userSavedPosts.stream().map(p -> modelMapper.map(p, PostDTO.class)).toList();

        return postDTOS;
    }

    @Override
    public List<PostDTO> getPostsByCurrentUser(Userr user) {
        List<Post> posts = user.getUserPosts();

        List<PostDTO> postDTOS = posts.stream().map(p -> modelMapper.map(p, PostDTO.class)).toList();

        return postDTOS;
    }


    // Create Get Current Users Post


}
