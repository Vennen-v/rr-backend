package com.therogueroad.project.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@Entity
@Table(name = "userss", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class Userr {

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
    private List<Userr> following = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "follower_id")
    private List<Userr> followers = new ArrayList<>();

    public Userr(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public Userr(Long userId, String displayName, String userName, String email, String password, String profilePic, List<Post> savedPosts, List<Like> likedPosts, List<Userr> following, List<Userr> followers) {
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
