package net.rssucker;


public class Episode implements Comparable<Episode> {
    private int season;
    private int episodeNumber;

    public Episode(int season, int episodeNumber) {
        this.season = season;
        this.episodeNumber = episodeNumber;
    }

    public int getSeason() {
        return season;
    }

    public int getNumber() {
        return episodeNumber;
    }

    @Override
    public int compareTo(Episode o) {
        if (getSeason() > o.getSeason()) return 1;
        if (getSeason() < o.getSeason()) return -1;
        if (getNumber() > o.getNumber()) return 1;
        if (getNumber() < o.getNumber()) return -1;
        return 0;
    }
}
