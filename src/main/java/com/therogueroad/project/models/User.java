package com.therogueroad.project.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;


    @Size(max = 20)
    private String displayName;

    @NotBlank
    @Column(name = "username")
    @Size(max = 20)
    private String userName;

    @NotBlank
    @Size(max = 50)
    @Email
    @Column(name = "email")
    private String email;

    @Size(min = 6, max = 120)
    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Size(max = 250)
    @Column(name = "bio")
    private String bio;

    @Column(name = "profile_pic")
    private String profilePic = "https://img.freepik.com/premium-photo/icon-define-person-allocate-stylize-250_1137696-4300.jpg?semt=ais_hybrid&w=740";

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "user_posts")
    private List<Post> userPosts = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Column(name = "user_posts")
    private List<Comment> userComments = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_saves",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private List<Post> savedPosts;


   @JsonIgnore
   @ManyToMany(cascade = CascadeType.ALL)
   @JoinTable(name = "user_likes",
           joinColumns = @JoinColumn(name = "user_id"),
           inverseJoinColumns = @JoinColumn(name = "post_id"))
    private List<Post> likedPosts = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "following_id" )
    @JsonIgnore
    private List<User> following = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "follower_id")
    @JsonIgnore
    private List<User> followers = new ArrayList<>();

    @OneToMany( cascade = CascadeType.ALL)
    private List<Notification> recievedNotifs;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Notification> actedNotifs;

    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Conversation> conversationsAsAuthor;

    @JsonIgnore
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Conversation> conversationsAsRecipient;

    private boolean emailVerified = false;

    @Getter
    @Setter
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public User(String userName, String displayName, String email, String password) {
        this.userName = userName;
        this.displayName = displayName;
        this.email = email;
        this.password = password;
    }

    public User(Long userId, String displayName, String userName, String email, String password, String profilePic, List<Post> savedPosts, List<Post> likedPosts, List<User> following, List<User> followers, LocalDateTime createdAt) {
        this.userId = userId;
        this.displayName = displayName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.profilePic = profilePic;
        this.savedPosts = savedPosts;
        this.likedPosts = likedPosts;
        this.following = following;
        this.followers = followers;
        this.createdAt = createdAt;
    }

}
