package com.YouTubeTools.Service;



import com.YouTubeTools.Model.SearchVideo;
import com.YouTubeTools.Model.Video;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class YouTubeService {

    @Value("${youtube.api.key}")
    private String apiKey;

    private static final String SEARCH_URL =
            "https://www.googleapis.com/youtube/v3/search";
    private static final String VIDEOS_URL =
            "https://www.googleapis.com/youtube/v3/videos";

    private final RestTemplate restTemplate = new RestTemplate();

    public SearchVideo searchVideos(String title) {

        // 1Search videos
        String searchApi =
                SEARCH_URL +
                        "?part=snippet&type=video&maxResults=5&q=" + title +
                        "&key=" + apiKey;

        Map searchResponse =
                restTemplate.getForObject(searchApi, Map.class);

        List<Map> items = (List<Map>) searchResponse.get("items");

        List<String> videoIds = new ArrayList<>();

        for (Map item : items) {
            Map id = (Map) item.get("id");
            videoIds.add((String) id.get("videoId"));
        }

        // 2Fetch video details (for TAGS)
        String videosApi =
                VIDEOS_URL +
                        "?part=snippet&id=" + String.join(",", videoIds) +
                        "&key=" + apiKey;

        Map videoResponse =
                restTemplate.getForObject(videosApi, Map.class);

        List<Map> videoItems = (List<Map>) videoResponse.get("items");


        Map first = videoItems.get(0);
        Map snippet = (Map) first.get("snippet");

        Video primaryVideo = Video.builder()
                .title((String) snippet.get("title"))
                .channelTitle((String) snippet.get("channelTitle"))
                .tags((List<String>) snippet.getOrDefault("tags", List.of()))
                .build();

        // 4Related videos
        List<Video> relatedVideos = new ArrayList<>();

        for (int i = 1; i < videoItems.size(); i++) {
            Map snip = (Map) videoItems.get(i).get("snippet");

            relatedVideos.add(Video.builder()
                    .title((String) snip.get("title"))
                    .tags((List<String>) snip.getOrDefault("tags", List.of()))
                    .build());
        }

        return SearchVideo.builder()
                .primaryVideo(primaryVideo)
                .relatedVideos(relatedVideos)
                .build();
    }
    public Video getVideoDetails(String videoId) {

        String api =
                VIDEOS_URL +
                        "?part=snippet&id=" + videoId +
                        "&key=" + apiKey;

        Map response = restTemplate.getForObject(api, Map.class);
        List<Map> items = (List<Map>) response.get("items");

        if (items == null || items.isEmpty()) return null;

        Map snippet = (Map) items.get(0).get("snippet");

        List<String> tags = new ArrayList<>();
        Object tagObj = snippet.get("tags");
        if (tagObj instanceof List<?>) {
            for (Object t : (List<?>) tagObj) {
                tags.add(String.valueOf(t));
            }
        }

        return Video.builder()
                .title((String) snippet.get("title"))
                .channelTitle((String) snippet.get("channelTitle"))
                .description((String) snippet.get("description"))
                .thumbnailUrl(
                        ((Map)((Map)snippet.get("thumbnails"))
                                .get("high")).get("url").toString()
                )
                .tags(tags)
                .build();
    }
}