package com.therogueroad.project.repositories;

import com.therogueroad.project.dto.PostDTO;
import com.therogueroad.project.models.Post;
import com.therogueroad.project.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {


    Page<Post> findByTitleContainingIgnoreCase(String keyword, Pageable pageDetails);

    Page<Post> findByUserOrderByCreatedAtAsc(User user, Pageable pageDetails);

    Page<Post> findByUser(User user, Pageable pageDetails);

    Post findByUser(User user);
};
