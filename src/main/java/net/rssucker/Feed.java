package net.rssucker;


import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Feed {

    private final FeedConfig config;

    private SyndFeed feed;
    private List<Item> items = null;

    Feed(FeedConfig config) {
        this.config = config;
    }

    public void parse(XmlReader reader) {
        SyndFeedInput input = new SyndFeedInput();
        try {
            feed = input.build(reader);
        } catch (FeedException e) {
            throw new InvalidFeedException(e);
        }
    }

    public Integer getItemCount() {
        return feed.getEntries().size();
    }

    public List<Item> getItems() {
        if (items == null) {
            items = new ArrayList<>(getItemCount());
            @SuppressWarnings("unchecked") List<SyndEntry> entries = feed.getEntries();
            for (SyndEntry entry : entries) {
                items.add(new Item(StringEscapeUtils.unescapeHtml4(entry.getTitle()), entry.getLink()));
            }
            Collections.sort((List<Item>) items, new Item.ItemComparator());
        }
        return items;
    }

    public FeedConfig getConfig() {
        return config;
    }

    private boolean passesCriteria(Item item) {
        return item.passesCriteria(getConfig());
    }

    public List<Item> getNewItems() {
        List<Item> newItems = new ArrayList<>();
        for (Item item : getItems()) {
            if (passesCriteria(item)) {
                newItems.add(item);
            }
        }
        return newItems;
    }
}
