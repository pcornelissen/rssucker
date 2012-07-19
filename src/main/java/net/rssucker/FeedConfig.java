package net.rssucker;


public class FeedConfig {

    private final String address;
    private final String name;
    private Episode episode;

    public FeedConfig(String address, String name, Episode episode) {
        this.address = address;
        this.name = name;
        this.episode = episode;
    }

    public String getAddress() {
        return address;
    }

    public Episode getEpisode() {
        return episode;
    }

    public void setEpisode(Episode episode) {
        this.episode = episode;
    }

    public String getName() {
        return name;
    }
}
