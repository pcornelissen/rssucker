package net.rssucker;


import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RSSuckerTest {

    @Test
    public void processFeedReturnsAllURLsForNewFeed(){
        RSSucker rsSucker = new RSSucker();
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("full.xml");
        rsSucker.addLocalFeed(stream);

        List<Item> items = rsSucker.fetchNewItems(0, 0, Quality.ANY);
        assertThat(items, is(not(nullValue()))) ;
        assertThat(items.size(), is(25)) ;
    }

    @Test
    public void processFeedReturnsAllURLsSinceEpisode6ForNewFeedWithQualityMedium(){
        RSSucker rsSucker = new RSSucker();
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("full.xml");
        rsSucker.addLocalFeed(stream);

        List<Item> items = rsSucker.fetchNewItems(3,6, Quality.MEDIUM);
        assertThat(items, is(not(nullValue()))) ;
        assertThat(items.size(), is(4)) ;
    }
}
