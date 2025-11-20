package com.vitor.safenotes.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Notas")
@Data
public class Note {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @Column(columnDefinition = "TEXT")
    private String content;

}
