package com.example.sentiment.model;

public record TokenSentiment (
    String word,
    SentimentLabel label
) {
}
