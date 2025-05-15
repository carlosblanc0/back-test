package com.anonymousibex.Agents.of.Revature.service;

import com.anonymousibex.Agents.of.Revature.dto.ContinueScenarioRequest;
import com.anonymousibex.Agents.of.Revature.dto.ScenarioDto;
import com.anonymousibex.Agents.of.Revature.dto.ScenarioRequestDto;
import com.anonymousibex.Agents.of.Revature.exception.CalamityNotFoundException;
import com.anonymousibex.Agents.of.Revature.model.*;
import com.anonymousibex.Agents.of.Revature.repository.*;
import com.anonymousibex.Agents.of.Revature.util.ScenarioMapper;
import com.anonymousibex.Agents.of.Revature.util.ScenarioUtils;
import com.google.genai.Client;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ScenarioService {

    private final CalamityRepository calamityRepository;
    private final ScenarioRepository scenarioRepository;
    private final StoryPointSelectionRepository storyPointSelectionRepository;
    private final StoryPointOptionRepository storyPointOptionRepository;
    private final GeminiService geminiService;
    private final ResultsService resultsService;
    private final UserService userService;


    private final Function<String, String> callGemini = prompt ->
            new Client()
                    .models
                    .generateContent("gemini-2.0-flash-001", prompt, null)
                    .text();

    public ScenarioDto startScenario(ScenarioRequestDto requestDto, HttpServletRequest httpRequest) {
        Calamity calamity = calamityRepository.findById(requestDto.calamityId())
                .orElseThrow(() -> new CalamityNotFoundException("Calamity not found"));
        User user = userService.getCurrentUserBySession(httpRequest);

        HeroSelection heroSelection = new HeroSelection();
        heroSelection.setHero1(requestDto.hero1());
        heroSelection.setHero2(requestDto.hero2());
        heroSelection.setHero3(requestDto.hero3());
        heroSelection.setCalamity(calamity);
        heroSelection.setUser(user);

        Scenario scenario = new Scenario();
        scenario.setCalamity(calamity);
        scenario.setUser(user);
        scenario.setHeroSelection(heroSelection);
        scenario.setChapterCount(1);
        scenario.setPointTotal(0);
        scenario = scenarioRepository.save(scenario);

        List<StoryPointSelection> selections = storyPointSelectionRepository
                .findByScenarioIdOrderByChapterNumberAsc(scenario.getId());
        String prompt = ScenarioUtils.buildPrompt(requestDto, scenario, selections);

        String raw = geminiService.getValidResponse(
                prompt,
                ScenarioUtils::isValidGeminiResponse,
                callGemini
        );

        StoryPoint firstPoint = ScenarioUtils.parseStoryPoint(raw, scenario, 1);
        scenario.getStoryPoints().add(firstPoint);
        scenarioRepository.save(scenario);

        return ScenarioMapper.toDto(scenario);
    }

    public ScenarioDto continueScenario(Long scenarioId, ContinueScenarioRequest req) {
        Scenario scenario = scenarioRepository.findById(scenarioId)
                .orElseThrow(() -> new EntityNotFoundException("Scenario not found"));

        StoryPointOption picked = storyPointOptionRepository.findById(req.selectedOptionId())
                .orElseThrow(() -> new EntityNotFoundException("Option not found"));

        int currentChapter = scenario.getChapterCount();
        StoryPointSelection sel = new StoryPointSelection();
        sel.setScenario(scenario);
        sel.setStoryPoint(picked.getStoryPoint());
        sel.setSelectedOption(picked);
        sel.setChapterNumber(currentChapter);
        storyPointSelectionRepository.save(sel);

        scenario.setPointTotal(scenario.getPointTotal() + picked.getPoints());
        scenario.setChapterCount(currentChapter + 1);
        scenarioRepository.save(scenario);

        int nextChapter = scenario.getChapterCount();

        if (nextChapter <= 5) {
            List<StoryPointSelection> allSelections = storyPointSelectionRepository
                    .findByScenarioIdOrderByChapterNumberAsc(scenarioId);
            String prompt = ScenarioUtils.buildPrompt(
                    ScenarioUtils.toRequestDto(scenario),
                    scenario,
                    allSelections
            );

            String raw = geminiService.getValidResponse(
                    prompt,
                    ScenarioUtils::isValidGeminiResponse,
                    callGemini
            );

            StoryPoint nextPoint = ScenarioUtils.parseStoryPoint(raw, scenario, nextChapter);
            scenario.getStoryPoints().add(nextPoint);
            scenarioRepository.save(scenario);

            return ScenarioMapper.toDto(scenario);

        } else {
            List<StoryPointSelection> allSelections = storyPointSelectionRepository
                    .findByScenarioIdOrderByChapterNumberAsc(scenarioId);
            String recap = ScenarioUtils.buildContextRecap(scenario, allSelections);

            int totalPoints = scenario.getPointTotal();
            Severity severity = scenario.getCalamity().getSeverity();
            int required = ScenarioUtils.THRESHOLDS.get(severity);
            boolean success = totalPoints >= required;

            String resultLine = success
                    ? "RESULT: Mission succeeded with " + totalPoints + " points (needed " + required + ")."
                    : "RESULT: Mission failed with only " + totalPoints + " points (needed " + required + ").";

            String prompt = ScenarioUtils.FINAL_PROMPT
                    + "\n\n" + resultLine
                    + "\n\nContext recap:\n" + recap;

            String closingNarrative = callGemini.apply(prompt).trim();

            scenario.setComplete(true);
            scenario.setClosing(closingNarrative);
            scenarioRepository.save(scenario);

            int repGained = success? totalPoints : 0;
            resultsService.addResult(scenario, success, repGained);

            return ScenarioMapper.toDto(scenario);
        }
    }
}
