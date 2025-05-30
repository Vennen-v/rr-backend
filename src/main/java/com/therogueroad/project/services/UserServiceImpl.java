package com.therogueroad.project.services;

import com.therogueroad.project.dto.UserDTO;
import com.therogueroad.project.dto.UserDTOO;
import com.therogueroad.project.dto.UserResponse;
import com.therogueroad.project.models.EmailVerificationToken;
import com.therogueroad.project.models.PasswordResetToken;
import com.therogueroad.project.models.User;
import com.therogueroad.project.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${frontend.url}")
    String frontendURL;

    @Override
    public List<UserDTOO> getFollowing(User user) {
        List<User> userFollowing = user.getFollowing();


        return userFollowing.stream().map(uf -> modelMapper.map(uf, UserDTOO.class)).toList();
    }


    @Override
    public List<UserDTOO> getFollowers(User user) {
        List<User> userFollowers = user.getFollowers();

        return userFollowers.stream().map(uf -> modelMapper.map(uf, UserDTOO.class)).toList();
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
        user.getUserPosts().forEach(post -> {post.setDisplayName(userDTO.getDisplayName()); postRepository.save(post);});
        user.getUserComments().forEach(comment -> {comment.setDisplayName(userDTO.getDisplayName()); commentRepository.save(comment);});

        user.setUserName(userDTO.getUserName());
        user.getUserPosts().forEach(post -> {post.setUserName(userDTO.getUserName()); postRepository.save(post);});
        user.getUserComments().forEach(comment -> {comment.setUserName(userDTO.getUserName()); commentRepository.save(comment);});


        user.setEmail(userDTO.getEmail());

        user.setBio(userDTO.getBio());

        userRepository.save(user);

      modelMapper.map(user, UserDTO.class);

    }

    @Override
    public void updateProfilePic(MultipartFile file, User user) {

        String fileUrl = fileService.saveFileToAWSS3Bucket(file);
        user.setProfilePic(fileUrl);
        user.getUserPosts().forEach(post -> {post.setProfilePic(fileUrl); postRepository.save(post);});
        user.getUserComments().forEach(comment -> {comment.setProfilePic(fileUrl); commentRepository.save(comment);});


        userRepository.save(user);
    }

    @Override
    public Boolean isFollowingUser(String username, User user) {
        User checkedUser = userRepository.findByUserName(username).orElseThrow(() -> new RuntimeException("User Not Found"));

        return user.getFollowing().contains(checkedUser);
    }

    @Override
    public void followUser(Long userId, User user) {
        User toFollow = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));

        if(!toFollow.equals(user)) {
            user.getFollowing().add(toFollow);
            toFollow.getFollowers().add(user);

            userRepository.save(user);
            userRepository.save(toFollow);

            notificationService.sendFollowNotif(user.getUserName(), toFollow.getUserId());

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

    @Override
    public void generatePasswordResetToken(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not Found"));

        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plus(24, ChronoUnit.HOURS);

        PasswordResetToken resetToken = new PasswordResetToken(token, expiryDate, user);
        passwordResetTokenRepository.save(resetToken);

        String resetUrl = frontendURL + "reset-password?token=" + token;

        emailService.sendPasswordResetEmail(user.getEmail(), resetUrl);

    }

    @Override
    public void generateEmailVerificationToken(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not Found"));

        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plus(3, ChronoUnit.DAYS);

        EmailVerificationToken verificationToken = new EmailVerificationToken(token, expiryDate, user);
        emailVerificationTokenRepository.save(verificationToken);

        String verificationUrl = frontendURL + "email-verification?link=" + token;

        emailService.sendEmailVerificationLink(user.getEmail(), verificationUrl);

    }

    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(()-> new RuntimeException("Reset token is invalid"));

        if(resetToken.isUsed()){
            throw new RuntimeException("Password reset token has already been used");
        }

        if(resetToken.getExpiryDate().isBefore(Instant.now())){
            throw new RuntimeException("Password reset token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }

    @Override
    public void verifyEmail(String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token).orElseThrow(()-> new RuntimeException("Token is invalid"));

        if(verificationToken.isUsed()){
            throw new RuntimeException("Verification token has already been used");
        }

        if(verificationToken.getExpiryDate().isBefore(Instant.now())){
            throw new RuntimeException("Email Verification token has expired");
        }

        if (token != null) {
        User user = verificationToken.getUser();
            user.setEmailVerified(true);
            userRepository.save(user);
            verificationToken.setUsed(true);
            emailVerificationTokenRepository.save(verificationToken);
        } else {
            throw new RuntimeException("The Verification Token does not exist");
        }





    }


}
