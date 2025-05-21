package com.therogueroad.project.models;

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

    @NotBlank
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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Column(name = "user_posts")
    private List<Post> userPosts = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "saved_posts")
    private List<Post> savedPosts;

    @OneToMany( fetch = FetchType.LAZY)
    @Column(name = "liked_posts")
    private List<Post> likedPosts = new ArrayList<>();

    @OneToMany( fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id" )
    private List<User> following = new ArrayList<>();

    @OneToMany( fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private List<User> followers = new ArrayList<>();

    @Getter
    @Setter
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @CreationTimestamp
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
