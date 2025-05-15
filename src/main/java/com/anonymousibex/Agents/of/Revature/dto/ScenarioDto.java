package com.anonymousibex.Agents.of.Revature.dto;

import com.anonymousibex.Agents.of.Revature.model.HeroSelection;

import java.util.List;

public record ScenarioDto(
        Long id,
        Long calamityId,
        List<String> heroes,
        int pointTotal,
        int chapterCount,
        List<StoryPointDto> storyPoints,
        String closing
) {}