package net.rssucker;

/**
* Created with IntelliJ IDEA.
* User: pcornelissen
* Date: 19.07.12
* Time: 08:54
* To change this template use File | Settings | File Templates.
*/
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
