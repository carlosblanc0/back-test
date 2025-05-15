package com.anonymousibex.Agents.of.Revature.dto;

import com.anonymousibex.Agents.of.Revature.model.StoryPoint;
import com.anonymousibex.Agents.of.Revature.model.StoryPointOption;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public record StoryPointDto(
        Long id,
        String text,
        int chapterNumber,
        List<StoryPointOptionDto> options
) {}