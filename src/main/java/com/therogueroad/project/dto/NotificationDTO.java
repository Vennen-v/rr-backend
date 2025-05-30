package com.therogueroad.project.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.therogueroad.project.models.NotificationType;
import com.therogueroad.project.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    private Long notificationId;
    private Long recipient;
    private String actor;
    private boolean isRead;
    private NotificationType type;
    private Long resourceId;
    private String resourceString;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
