package com.example.sentiment.model;

import java.util.List;

public record BatchRequest (
        List<String> texts
) {
}
