package com.example.sentiment.model;

import java.util.List;

public record SentimentResponse (
        String input,
        SentimentLabel label,
        double score,
        List<TokenSentiment> tokens,
        String modelVersion,
        long processingMsg,
        String pod
) {
}
