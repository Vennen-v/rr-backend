package com.therogueroad.project.services;

import com.therogueroad.project.dto.ConversationDTO;
import com.therogueroad.project.dto.MessageDT;
import com.therogueroad.project.dto.MessageDTO;
import com.therogueroad.project.models.Conversation;
import com.therogueroad.project.models.User;

import java.util.List;

public interface MessageService {
    List<ConversationDTO> getUserConvos(User user);

    ConversationDTO getConvoById(User user, Long conversationId);

    ConversationDTO createConvoAndSendMessage(User user, MessageDTO messageDTO);

    MessageDT sendMessageToConvo(User user, MessageDTO messageDTO, Long conversationId);

    void markMessageAsRead(User user, Long messageId);
}
