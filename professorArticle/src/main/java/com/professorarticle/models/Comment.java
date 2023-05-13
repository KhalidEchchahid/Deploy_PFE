package com.professorarticle.models;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@Table(name = "comments")
public class Comment {
    @Id
    @SequenceGenerator(
            name = "comment_id_sequence",
            sequenceName = "comment_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "comment_id_sequence"
    )
    private  Long id ;
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "createdAt")
    private Date createdAt ;
    @Column(name = "text")
    private String text;
    @ManyToOne
    @JoinColumn(name = "professor_article_id")
    private ProfessorArticle professorArticle;
}
