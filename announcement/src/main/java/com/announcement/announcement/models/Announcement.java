package com.announcement.announcement.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonInclude
@Table(name = "announcement")
public class Announcement {
        @Id
        @SequenceGenerator(
                name = "announcement_id_sequence",
                sequenceName = "announcement_id_sequence",
                allocationSize = 1
        )
        @GeneratedValue(
                strategy = GenerationType.SEQUENCE,
                generator = "announcement_id_sequence"
        )
        private Long id ;

        @Column(name="title")
        private String title;
        @Nullable
        @Lob
        @Column(name="file")
        private byte[] File ;

        @Column(name="file_name")
        private String fileName;
        @Column(name="description")
        private String description;
        @Column(name="createdAt")
        private Date createdAt;
        @Column(name="creator-name")
        private String creatorName;
        @Column(name="user_id")
        private Long userId;

        @Column(name = "semester")
        private String semester ;
}
