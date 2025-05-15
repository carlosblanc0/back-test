package com.anonymousibex.Agents.of.Revature.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "calamities")
public class Calamity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;
    private String reported;
    private String location;
    private String description;
    private String villain;

    @Enumerated(EnumType.STRING)
    private Severity severity;
}
