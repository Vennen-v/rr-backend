package com.therogueroad.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.therogueroad.project.models.Conversation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDT {
    private Long messageId;
    private UserDTO sender;
    private UserDTO receiver;
    private String content;
    private boolean isRead = false;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();
}
