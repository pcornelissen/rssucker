package net.rssucker;


import com.sun.syndication.io.XmlReader;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.junit.matchers.JUnitMatchers.hasItem;


public class FeedTest {
	private Feed feed;
	private FeedConfig config;

	@Before
	public void setUp() throws IOException {
		config = new FeedConfig("test", "name");
		feed = new Feed(config);
		parseResource("full.xml");
	}

	@Test(expected = InvalidFeedException.class)
	public void openRSSFileFromStreamFail() throws IOException {
		feed = new Feed(config);
		parseResource("invalid.xml");
	}

	@Test
	public void canReadNumberOfItems() throws IOException {
		parseResource("valid.xml");
		assertThat(feed.getItemCount(), is(4));
	}

	@Test
	public void canGetURLsOfStreamEntriesWithEnclosure() throws IOException {
		parseResource("valid.xml");
		List<String> urls = Arrays.asList("http://example.com/123.S08E24.720p.HDTV.X264.[testtest].zip",
				"http://media.railscasts.com/assets/episodes/videos/366-sidekiq.mp4",
				"http://example.com/12.zip",
				"http://example.com/123.zip");
		Set<String> seen = new HashSet<>(urls.size());
		for (Item item : feed.getItems()) {
			assertThat(urls, hasItem(item.getUrl()));
			assertThat(seen, not(hasItem(item.getUrl())));
			seen.add(item.getUrl());
		}
	}

	@Test
	public void canGetTitlesOfStreamEntries() throws IOException {
		parseResource("valid.xml");

		List<String> titles = Arrays.asList("Title with äöüß&/$%/ special chars 1x1",
				"Title1 with normal chars 1x1",
				"Title2 with normal chars 1x1",
				"Title3 with normal chars 1x1");
		Set<String> seen = new HashSet<>(titles.size());
		for (Item item : feed.getItems()) {
			assertThat(titles, hasItem(item.getTitle()));
			assertThat(seen, not(hasItem(item.getTitle())));
			seen.add(item.getTitle());
		}
	}

	@Test
	public void canGetEpisodeOfAllStreamEntries() throws IOException {
		parseResource("full.xml");

		for (Item item : feed.getItems()) {
			assertThat(item.getEpisode(), is(notNullValue()));
		}
	}

	@Test
	public void canGetQualityOfAllStreamEntries() throws IOException {
		parseResource("full.xml");

		for (Item item : feed.getItems()) {
			assertThat(item.getQuality(), is(notNullValue()));
		}
	}

	@Test
	public void entriesAreSortedAscending() throws IOException {
		parseResource("full.xml");
		Item oldItem = null;
		for (Item item : feed.getItems()) {
			if (oldItem != null) {
				if (oldItem.getEpisode().getSeason() == item.getEpisode().getSeason()) {
					assertThat(item.getEpisode().getNumber(), is(greaterThanOrEqualTo(oldItem.getEpisode().getNumber())));
				} else
					assertThat(item.getEpisode().getSeason(), is(greaterThanOrEqualTo(oldItem.getEpisode().getSeason())));
			}
			oldItem = item;
		}
	}

	private void parseResource(String name) throws IOException {
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream(name);
		assumeThat(stream, is(not(nullValue())));
		feed.parse(new XmlReader(stream));
		assertThat(feed.getItemCount(), is(greaterThan(0)));
	}
}
