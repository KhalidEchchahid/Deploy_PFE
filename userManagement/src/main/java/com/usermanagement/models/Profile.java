package com.usermanagement.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "profile")
public class Profile {

    @Id
    @SequenceGenerator(
            name = "profile_id_sequence",
            sequenceName = "profile_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "profile_id_sequence"
    )
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    @Column(name = "image")
    private byte[] image;


}
