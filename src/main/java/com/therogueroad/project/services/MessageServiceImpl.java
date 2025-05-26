package com.therogueroad.project.services;

import com.therogueroad.project.dto.ConversationDTO;
import com.therogueroad.project.dto.MessageDT;
import com.therogueroad.project.dto.MessageDTO;
import com.therogueroad.project.models.Conversation;
import com.therogueroad.project.models.Message;
import com.therogueroad.project.models.User;
import com.therogueroad.project.repositories.ConversationRepository;
import com.therogueroad.project.repositories.MessageRepository;
import com.therogueroad.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService{
    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ConversationDTO> getUserConvos(User user) {
        List<Conversation> userConvos = conversationRepository.findByAuthorOrRecipient(user, user);

        List<ConversationDTO> conversationDTOS = userConvos.stream()
                .map(conversation -> modelMapper.map(conversation, ConversationDTO.class))
                .toList();

        return conversationDTOS;
    }

    @Override
    public ConversationDTO getConvoById(User user, Long conversationId) {
       Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(()-> new RuntimeException("Conversation Not Found"));
       if (!conversation.getAuthor().getUserId().equals(user.getUserId()) && !conversation.getRecipient().getUserId().equals(user.getUserId())) {
           throw new RuntimeException("User not authorized to view conversation");
       }
       return modelMapper.map(conversation, ConversationDTO.class);
    }

    @Override
    @Transactional
    public ConversationDTO createConvoAndSendMessage(User user, MessageDTO messageDTO) {
       User receiver = userRepository.findById(messageDTO.getReceiverId()).orElseThrow(()-> new RuntimeException("User not found"));
       conversationRepository.findByAuthorAndRecipient(user, receiver).ifPresentOrElse(conversation -> {
           throw new RuntimeException("Conversation already exists, use the conversation Id to send messages");
       },
        ()-> {
        }
        );

        conversationRepository.findByAuthorAndRecipient(receiver, user).ifPresentOrElse(conversation -> {
                    throw new RuntimeException("Conversation already exists, use the conversation Id to send messages");
                },
                ()-> {
                }
        );

        Conversation conversation = new Conversation(user, receiver);
        conversationRepository.save(conversation);

        Message message = new Message(user, receiver, conversation, messageDTO.getContent());
        messageRepository.save(message);

        conversation.getMessages().add(message);

       notificationService.sendConvoToUsers(user.getUserName(), receiver.getUserId(), conversation);

       return modelMapper.map(conversation, ConversationDTO.class);

    }

    @Override
    public MessageDT sendMessageToConvo(User user, MessageDTO messageDTO, Long conversationId) {
        User receiver = userRepository.findById(messageDTO.getReceiverId()).orElseThrow(()-> new RuntimeException("User not found"));
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(()-> new RuntimeException("Conversation Not Found"));

        if(!conversation.getAuthor().getUserId().equals(user.getUserId()) && !conversation.getRecipient().getUserId().equals(user.getUserId())){
            throw new RuntimeException("User not authorized to send message to this conversation");
        }

        if(!conversation.getAuthor().getUserId().equals(receiver.getUserId()) && !conversation.getRecipient().getUserId().equals(receiver.getUserId())){
            throw new RuntimeException("Receiver does not belong to this conversation");
        }

        Message message = new Message(user, receiver, conversation, messageDTO.getContent());
        messageRepository.save(message);

        conversation.getMessages().add(message);

        notificationService.sendMessageToConversation(conversationId, message);
        notificationService.sendConvoToUsers(user.getUserName(), receiver.getUserId(), conversation);

        return modelMapper.map(message, MessageDT.class);
    }

    @Override
    public void markMessageAsRead(User user, Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(()-> new RuntimeException("Message Not Found"));

        if(!message.getReceiver().getUserId().equals(user.getUserId())){
            throw new RuntimeException("User not Authorized to mark message as read");
        }
        message.setIsRead(true);
        messageRepository.save(message);

    }


}
