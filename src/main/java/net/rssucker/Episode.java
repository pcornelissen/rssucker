package net.rssucker;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Episode implements Comparable<Episode> {
    private static final String PATTERN_AAxBB = ".*(?<season>\\d+)x(?<episode>\\d+).*";
    private static final String PATTERN_sAAeBB = ".*s(?<season>\\d+)e(?<episode>\\d+).*";
    private final int season;
    private final int episodeNumber;

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

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.season).append(this.episodeNumber).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Episode other = (Episode) obj;
        return new EqualsBuilder().append(this.season, other.season).append(this.episodeNumber, other.episodeNumber).isEquals();
    }

    @Override
    public String toString() {
        return "[S" + season + "E" + episodeNumber + ']';
    }

    public static Episode parseEpisode(String text) {
        List<String> patterns = Arrays.asList(
                PATTERN_AAxBB,
                PATTERN_sAAeBB);
        for (String pattern : patterns) {
            final Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            final Matcher m = p.matcher(text);
            if (m.find()) {
                return new Episode(Integer.valueOf(m.group("season")), Integer.valueOf(m.group("episode")));
            }
        }
        return null;

    }
}
