package com.example.sentiment.model;

import java.util.List;

public record SentimentAnalysis (
        SentimentLabel label,
        double score,
        List<TokenSentiment> tokens
) {
}
