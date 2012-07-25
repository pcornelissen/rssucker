package net.rssucker;


class FeedConfig {

    private final String address;
    private final String name;
    private Episode episode;
    private final Quality quality;

    public FeedConfig(String address, String name) {
        this(address,name,new Episode(0,0), Quality.MEDIUM);
    }

    public FeedConfig(String address, String name, Episode episode, Quality quality) {
        this.address = address;
        this.name = name;
        this.episode = episode;
        this.quality = quality;
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

    public Quality getQuality() {
        return quality;
    }
}
