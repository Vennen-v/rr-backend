package com.therogueroad.project.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Lob
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Userr user;

    private String userName;

    private String displayName;

    private String profilePic;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany
    @JoinColumn(name = "parent_id")
    private List<Comment> replies;

    @OneToMany(mappedBy = "comment")
    private List<Like> likes = new ArrayList<>();

    private LocalDateTime createdAt;

    public void addCommentReply(Comment comment){
        if (replies == null) {
            replies = new ArrayList<>();
        }
        replies.add(comment);
    }

}
