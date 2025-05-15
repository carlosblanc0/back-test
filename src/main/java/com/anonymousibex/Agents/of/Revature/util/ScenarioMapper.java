package com.anonymousibex.Agents.of.Revature.util;

import com.anonymousibex.Agents.of.Revature.dto.ScenarioDto;
import com.anonymousibex.Agents.of.Revature.dto.StoryPointDto;
import com.anonymousibex.Agents.of.Revature.dto.StoryPointOptionDto;
import com.anonymousibex.Agents.of.Revature.model.Scenario;
import com.anonymousibex.Agents.of.Revature.model.StoryPoint;

import java.util.List;

public class ScenarioMapper {
    public static ScenarioDto toDto(Scenario scenario) {
        List<StoryPointDto> storyPointDtos = scenario.getStoryPoints().stream()
                .map(ScenarioMapper::toDto)
                .toList();

        return new ScenarioDto(
                scenario.getId(),
                scenario.getCalamity().getId(),
                scenario.getHeroSelection().getHeroes(),
                scenario.getPointTotal(),
                scenario.getChapterCount(),
                storyPointDtos,
                scenario.getClosing()
        );
    }
        public static StoryPointDto toDto(StoryPoint point) {
            List<StoryPointOptionDto> optionDtos = point.getOptions().stream()
                    .map(option -> new StoryPointOptionDto(option.getId(), option.getText()))
                    .toList();

            return new StoryPointDto(
                    point.getId(),
                    point.getText(),
                    point.getChapterNumber(),
                    optionDtos
            );
        }
    }

