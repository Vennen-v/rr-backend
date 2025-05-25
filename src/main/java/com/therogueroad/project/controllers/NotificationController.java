package com.therogueroad.project.controllers;

import com.therogueroad.project.dto.NotificationDTO;
import com.therogueroad.project.models.Notification;
import com.therogueroad.project.models.NotificationType;
import com.therogueroad.project.models.User;
import com.therogueroad.project.repositories.UserRepository;
import com.therogueroad.project.security.services.UserDetailsImpl;
import com.therogueroad.project.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/notifs")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));
        return new ResponseEntity<>(notificationService.getUserNotifs(user), HttpStatus.OK);
    }

    @PutMapping("/user/notifs/{notificationId}")
    public ResponseEntity<NotificationDTO> markNotifAsRead(@PathVariable Long notificationId){
        return new ResponseEntity<>(notificationService.markNotifAsRead(notificationId), HttpStatus.OK);
    }
}
