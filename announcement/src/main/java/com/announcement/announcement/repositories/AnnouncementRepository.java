package com.announcement.announcement.repositories;

import com.announcement.announcement.models.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement , Long> {
    @Transactional
    List<Announcement> findAllByOrderByCreatedAtDesc();
}
