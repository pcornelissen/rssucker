package net.rssucker;


import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class RSSReader {

    private SyndFeed feed;
    private List<Item> items = null;

    public void parse(InputStream stream) {
        SyndFeedInput input = new SyndFeedInput();
        try {
            feed = input.build(new XmlReader(stream));
        } catch (FeedException e) {
            throw new InvalidFeedException(e);
        } catch (IOException e) {
            throw new InvalidFeedException(e);
        }
    }

    public Integer getItemCount() {
        return feed.getEntries().size();
    }

    public List<Item> getItems() {
        if (items == null) {
            items = new ArrayList<Item>(getItemCount());
            List<SyndEntry> entries = feed.getEntries();
            for (SyndEntry entry : entries) {
                items.add(new Item(StringEscapeUtils.unescapeHtml4(entry.getTitle()), entry.getLink()));
            }
            Collections.sort((List<Item>) items, new Item.ItemComparator());
        }
        return items;
    }

}
