package net.rssucker;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class Config {
    private final List<FeedConfig> feeds = new ArrayList<FeedConfig>();
    private final String configName;

    public Config(File file) throws IOException {
        if (file.canRead()) {
            configName = file.getAbsolutePath();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(file);
            Iterator<Map.Entry<String,JsonNode>> iterator = rootNode.getFields();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> elt = iterator.next();
                JsonNode node = elt.getValue();
                Episode episode = new Episode(getLastSeasonFromNode(node), getLastEpisodeFromNode(node));
                String address = node.get("address").asText();
                String name = elt.getKey();
                FeedConfig feedConfig = new FeedConfig(address, name, episode);
                feeds.add(feedConfig);
            }
        } else {
            configName = System.getProperty("user.home") + File.pathSeparatorChar + ".rssucker";
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

    public void write() throws IOException {
        File file = new File(configName);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode parentNode = mapper.createObjectNode();
        for (FeedConfig feed : feeds) {
            ObjectNode feedNode = parentNode.putObject(feed.getName());
            feedNode.put("address", feed.getAddress());
            feedNode.put("lastEpisode", feed.getEpisode().getNumber());
            feedNode.put("lastSeason", feed.getEpisode().getSeason());
        }
        String data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parentNode);

        writeStringToFile(file, data);
    }

    private void writeStringToFile(File file, String data) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file, true);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                fileOutputStream, 128 * 100);

        bufferedOutputStream.write(data.getBytes(Charset
                .forName("UTF-8")));
        bufferedOutputStream.flush();
        fileOutputStream.close();

    }

}
