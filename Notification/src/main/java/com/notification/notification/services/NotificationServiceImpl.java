package com.notification.notification.services;

import com.notification.notification.dto.NotificationDTO;
import com.notification.notification.model.Notification;
import com.notification.notification.repositories.NotificationRepository;
import com.openfeign.openfeign.user.UserClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService{

    private final UserClient client;
    private final NotificationRepository notificationRepository;
    private final MailService mailService;
    @Override
    public ResponseEntity<?> creatNotification(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setContent(notificationDTO.content());
        notification.setSenderId(notificationDTO.senderId());
        notification.setSemester(notificationDTO.semester());
        notification.setCreatedAt(new Date());
        notificationRepository.saveAndFlush(notification);

        log.info("---------------- semester {}" , notification.getSemester());
        //done : get all student from student management at the same semester :
        List<String> emails = client.getStudentBySemester(notification.getSemester());

        if(emails != null){
            log.info("--------------------------------Emails {}" , emails);

            //send this notification to all Students in the same semester

            for (String email : emails){
                mailService.sendEmail(email , "SMIF Announcement" , notification.getContent());
            }
        }

       return  ResponseEntity.ok(notification);
    }




    public ResponseEntity<?> getNotificationsByPagination(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.by("createdAt").descending());
        return ResponseEntity.ok(notificationRepository.findByOrderByCreatedAtDesc(pageable).getContent()) ;
    }


    public boolean addUserToReaders(Long notificationId, Long readerId) {
        Optional<Notification> optionalNotification = notificationRepository.findById(notificationId);
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            Set<Long> readers = notification.getReaders();
            if (!readers.contains(readerId)) {
                readers.add(readerId);
                notification.setReaders(readers);
                notificationRepository.save(notification);
                return true;
            }
        }
        return false;
    }
}
