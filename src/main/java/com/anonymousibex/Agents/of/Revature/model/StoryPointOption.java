package com.anonymousibex.Agents.of.Revature.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "story_point_options")
public class StoryPointOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "story_point_id")
    private StoryPoint storyPoint;

    @Column(length = 1000)
    private String text;

    private int points;

    public StoryPointOption(String text, int points) {
        this.text = text;
        this.points = points;
    }

    public StoryPointOption() {}
}
