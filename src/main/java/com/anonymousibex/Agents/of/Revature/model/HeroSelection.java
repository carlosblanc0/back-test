package com.anonymousibex.Agents.of.Revature.model;

import jakarta.persistence.*;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "hero_selections")

public class HeroSelection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "calamity_id", referencedColumnName = "id")
    private Calamity calamity;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(mappedBy = "heroSelection")
    private Scenario scenario;

    @Column(name = "hero1")
    private String hero1;
    
    @Column(name = "hero2")
    private String hero2;

    @Column(name = "hero3")
    private String hero3;

    @Transient
    public List<String> getHeroes(){
        List<String> heroes = new ArrayList<>();
        heroes.add(hero1);
        heroes.add(hero2);
        heroes.add(hero3);
        return heroes;
    }
}
