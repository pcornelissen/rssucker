package net.rssucker;


import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Feed provides access to the items of an rss feed which has been fetched
 * and provided via the parse method. After calling "parse" you can find the
 * items sorted by episode/season via getItems.
 */
class Feed {

	private final FeedConfig config;
	private SyndFeed feed;
	private List<Item> items = new ArrayList<>();

	/**
	 * Initializes the feed and saves the config for later use.
	 *
	 * @param config
	 */
	Feed(FeedConfig config) {
		this.config = config;
	}

	/**
	 * Parses the given XML Stream as RSS Feed and extracts the
	 * necessary information. The result can be fetched via
	 * {@link net.rssucker.Feed#getItems()} afterwards.
	 *
	 * @param reader
	 */
	public void parse(XmlReader reader) {
		SyndFeedInput input = new SyndFeedInput();
		try {
			feed = input.build(reader);
			items = new ArrayList<>(getItemCount());
			@SuppressWarnings("unchecked") List<SyndEntry> entries = feed.getEntries();
			for (SyndEntry entry : entries) {
				String link;
				link = findEnclosureUrl(entry);
				if (link == null) link = entry.getLink();
				items.add(new Item(StringEscapeUtils.unescapeHtml4(entry.getTitle()), link));
			}
			Collections.sort((List<Item>) items, new Item.ItemComparator());
		} catch (FeedException e) {
			throw new InvalidFeedException(e);
		}
	}

	public Integer getItemCount() {
		return items.size();
	}

	public List<Item> getItems() {
		return items;
	}

	private String findEnclosureUrl(SyndEntry entry) {
		if (entry.getEnclosures() != null) {
			List<SyndEnclosure> enclosures = entry.getEnclosures();
			for (SyndEnclosure enclosure : enclosures) {
				if (enclosure.getUrl() != null) {
					return enclosure.getUrl();
				}
			}

		}
		return null;
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
