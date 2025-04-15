package com.therogueroad.project.repositories;

import com.therogueroad.project.models.Post;
import com.therogueroad.project.models.Userr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Userr, Long> {
    Userr findByUserName(String username);

    boolean existsByUserName(String username);


}
