package com.dating.controller;

import com.dating.service.ZodiacCompatibilityService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/userapi")
public class CompatibilityController {

    private final ZodiacCompatibilityService zodiacCompatibilityService;

    public CompatibilityController(ZodiacCompatibilityService zodiacCompatibilityService) {
        this.zodiacCompatibilityService = zodiacCompatibilityService;
    }

    @GetMapping("/compatibility")
    public Mono<Map<String, Integer>> getCompatibility(@RequestParam String first,
                                                       @RequestParam String second) {
        return zodiacCompatibilityService.getCompatibilityScore(first, second)
                .map(score -> Map.of("score", score));
    }
}
