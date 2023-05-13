package com.notification.notification.controllers;

import com.notification.notification.dto.NotificationDTO;
import com.notification.notification.services.NotificationService;
import com.openfeign.openfeign.user.UserClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {


  private final NotificationService notificationService;


    @PostMapping
    public ResponseEntity<?> sendNotificationEmail(@RequestBody NotificationDTO notificationDTO){
        return ResponseEntity.ok(notificationService.creatNotification(notificationDTO));
    }

    @GetMapping
    public ResponseEntity<?> getNotification(@RequestParam(defaultValue = "1") int page){
        return ResponseEntity.ok(notificationService.getNotificationsByPagination(page).getBody());
    }


    @PutMapping("/{id}/readers/{readerId}")
    public ResponseEntity<?> addUserToReaders(@PathVariable Long id, @PathVariable Long readerId) {
        boolean added = notificationService.addUserToReaders(id, readerId);
        if (added) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
