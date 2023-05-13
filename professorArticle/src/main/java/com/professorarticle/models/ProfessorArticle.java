package com.professorarticle.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonInclude
@Table(name = "professor_article")
public class ProfessorArticle {
    @Id
    @SequenceGenerator(
            name = "professor_article_id_sequence",
            sequenceName = "professor_article_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "professor_article_id_sequence"
    )
    private Long id ;

    @Column(name="title")
    private String title;
    @Lob
    @Column(name="image_data")
    private byte[] imageData ;
    @Column(name="content" , length = 20000)
    private String content;
    @Column(name="category")
    private String category;
    @Column(name="createdAt")
    private Date createdAt;
    @Column(name="user_id")
    private Long userId;

    @OneToMany(mappedBy = "professorArticle" , cascade = CascadeType.ALL ,orphanRemoval = true ,fetch = FetchType.EAGER)
    @JsonIgnoreProperties("professorArticle")
    private List<Comment> comments = new ArrayList<>();

}
