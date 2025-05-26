package com.therogueroad.project.repositories;

import com.therogueroad.project.models.Conversation;
import com.therogueroad.project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByAuthorAndRecipient(User author, User recipient);
    List<Conversation> findByAuthorOrRecipient(User user1, User user2);

}
