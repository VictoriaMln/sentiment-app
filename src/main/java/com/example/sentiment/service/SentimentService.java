package com.example.sentiment.service;

import com.example.sentiment.model.SentimentAnalysis;
import com.example.sentiment.model.SentimentLabel;
import com.example.sentiment.model.TokenSentiment;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class SentimentService {

    private final Map<SentimentLabel, Set<String>> dictionary = new EnumMap<>(SentimentLabel.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String MODEL_VERSION = "1.0.0";

    @PostConstruct
    public void loadDictionary() throws IOException {
        dictionary.put(SentimentLabel.POSITIVE, new HashSet<>());
        dictionary.put(SentimentLabel.NEGATIVE, new HashSet<>());
        dictionary.put(SentimentLabel.NEUTRAL, new HashSet<>());

        ClassPathResource resource = new ClassPathResource("sentiment_dict.json");
        try (InputStream is = resource.getInputStream()) {
            JsonNode root = objectMapper.readTree(is);
            root.get("positive").forEach(node ->
                    dictionary.get(SentimentLabel.POSITIVE).add(node.asText().toLowerCase()));
            root.get("negative").forEach(node ->
                    dictionary.get(SentimentLabel.NEGATIVE).add(node.asText().toLowerCase()));
            root.get("neutral").forEach(node ->
                    dictionary.get(SentimentLabel.NEUTRAL).add(node.asText().toLowerCase()));
        }
    }

    public SentimentAnalysis analyzeText(String text) {
        if (text == null || text.isBlank()) {
            return new SentimentAnalysis(SentimentLabel.NEUTRAL, 0.0, List.of());
        }

        String lower = text.toLowerCase(Locale.ROOT);
        String[] rawTokens = lower.split("\\W+");

        List<TokenSentiment> tokenSentiments = new ArrayList<>();
        int score = 0;
        int countedTokens = 0;

        for (String token : rawTokens) {
            if (token.isBlank()) continue;

            SentimentLabel label = detectLabel(token);
            if (label != null && label != SentimentLabel.NEUTRAL) {
                countedTokens++;
                if (label == SentimentLabel.POSITIVE) {
                    score++;
                } else if (label == SentimentLabel.NEGATIVE) {
                    score--;
                }
            }

            if (label != null) {
                tokenSentiments.add(new TokenSentiment(token, label));
            }
        }

        SentimentLabel finalLabel;
        double confidence;

        if (countedTokens == 0) {
            finalLabel = SentimentLabel.NEUTRAL;
            confidence = 0.5;
        } else {
            if (score > 0) {
                finalLabel = SentimentLabel.POSITIVE;
            } else if (score < 0) {
                finalLabel = SentimentLabel.NEGATIVE;
            } else {
                finalLabel = SentimentLabel.NEUTRAL;
            }
            confidence = Math.min(1.0, Math.abs((double) score) / countedTokens);
        }

        return new SentimentAnalysis(finalLabel, confidence, tokenSentiments);
    }

    private SentimentLabel detectLabel(String token) {
        String t = token.toLowerCase(Locale.ROOT);
        if (dictionary.get(SentimentLabel.POSITIVE).contains(t)) {
            return SentimentLabel.POSITIVE;
        }
        if (dictionary.get(SentimentLabel.NEGATIVE).contains(t)) {
            return SentimentLabel.NEGATIVE;
        }
        if (dictionary.get(SentimentLabel.NEUTRAL).contains(t)) {
            return SentimentLabel.NEUTRAL;
        }
        return null;
    }

    public String modelVersion() {
        return MODEL_VERSION;
    }
}

