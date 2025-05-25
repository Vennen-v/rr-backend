package com.therogueroad.project.repositories;

import com.therogueroad.project.models.Notification;
import com.therogueroad.project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipient(User recipient);
    List<Notification> findByRecipientOrderByCreatedAtDesc(User user);
}
