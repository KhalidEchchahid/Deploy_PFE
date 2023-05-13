package com.announcement.announcement.services;

import com.announcement.announcement.dto.AnnouncemetRequest;
import org.springframework.http.ResponseEntity;

public interface AnnouncementService {
    ResponseEntity<?> getAllAnnouncements() throws Exception;
    ResponseEntity<?> createAnnouncement(AnnouncemetRequest request) throws Exception;
    ResponseEntity<?> deleteAnnouncementById(Long id) throws Exception;
    ResponseEntity<?> updateAnnouncementById(Long id, AnnouncemetRequest request) throws Exception;

}
