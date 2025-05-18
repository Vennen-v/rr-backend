package com.therogueroad.project.repositories;


import com.therogueroad.project.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByUserName(String username);

    List<User> findByDisplayNameContainingIgnoreCase(String keyword);

    List<User> findByUserNameContainingIgnoreCase(String keyword);

    Page<User> findByUserNameOrDisplayNameContainingIgnoreCase(String keyword, String keyword2, Pageable pageDetails);
}
