package com.therogueroad.project.controllers;

import com.therogueroad.project.dto.ConversationDTO;
import com.therogueroad.project.dto.MessageDT;
import com.therogueroad.project.dto.MessageDTO;
import com.therogueroad.project.models.Conversation;
import com.therogueroad.project.models.User;
import com.therogueroad.project.repositories.UserRepository;
import com.therogueroad.project.security.services.UserDetailsImpl;
import com.therogueroad.project.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationDTO>> getUserConvos(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));
        return new ResponseEntity<>(messageService.getUserConvos(user), HttpStatus.OK);
    }

    @GetMapping("/conversations/{conversationId}")
    public ResponseEntity<ConversationDTO> getConvo(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long conversationId){
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));
        return new ResponseEntity<>(messageService.getConvoById(user, conversationId), HttpStatus.OK);
    }

    @PostMapping("/conversations")
    public ResponseEntity<ConversationDTO> createConversationAndAddMessage(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody MessageDTO messageDTO){
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));
        return new ResponseEntity<>(messageService.createConvoAndSendMessage(user, messageDTO), HttpStatus.OK);
    }

    @PostMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<MessageDT> sendMessageToConvo(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody MessageDTO messageDTO, @PathVariable Long conversationId){
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));
        return new ResponseEntity<>(messageService.sendMessageToConvo(user, messageDTO, conversationId), HttpStatus.OK);
    }

    @PutMapping("/conversations/messages/{messageId}")
    public ResponseEntity<String> markMessageAsRead(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long messageId){
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));
        messageService.markMessageAsRead(user, messageId);
        return new ResponseEntity<>("Message has been read", HttpStatus.OK);

    }

}
