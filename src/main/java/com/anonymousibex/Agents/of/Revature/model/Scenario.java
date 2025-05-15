package com.anonymousibex.Agents.of.Revature.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "scenarios")
public class Scenario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "calamity_id")
    private Calamity calamity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hero_selection_id", referencedColumnName = "id")
    private HeroSelection heroSelection;

    private int pointTotal = 0;
    private int chapterCount = 0;
    private boolean complete = false;

    @Column(length = 1000)
    private String closing;

    @OneToOne(
            mappedBy = "scenario",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Results result;

    @OneToMany(mappedBy = "scenario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoryPoint> storyPoints = new ArrayList<>();

//    @Transient
//    public List<String> getHeroes() {
//        return List.of(hero1, hero2, hero3);
//    }
}
