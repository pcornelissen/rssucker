package net.rssucker;

import java.util.ArrayList;
import java.util.List;

class FeedFactory  {
    public List<Feed> createFrom(Config config) {
        List<Feed> feeds = new ArrayList<>();
        for (FeedConfig feedConfig : config.getFeeds()) {
            feeds.add(new Feed(feedConfig));
        }
        return feeds;
    }
}
