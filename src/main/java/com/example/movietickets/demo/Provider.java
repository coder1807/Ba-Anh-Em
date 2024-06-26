package com.example.movietickets.demo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Provider {
    LOCAL("Local"),
    GOOGLE("Google");
    public final String value;
}