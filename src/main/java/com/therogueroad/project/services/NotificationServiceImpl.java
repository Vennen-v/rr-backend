package com.therogueroad.project.services;

import com.therogueroad.project.dto.NotificationDTO;
import com.therogueroad.project.dto.PostDTO;
import com.therogueroad.project.models.Notification;
import com.therogueroad.project.models.NotificationType;
import com.therogueroad.project.models.User;
import com.therogueroad.project.repositories.NotificationRepository;
import com.therogueroad.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService{

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public List<NotificationDTO> getUserNotifs(User user) {

        List<Notification> notifications = user.getRecievedNotifs();

         List<NotificationDTO>notificationDTOs = notifications.stream()
                .map(notification -> modelMapper.map(notification, NotificationDTO.class))
                .toList();

         return notificationDTOs;
    }

    @Override
    public void sendLikeNotif(String actor, Long recipient, Long postId) {

        User actingUser = userRepository.findByUserName(actor).orElseThrow(()-> new RuntimeException("User Not Found"));

        User receivingUser = userRepository.findById(recipient).orElseThrow(()-> new RuntimeException("User Not Found"));

        if (actingUser.getUserId().equals(recipient)){
            return;
        }

        Notification notification = new Notification(actor, recipient, NotificationType.LIKE, postId);
        notificationRepository.save(notification);
        actingUser.getActedNotifs().add(notification);
        receivingUser.getRecievedNotifs().add(notification);

        userRepository.save(actingUser);
        userRepository.save(receivingUser);

        messagingTemplate.convertAndSend("/topic/users/" + recipient + "/notifications", notification);

    }

//    @Override
//    public void sendCommentNotif(String actor, Long recipient, Long postId) {
//        if (actor.equals(recipient)){
//            return;
//        }
//
//        Notification notification = new Notification(actor, recipient, NotificationType.COMMENT, postId);
//        notificationRepository.save(notification);
//
//        messagingTemplate.convertAndSend("/topic/users/" + recipient + "/notifications", notification);
//
//    }

    @Override
    public NotificationDTO markNotifAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setIsRead(true);
        messagingTemplate.convertAndSend("/topic/users/" + notification.getRecipient() + "/notifications", notification);
        notificationRepository.save(notification);
        return modelMapper.map(notification, NotificationDTO.class);
    }


}
