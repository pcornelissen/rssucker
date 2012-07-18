package net.rssucker;

import java.util.Arrays;
import java.util.List;

public enum Quality {
    HIGH("720p HDTV"),MEDIUM("HDTV"), ANY();

    private List<String> possibleKeys;

    private Quality(String... keys) {
        possibleKeys= Arrays.asList(keys);
    }

    public List<String> getPossibleKeys() {
        return possibleKeys;
    }
}
