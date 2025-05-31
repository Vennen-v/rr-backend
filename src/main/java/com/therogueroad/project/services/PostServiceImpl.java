package com.therogueroad.project.services;


import com.therogueroad.project.dto.PostDTO;
import com.therogueroad.project.dto.PostResponse;
import com.therogueroad.project.models.Post;
import com.therogueroad.project.models.User;
import com.therogueroad.project.repositories.PostRepository;
import com.therogueroad.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private FileService fileService;


    @Override
    public PostDTO createPost(String title, String content, MultipartFile file, PostDTO postDTO, String username) {
        Post post = modelMapper.map(postDTO, Post.class);

//        User user = new User("yoshi35", "yoshi@gmail.com", "meepmoop");
//        user.setDisplayName("Yoshi Vennen");
//        userRepository.save(user);

        String fileUrl = fileService.saveFileToAWSS3Bucket(file);


        User user = userRepository.findByUserName(username).orElseThrow(() -> new RuntimeException("User Not Found"));


        List<Post> userPost = user.getUserPosts();

        post.setTitle(title);
        post.setUser(user);
        post.setUserName(user.getUserName());
        post.setDisplayName(user.getDisplayName());
        post.setProfilePic(user.getProfilePic());
        post.setContent(content);
        post.setPostImg(fileUrl);
        post.setCreatedAt(LocalDateTime.now());

        postRepository.save(post);

        userPost.add(post);

        return modelMapper.map(post, PostDTO.class);
    }




    @Override
//    public List<PostDTO> getAllPost() {
//        List<Post> posts = postRepository.findAll();
//
//        List<PostDTO> postDTOS = posts.stream().map(p -> modelMapper.map(p, PostDTO.class)).toList();
//
//        return postDTOS;
//    }

    public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Post> postPage = postRepository.findAll(pageDetails);

        List<Post> posts = postPage.getContent();


        List<PostDTO> postDTOS = posts.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .toList();

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDTOS);
        postResponse.setPageNumber(postPage.getNumber());
        postResponse.setPageSize(postPage.getSize());
        postResponse.setTotalElements(postPage.getTotalElements());
        postResponse.setTotalPages(postPage.getTotalPages());
        postResponse.setLastPage(postPage.isLast());

        return postResponse;
    }

    @Override
    public PostResponse getPostsByUserId(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
         User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User Not Found"));


        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Post> postPage = postRepository.findByUser(user, pageDetails);

        List<Post> posts = postPage.getContent();


        List<PostDTO> postDTOS = posts.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .toList();

       PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDTOS);
        postResponse.setPageNumber(postPage.getNumber());
        postResponse.setPageSize(postPage.getSize());
        postResponse.setTotalElements(postPage.getTotalElements());
        postResponse.setTotalPages(postPage.getTotalPages());
        postResponse.setLastPage(postPage.isLast());

        return postResponse;
    }

    @Override
    public void deletePost(Long postId) {
        Post deletedPost = postRepository.findById(postId).orElseThrow(()-> new RuntimeException("Post Not Found"));
       List<User> userList = userRepository.findAll();
       userList.forEach(user -> {
           user.getLikedPosts().remove(postId);
           userRepository.save(user);
       });
        postRepository.delete(deletedPost);
    }

    @Override
    public void deleteOwnPost(Long postId, User user) {
        Post deletedPost = postRepository.findById(postId).orElseThrow(()-> new RuntimeException("Post Not Found"));

        if (deletedPost.getUser().equals(user)){
            List<User> userList = userRepository.findAll();
            userList.forEach(u -> {
                u.getLikedPosts().remove(deletedPost);
                u.getSavedPosts().remove(deletedPost);
                userRepository.save(u);
            });
        postRepository.delete(deletedPost);
        } else {
            throw new RuntimeException("User does not match");
        }
    }

    @Override
    public void removeSave(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post Not Found"));
        List<Post> userSaves = user.getSavedPosts();
        userSaves.remove(post);
        post.setSaves(post.getSaves() == 0 ? 0 : post.getSaves() - 1);
        postRepository.save(post);
        userRepository.save(user);
    }

    @Override
    public Boolean isPostSaved(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post Not Found"));

        return user.getSavedPosts().contains(post);

    }


    @Override
    public PostDTO updatePost(String title, String content, MultipartFile file, Long postId) {
       Post post = postRepository.findById(postId).orElseThrow(()-> new RuntimeException("Post Not Found"));

       String fileUrl;

       if(file == null){
           fileUrl = post.getPostImg();
       } else {
           fileUrl = fileService.saveFileToAWSS3Bucket(file);

       }

       post.setPostId(postId);
       post.setTitle(title);
       post.setContent(content);
       post.setPostImg(fileUrl);
       post.setProfilePic(post.getProfilePic());
//       Like like = new Like();
//       like.setPost(post);

       postRepository.save(post);

       return modelMapper.map(post, PostDTO.class);

    }

    @Override
    public PostDTO getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new RuntimeException("Post Not Found"));
        return modelMapper.map(post, PostDTO.class);

    }

    @Override
    public PostResponse findByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Post> postPage = postRepository.findByTitleContainingIgnoreCase(keyword , pageDetails);

        List<Post> posts = postPage.getContent();
        List<PostDTO> postDTOS = posts.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .toList();


        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDTOS);
        postResponse.setPageNumber(postPage.getNumber());
        postResponse.setPageSize(postPage.getSize());
        postResponse.setTotalElements(postPage.getTotalElements());
        postResponse.setTotalPages(postPage.getTotalPages());
        postResponse.setLastPage(postPage.isLast());
        return postResponse;
    }

    @Override
    public PostDTO bookmarkPost(Long postId, String username) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new RuntimeException("Post Not Found"));

        User user = userRepository.findByUserName(username).orElseThrow(() -> new RuntimeException("User Not Found"));

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
        User user = userRepository.findByUserName(username).orElseThrow(() -> new RuntimeException("User Not Found"));

        List<Post> userSavedPosts = user.getSavedPosts();

        List<PostDTO> postDTOS = userSavedPosts.stream().map(p -> modelMapper.map(p, PostDTO.class)).toList();

        return postDTOS;
    }

    @Override
    public List<PostDTO> getPostsByCurrentUser(User user) {
        List<Post> posts = user.getUserPosts();

        List<PostDTO> postDTOS = posts.stream().map(p -> modelMapper.map(p, PostDTO.class)).toList();

        return postDTOS;
    }


    // Create Get Current Users Post


}
