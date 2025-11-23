package com.example.sentiment.model;

import java.util.List;

public record BatchResponse (
        List<SentimentResponse> results
) {
}
