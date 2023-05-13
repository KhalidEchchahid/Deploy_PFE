package com.letters.letters.models;

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
@Table(name = "letter_request")
public class LetterRequest {

    @Id
    @SequenceGenerator(
            name = "letter_request_id_sequence",
            sequenceName = "letter_request_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "letter_request_id_sequence"
    )
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "professor_id")
    private Long professorId;
    @Lob
    @Column(name="report_card")
    private byte[] reportCard ;

    @Column(name = "created_at")
    private Date createdAt ;
    @Column(name = "letter_type")
    private String letterType;
    @Column(name = "cause")
    private String cause;
    @Column(name = "status")
    private String status;

    @Lob
    @Column(name="pdf_file")
    private byte[] pdfFile ;

    @Column(name="message")
    private String message;

}
