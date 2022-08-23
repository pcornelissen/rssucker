package net.rssucker;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class Config {
    public static final String LAST_EPISODE = "lastEpisode";
    public static final String LAST_SEASON = "lastSeason";
    private final List<FeedConfig> feeds = new ArrayList<>();
    private final String configName;
    private String downloadLocation;
    private boolean verbose = false;

    public Config(File file, String[] args) throws IOException {
        parseArgs(args);
        configName = file.getAbsolutePath();
        if (verbose) System.out.println("Using Config: " + configName);
        if (file.canRead()) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(file);
            if (rootNode.has("downloaddir")) {
                downloadLocation = rootNode.get("downloaddir").asText();
            } else {
                downloadLocation = System.getProperty("user.home") + File.separatorChar + "rssucker";
            }
            if (verbose) System.out.println("DownloadDir: " + downloadLocation);
            Iterator<Map.Entry<String, JsonNode>> iterator = rootNode.fields();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> elt = iterator.next();
                if ("feeds".equals(elt.getKey().toLowerCase())) {
                    parseFeeds(elt.getValue());
                }
            }
        }
    }

    private void parseArgs(String[] args) {
        for (String arg : args) {
            System.out.println("ARG: " + arg);
            if ("-v".equals(arg)) setVerbose(true);
        }
    }

    private void parseFeeds(JsonNode feedsNode) {
        Iterator<Map.Entry<String, JsonNode>> iterator = feedsNode.fields();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> elt = iterator.next();
            if (verbose) System.out.println("FeedName: " + elt.getKey());

            JsonNode node = elt.getValue();
            Episode episode = new Episode(getLastSeasonFromNode(node), getLastEpisodeFromNode(node));
            if (verbose) System.out.println("LastEpisode: " + episode);
            String address = node.get("address").asText();
            String name = elt.getKey();
            FeedConfig feedConfig = new FeedConfig(address, name, episode, Quality.MEDIUM);
            feeds.add(feedConfig);
        }
    }


    public Config(String[] args) throws IOException {
        this(new File(System.getProperty("user.home") + File.separatorChar + ".rssucker"), args);
    }

    private int getLastEpisodeFromNode(JsonNode node) {
        if (node.has(LAST_EPISODE))
            return node.get(LAST_EPISODE).asInt(0);
        else
            return 0;
    }

    private int getLastSeasonFromNode(JsonNode node) {
        if (node.has(LAST_SEASON))
            return node.get(LAST_SEASON).asInt(0);
        else
            return 0;
    }

    public int getFeedCount() {
        return feeds.size();
    }

    public List<FeedConfig> getFeeds() {
        return feeds;
    }

    public String getDownloadLocation() {
        return downloadLocation;
    }

    public void write() throws IOException {
        File file = new File(configName);
        if(file.delete()) System.out.println("Replacing old config");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode parentNode = mapper.createObjectNode();
        feedsToNodes(parentNode.putObject("feeds"));
        parentNode.put("downloaddir", downloadLocation);

        String data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parentNode);

        writeStringToFile(file, data);
    }

    private void feedsToNodes(ObjectNode parentNode) {
        for (FeedConfig feed : feeds) {
            ObjectNode feedNode = parentNode.putObject(feed.getName());
            feedNode.put("address", feed.getAddress());
            feedNode.put(LAST_EPISODE, feed.getEpisode().getNumber());
            feedNode.put(LAST_SEASON, feed.getEpisode().getSeason());
        }
    }

    private void writeStringToFile(File file, String data) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file, true);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                     fileOutputStream, 128 * 100)) {

            bufferedOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
            bufferedOutputStream.flush();
        }

    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
}
