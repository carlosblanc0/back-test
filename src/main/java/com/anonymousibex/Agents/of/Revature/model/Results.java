package com.anonymousibex.Agents.of.Revature.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "results")
public class Results {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne
    @JoinColumn(name = "scenario_id", referencedColumnName = "id")
    private Scenario scenario;

    @ManyToOne
    @JoinColumn(name = "calamity_id", referencedColumnName = "id")
    private Calamity calamity;

    @Column(name = "did_win")
    private boolean didWin;

    @Column(name = "rep_gained")
    private int repGained;
}
