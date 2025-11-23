package com.example.sentiment.controller;

import com.example.sentiment.model.*;
import com.example.sentiment.service.SentimentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SentimentController {

    private final SentimentService sentimentService;

    public SentimentController(SentimentService sentimentService) {
        this.sentimentService = sentimentService;
    }

    @GetMapping("/sentiment")
    public SentimentResponse analyze(@RequestParam String text) {
        long start = System.currentTimeMillis();

        SentimentAnalysis analysis = sentimentService.analyzeText(text);

        long processingMs = System.currentTimeMillis() - start;
        String pod = Optional.ofNullable(System.getenv("HOSTNAME")).orElse("local");
        String modelVersion = sentimentService.modelVersion();

        return new SentimentResponse(
                text,
                analysis.label(),
                analysis.score(),
                analysis.tokens(),
                modelVersion,
                processingMs,
                pod
        );
    }

    @PostMapping("/sentiment/batch")
    public BatchResponse analyzeBatch(@RequestBody BatchRequest request) {
        List<SentimentResponse> results = request.texts().stream()
                .map(this::analyze)
                .toList();
        return new BatchResponse(results);
    }
}
