package com.YouTubeTools.controller;

import com.YouTubeTools.Model.SearchVideo;
import com.YouTubeTools.Service.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/youtube")
public class YouTubeTagsController {

    @Autowired
    private YouTubeService youTubeService;

    @Value("${youtube.api.key}")
    private String apiKey;

    private boolean isApiKeyConfiguried() {
        return apiKey != null && !apiKey.isEmpty();
    }

    @PostMapping("/search")
    public String videoTags(@RequestParam("videoTitle") String videoTitle, Model model) {

        if (!isApiKeyConfiguried()) {
            model.addAttribute("error", "API key is not configured");
            return "home";
        }

        if (videoTitle == null || videoTitle.trim().isEmpty()) {
            model.addAttribute("error", "Video Title is required");
            return "home";
        }

        SearchVideo result = youTubeService.searchVideos(videoTitle);

        model.addAttribute("videoDetails", result.getPrimaryVideo());
        model.addAttribute("relatedVideos", result.getRelatedVideos());

        return "home";
    }
}