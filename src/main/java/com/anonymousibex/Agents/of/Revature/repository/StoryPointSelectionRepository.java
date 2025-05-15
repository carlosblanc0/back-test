package com.anonymousibex.Agents.of.Revature.repository;

import com.anonymousibex.Agents.of.Revature.model.StoryPointSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StoryPointSelectionRepository extends JpaRepository<StoryPointSelection, Long> {
    List<StoryPointSelection> findByScenarioIdOrderByChapterNumberAsc(Long scenarioId);
}
