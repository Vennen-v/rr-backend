package com.therogueroad.project.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String displayName;

    @Column(name = "username")
    private String userName;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "profile_pic")
    private String profilePic = "default.png";

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Column(name = "user_posts")
    private List<Post> userPosts = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "saved_posts")
    private List<Post> savedPosts;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Column(name = "liked_posts")
    private List<Like> likedPosts = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "following_id")
    private List<User> following = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "follower_id")
    private List<User> followers = new ArrayList<>();

    @Getter
    @Setter
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public User(Long userId, String displayName, String userName, String email, String password, String profilePic, List<Post> savedPosts, List<Like> likedPosts, List<User> following, List<User> followers) {
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
    }

}
