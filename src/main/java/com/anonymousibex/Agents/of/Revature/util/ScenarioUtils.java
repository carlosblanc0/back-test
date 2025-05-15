package com.anonymousibex.Agents.of.Revature.util;

import com.anonymousibex.Agents.of.Revature.dto.ScenarioRequestDto;
import com.anonymousibex.Agents.of.Revature.model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ScenarioUtils {
    static final public String PROMPT =
            "You are writing a 5-chapter interactive superhero story. "+
                    "The user is a special agent responding to a calamity with a team of 3 heroes they have selected. "+
                    "Each chapter contains a narrative followed by exactly 3 choices. " +
                    "The choices are for the user to decide how to best utilize their hero team given the chapter context. " +
                    "Each choice must include a point value from 0 to 3. The chapter’s story difficulty depends on the calamity’s severity: critical, high, medium, or low. " +
                    "Bonus points (up to +1) are awarded if the choice effectively uses a hero from the villain’s universe. " +
                    "Do not share which options are contain bonus points. "+
                    "You must format your response using the '>' character as a delimiter and **nothing else**. The formatting is critical — I am parsing your response automatically.\n\n" +

                    "Required format:\n" +
                    "[1] Story text describing the chapter scenario (1 paragraph)>[2] First option text>[3] First option points>[4] Second option text>[5] Second option points>[6] Third option text>[7] Third option points\n\n" +

                    "Example:\n" +
                    "The heroes arrive at a city overrun by robotic drones. Citizens are fleeing in panic as a massive drone prepares to detonate a plasma bomb.>Attempt to disable the bomb with tech hero support.>2>Evacuate civilians first to minimize casualties.>1>Confront the drone directly using brute force.>0\n\n" +

                    "**Do not include any other symbols, explanations, or text outside of this format. This is essential.**\n\n" +

                    "You will be provided with the team of heroes, the calamity, the villain responsible, current chapter number, and a recap of previous chapters. " +
                    "The first chapter scenario should include a reveal of the villain. Each successive chapter scenario should include a dramatic unfolding of the user's previous choice before setting the stage for the next choice. "+
                    "Now generate the story and response for the current chapter.\n\n" +

                    "The context is as follows:";

    static final public String FINAL_PROMPT =
            "Provide me the story conclusion to the text based adventure that I will share with you below. " +
                    "For critical missions, I'll need 14 points to succeed (maximum of 15 total). For high severity missions I'll need 12, for medium I'll need 10, and for low I'll need 8. "+
                    "Do not provide any new options or point values. " +
                    "Instead, deliver a succinct closing narrative that explains what happens " +
                    "to the heroes, reveals whether they succeeded or failed, and states the final point total. " +
                    "Format your response as a single paragraph with no special delimiters.";

    public static String buildPrompt(ScenarioRequestDto dto, Scenario scenario, List<StoryPointSelection> selections) {
        StringBuilder promptBuilder = new StringBuilder(PROMPT);

        promptBuilder.append("\nMission team: ")
                .append(dto.hero1()).append(", ")
                .append(dto.hero2()).append(", ")
                .append(dto.hero3()).append(".");

        promptBuilder.append("\nCalamity: ")
                .append(scenario.getCalamity().getTitle())
                .append(" — ").append(scenario.getCalamity().getDescription())
                .append(" -- Villain responsible: ").append(scenario.getCalamity().getVillain())
                .append(" (Severity: ").append(scenario.getCalamity().getSeverity()).append(")");

        promptBuilder.append("\nThis is Chapter ")
                .append(scenario.getChapterCount())
                .append(" of 5.");

        promptBuilder.append("\nMy current point total is: ")
                .append(scenario.getPointTotal())
                .append(".");

        if (selections != null && !selections.isEmpty()) {
            promptBuilder.append("\n\nThe context is as follows:\n");
            selections.stream()
                    .sorted(Comparator.comparingInt(StoryPointSelection::getChapterNumber))
                    .forEach(selection -> {
                        StoryPoint point = selection.getStoryPoint();
                        promptBuilder.append("Chapter ").append(selection.getChapterNumber()).append(": ")
                                .append(point.getText()).append("\n")
                                .append("My choice: ").append(selection.getSelectedOption().getText()).append("\n");
                    });
        } else {
            promptBuilder.append("\n\nThe context is as follows: (first chapter — no prior events)");
        }
        promptBuilder.append("\n\nPlease remember: return only the scenario text and 3 options with point values, formatted using '>' as described. No extra commentary.\n");

        return promptBuilder.toString();
    }

    public static StoryPoint parseStoryPoint(String raw, Scenario scenario, int chapterNum) {
        System.out.println("Parsing StoryPoint raw:\n" + raw);

        String[] tokens = raw.split(">");
        String storyText = tokens[0].trim();

        List<StoryPointOption> options = new ArrayList<>();
        for (int i = 1; i + 1 < tokens.length; i += 2) {
            String optText = tokens[i].trim();
            String ptsToken = tokens[i + 1].trim().replaceAll("\\D+$", "");
            int pts = Integer.parseInt(ptsToken);

            StoryPointOption opt = new StoryPointOption(optText, pts);
            options.add(opt);
        }

        if (options.size() != 3) {
            throw new IllegalArgumentException(
                    "Expected exactly 3 options but found " + options.size() +
                            "\nRaw response:\n" + raw
            );
        }

        StoryPoint point = new StoryPoint();
        point.setScenario(scenario);
        point.setText(storyText);
        point.setChapterNumber(chapterNum);

        options.forEach(opt -> opt.setStoryPoint(point));
        point.setOptions(options);

        return point;
    }

    public static String buildContextRecap(Scenario scenario, List<StoryPointSelection> selections) {
        var sb = new StringBuilder();
        for (StoryPointSelection sel : selections) {
            sb.append("Chapter ")
                    .append(sel.getChapterNumber())
                    .append(": ")
                    .append(sel.getStoryPoint().getText())
                    .append(" My choice: ")
                    .append(sel.getSelectedOption().getText())
                    .append(". ");
        }
        return sb.toString();
    }

    public static ScenarioRequestDto toRequestDto(Scenario scenario) {
        HeroSelection heroSelection = scenario.getHeroSelection();
        return new ScenarioRequestDto(
                scenario.getCalamity().getId(),
                heroSelection.getHero1(),
                heroSelection.getHero2(),
                heroSelection.getHero3()
        );
    }

    public static boolean isValidGeminiResponse(String raw) {
        return raw.split(">").length == 7;
    }

    public static final Map<Severity, Integer> THRESHOLDS = Map.of(
            Severity.CRITICAL, 14,
            Severity.HIGH,     12,
            Severity.MEDIUM,   10,
            Severity.LOW,       8
    );
}