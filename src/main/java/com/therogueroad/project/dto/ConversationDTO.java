package com.therogueroad.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationDTO {


    private Long conversationId;
    private UserDTO author;
    private UserDTO recipient;
    private List<MessageDT> messages = new ArrayList<>();
}
