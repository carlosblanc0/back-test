package com.anonymousibex.Agents.of.Revature.service;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class GeminiService {

    private static final int MAX_RETRIES = 2;

    public String getValidResponse(
            String prompt,
            Function<String, Boolean> validator,
            Function<String, String> callGemini
    ) {
        String last = null;
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            String resp = callGemini.apply(prompt);
            if (validator.apply(resp)) {
                return resp;
            }
            last = resp;
            prompt = "Reminder: use '>' EXACTLY as delimiter, no extra text.\n\n" + prompt;
        }
        throw new IllegalStateException(
                "Gemini did not return a valid format after " + MAX_RETRIES +
                        " attempts. Last response:\n" + last
        );
    }
}
