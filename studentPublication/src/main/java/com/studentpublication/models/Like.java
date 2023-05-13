package com.studentpublication.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "likes")
public class Like {
    @Id
    @SequenceGenerator(
            name = "like_id_sequence",
            sequenceName = "like_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "like_id_sequence"
    )
    private  Long id ;
    @Column(name = "user_id")
    private Long userId;
    @ManyToOne
    @JoinColumn(name = "student_publication_id")
    private StudentPublication studentPublication;
}
