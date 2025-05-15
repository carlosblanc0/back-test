package com.anonymousibex.Agents.of.Revature.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "story_points")
public class StoryPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;

    @Column(columnDefinition = "TEXT")
    private String text;

    @OneToMany(mappedBy = "storyPoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoryPointOption> options = new ArrayList<>();

    private int chapterNumber;

}
