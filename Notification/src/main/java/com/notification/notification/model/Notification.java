package com.notification.notification.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonInclude
@Table(name = "notification")
public class Notification {
    @Id
    @SequenceGenerator(
            name = "notification_id_sequence",
            sequenceName = "notification_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "notification_id_sequence"
    )
    private Long id ;

    @Column(name = "content")
    private String content ;

    @Column(name = "sender_id")
    private Long senderId ;

    @Column(name = "semester")
    private String semester ;

    @Column(name = "created_at")
    private Date createdAt;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "notification_readers", joinColumns = @JoinColumn(name = "notification_id"))
    @Column(name = "user_id")
    private Set<Long> readers = new HashSet<>();
}
