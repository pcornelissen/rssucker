package net.rssucker;


import java.util.Comparator;

public class Item {
    private String url;
    private String title;
    private Episode episode = null;
    private Quality quality = null;

    public Item(String title, String url) {
        this.title = title;
        this.url = url;
        this.episode = Episode.parseEpisode(title);
        if (episode == null) {
            throw new RuntimeException("No Episode/season found");
        }
        this.quality = Quality.parseFromString(title);
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public Episode getEpisode() {
        return episode;
    }


    public Quality getQuality() {
        return quality;
    }

    public static class ItemComparator implements Comparator<Item> {
        @Override
        public int compare(Item o1, Item o2) {
            return o1.getEpisode().compareTo(o2.getEpisode());
        }
    }
}
