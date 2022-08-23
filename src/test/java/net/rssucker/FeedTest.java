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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;


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
		assertThat(feed.getItemCount()).isEqualTo(4);
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
			assertThat(urls).containsOnlyOnce(item.getUrl());
			assertThat(seen).doesNotContain(item.getUrl());
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
			assertThat(titles).containsOnlyOnce(item.getTitle());
			assertThat(seen).doesNotContain(item.getTitle());
			seen.add(item.getTitle());
		}
	}

	@Test
	public void canGetEpisodeOfAllStreamEntries() throws IOException {
		parseResource("full.xml");

		for (Item item : feed.getItems()) {
			assertThat(item.getEpisode()).isNotNull();
		}
	}

	@Test
	public void canGetQualityOfAllStreamEntries() throws IOException {
		parseResource("full.xml");

		for (Item item : feed.getItems()) {
			assertThat(item.getQuality()).isNotNull();
		}
	}

	@Test
	public void entriesAreSortedAscending() throws IOException {
		parseResource("full.xml");
		Item oldItem = null;
		for (Item item : feed.getItems()) {
			if (oldItem != null) {
				if (oldItem.getEpisode().getSeason() == item.getEpisode().getSeason()) {
					assertThat(item.getEpisode().getNumber()).isGreaterThanOrEqualTo(oldItem.getEpisode().getNumber());
				} else
					assertThat(item.getEpisode().getSeason()).isGreaterThanOrEqualTo(oldItem.getEpisode().getSeason());
			}
			oldItem = item;
		}
	}

	private void parseResource(String name) throws IOException {
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream(name);
		assumeThat(stream).isNotNull();
		feed.parse(new XmlReader(stream));
		assertThat(feed.getItemCount()).isGreaterThan(0);
	}
}
