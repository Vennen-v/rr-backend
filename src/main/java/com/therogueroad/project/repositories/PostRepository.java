package com.therogueroad.project.repositories;

import com.therogueroad.project.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Post findByTitleContainingIgnoreCase(String keyword);
}
