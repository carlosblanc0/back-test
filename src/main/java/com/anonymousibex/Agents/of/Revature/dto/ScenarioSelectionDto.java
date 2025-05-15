package com.anonymousibex.Agents.of.Revature.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ScenarioSelectionDto {
    private Long userId;
    private String username;
    private Long calamity_id;
    private String hero1;
    private String hero2;
    private String hero3;
}
