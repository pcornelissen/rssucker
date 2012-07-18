package net.rssucker;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private String feedAddress;
    private Episode lastEpisode = new Episode(0, 0);
    private List<FeedConfig> feeds = new ArrayList<FeedConfig>();

    public Config(File file) throws IOException {
        if (file.canRead()) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode parentNode = mapper.readTree(file);
            for (JsonNode node : parentNode) {
                Episode episode = new Episode(getLastSeasonFromNode(node), getLastEpisodeFromNode(node));
                String address = node.get("address").asText();
                FeedConfig feedConfig = new FeedConfig(address, episode);
                feeds.add(feedConfig);
            }
        }
    }

    private int getLastEpisodeFromNode(JsonNode node) {
        if (node.has("lastEpisode"))
            return node.get("lastEpisode").asInt(0);
        else
            return 0;
    }

    private int getLastSeasonFromNode(JsonNode node) {
        if (node.has("lastSeason"))
            return node.get("lastSeason").asInt(0);
        else
            return 0;
    }

    public int getFeedCount() {
        return feeds.size();
    }

    public List<FeedConfig> getFeeds() {
        return feeds;
    }

    public static class FeedConfig {

        private final String address;
        private final Episode episode;

        public FeedConfig(String address, Episode episode) {
            this.address = address;
            this.episode = episode;
        }

        public String getAddress() {
            return address;
        }

        public Episode getEpisode() {
            return episode;
        }
    }
}
