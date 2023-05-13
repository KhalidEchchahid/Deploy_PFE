package com.announcement.announcement.dto;

public record NotificationDTO(
        Long senderId ,
        String content ,
        String semester
) {

}
