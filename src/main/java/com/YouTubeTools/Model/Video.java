package com.YouTubeTools.Model;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Video {

    private String id;
    private String title;
    private String channelTitle;
    private String description;
    private String thumbnailUrl;
    private String publishedAt;
    private List<String> tags;
}