package com.therogueroad.project.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String userName;

    private String displayName;

    private String profilePic;

    private String postImg;

    @Lob
    private String content;

    @OneToMany(mappedBy = "post")
    private List<Like> likes;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    private Long saves = 0L;

    @CreationTimestamp
    private LocalDateTime createdAt;


}
