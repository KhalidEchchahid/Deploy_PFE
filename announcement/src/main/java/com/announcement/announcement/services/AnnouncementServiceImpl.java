package com.announcement.announcement.services;

import com.amqp.amqp.RabbitMQMessageProducer;
import com.announcement.announcement.dto.AnnouncemetRequest;
import com.announcement.announcement.dto.NotificationDTO;
import com.announcement.announcement.models.Announcement;
import com.announcement.announcement.repositories.AnnouncementRepository;
import com.announcement.announcement.util.FileUtils;


import com.vonage.client.VonageClient;

import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



import java.util.Date;
import java.util.List;
import java.util.Optional;



@Service
@AllArgsConstructor
@Slf4j
public class AnnouncementServiceImpl implements AnnouncementService {



    private final RabbitMQMessageProducer rabbitMQMessageProducer;
    private final AnnouncementRepository announcementRepository;


    @Override
    public ResponseEntity<?> getAllAnnouncements() throws Exception{
        try {

                List<Announcement> announcements = announcementRepository.findAllByOrderByCreatedAtDesc();
                for (Announcement announcement : announcements) {
                    if (announcement.getFile() != null) {
                        announcement.setFile(FileUtils.decompressFile(announcement.getFile()));
                    }
                }

                return ResponseEntity.ok().body(announcements);

        }catch (Exception e){
            throw new Exception("Error getting announcements", e);
        }
    }

    @Override
    public ResponseEntity<?> createAnnouncement(AnnouncemetRequest request) throws Exception {
        try {
            Announcement announcement = new Announcement();
            String text ;
            if (request.file() != null) {
                announcement.setFile(FileUtils.compressFile(request.file().getBytes()));
                announcement.setFileName(request.file().getOriginalFilename());
            }
            announcement.setTitle(request.title());
            announcement.setDescription(request.description());
            announcement.setCreatedAt(new Date());
            announcement.setUserId(request.userId());
            announcement.setCreatorName(request.creator());
            announcement.setSemester(request.semester());
            announcement = announcementRepository.saveAndFlush(announcement);
            if (announcement.getFile() != null) {
                announcement.setFile(FileUtils.decompressFile(announcement.getFile()));
            }
            log.info("semester {} " , announcement.getSemester() );
            if(announcement.getDescription().length() >= 21){
                 text = announcement.getDescription().substring(0, 20)+"..." ;
            }else {
                 text = announcement.getDescription();
            }
            NotificationDTO notificationDTO = new NotificationDTO(announcement.getUserId() , "New Announcement : " + text , announcement.getSemester() );
            rabbitMQMessageProducer.publish(
                    notificationDTO ,
                    "internal.exchange",
                    "internal.notification.routing-key"
            );
            return ResponseEntity.ok().body(announcement);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }



//    public void sendSMSNotification() throws Exception {
//        String apiKey = "85252ded";
//        String apiSecret = "8MOVDg31AkNhV1PA";
//        String FROM_NUMBER = "123456";
//        String TO_NUMBER = "+212645557609";
//        VonageClient client = VonageClient.builder()
//                .apiKey(apiKey)
//                .apiSecret(apiSecret)
//                .build();
//
//        SmsSubmissionResponse responses = client.getSmsClient().submitMessage(new TextMessage(
//                FROM_NUMBER,
//                TO_NUMBER,
//                "new announcement from SMI website check it "));
//
//        for (SmsSubmissionResponseMessage response : responses.getMessages()) {
//            System.out.println(response);
//        }
//    }


    @Override
    public ResponseEntity<?> deleteAnnouncementById(Long id) throws Exception {
        try {
            Optional<Announcement> announcement = announcementRepository.findById(id);

            if (announcement.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

//            //TODO: Check if the authenticated user is the creator of the announcement
//
//
//
//            if (userId1.id() != userId){
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//            }


            announcementRepository.delete(announcement.get());
            return ResponseEntity.ok().body(id);

        }catch (Exception e){
            throw new Exception("Error deleting announcement", e);
        }

    }


    @Override
    public ResponseEntity<?> updateAnnouncementById(Long id, AnnouncemetRequest request) throws Exception {

        try {

            Optional<Announcement> announcementOptional = announcementRepository.findById(id);

            if (announcementOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            //TODO: Check if the authenticated user is the creator of the announcement

            Announcement announcement = announcementOptional.get();
            announcement.setTitle(request.title());
            if (request.file() != null) {
                announcement.setFile(FileUtils.compressFile(request.file().getBytes()));
                announcement.setFileName(request.file().getOriginalFilename());
            }
            announcement.setDescription(request.description());
            announcement.setCreatedAt(new Date());
            announcement.setUserId(request.userId());

            announcement = announcementRepository.saveAndFlush(announcement);
            if (announcement.getFile() != null) {
                announcement.setFile(FileUtils.decompressFile(announcement.getFile()));
            }

            return ResponseEntity.ok().body(announcement);
        }catch (Exception e){
            throw new Exception("Error updating announcement", e);

        }
    }

}

