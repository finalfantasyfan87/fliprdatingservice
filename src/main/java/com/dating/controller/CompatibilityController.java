package com.dating.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dating.service.ZodiacCompatibilityService;

import reactor.core.publisher.Mono;

/**
 * CompatibilityController
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/userapi")
public class CompatibilityController {

    private final ZodiacCompatibilityService zodiacCompatibilityService;

    public CompatibilityController(ZodiacCompatibilityService zodiacCompatibilityService) {
        this.zodiacCompatibilityService = zodiacCompatibilityService;
    }

    /**
     * function to get compatibility score between two zodiac signs
     * @param first
     * @param second
     * @return
     */
    @GetMapping("/compatibility")
    public Mono<Map<String, Integer>> getCompatibility(@RequestParam String first,
                                                       @RequestParam String second) {
        return zodiacCompatibilityService.getCompatibilityScore(first, second)
                .map(score -> Map.of("score", score));
    }
}
