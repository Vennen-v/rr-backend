package com.therogueroad.project.services;

import com.therogueroad.project.dto.NotificationDTO;
import com.therogueroad.project.models.Notification;
import com.therogueroad.project.models.User;


import java.util.List;

public interface NotificationService {

    List<NotificationDTO> getUserNotifs(User user);

    NotificationDTO markNotifAsRead(Long notificationId);

    void sendLikeNotif(String actor, Long recipientId, Long postId);

//    void sendCommentNotif(String user, Long user1, Long commentId);
}
