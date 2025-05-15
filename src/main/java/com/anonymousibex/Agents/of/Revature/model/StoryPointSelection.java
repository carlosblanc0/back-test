package com.anonymousibex.Agents.of.Revature.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "story_point_selections")
public class StoryPointSelection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "story_point_id")
    private StoryPoint storyPoint;

    @ManyToOne(optional = false)
    @JoinColumn(name = "selected_option_id")
    private StoryPointOption selectedOption;

    private int chapterNumber;
}