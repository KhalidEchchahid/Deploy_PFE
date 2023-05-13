package com.notification.notification.services;

import com.notification.notification.dto.NotificationDTO;
import org.springframework.http.ResponseEntity;

public interface NotificationService {
    ResponseEntity<?> creatNotification(NotificationDTO notificationDTO);
    ResponseEntity<?> getNotificationsByPagination(int pageNumber);
    boolean addUserToReaders(Long notificationId, Long readerId);
}
