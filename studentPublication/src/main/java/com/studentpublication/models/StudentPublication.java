package com.studentpublication.models;


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
@Table(name = "student_publication")
public class StudentPublication {
    @Id
    @SequenceGenerator(
            name = "student_publication_id_sequence",
            sequenceName = "student_publication_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_publication_id_sequence"
    )
    private Long id ;


    @Column(name="title")
    private String title;
    @Lob
    @Column(name="pdf_file")
    private byte[] pdfFile ;

    @Column(name="description")
    private String description;
    @Column(name="tags")
    private String tags;
    @Column(name="createdAt")
    private Date createdAt;
    @Column(name="user_id")
    private Long userId;

    @OneToMany(mappedBy = "studentPublication" , cascade = CascadeType.ALL ,orphanRemoval = true ,fetch = FetchType.EAGER)
    @JsonIgnoreProperties("studentPublication")
    private List<Comment> comments = new ArrayList<>();
    @OneToMany(mappedBy = "studentPublication" , cascade = CascadeType.ALL ,orphanRemoval = true,fetch = FetchType.EAGER)
    @JsonIgnoreProperties("studentPublication")
    private List<Like> likes = new ArrayList<>();

}
