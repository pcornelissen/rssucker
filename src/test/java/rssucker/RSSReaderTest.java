package rssucker;


import net.rssucker.InvalidFeedException;
import net.rssucker.RSSReader;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.junit.matchers.JUnitMatchers.*;

public class RSSReaderTest {
    RSSReader reader;

    @Before
    public void setUp() {
        reader = new RSSReader();
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("valid.xml");
        assumeThat(stream, is(not(nullValue())));

        reader.parse(stream);
    }

    @Test(expected = InvalidFeedException.class)
    public void openRSSFileFromStreamFail() {
        reader = new RSSReader();
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("invalid.xml");
        assumeThat(stream, is(not(nullValue())));

        reader.parse(stream);
    }

    @Test
    public void canReadNumberOfItems() {
        assertThat(reader.getItemCount(), is(4));
    }

    @Test
    public void canGetURLsOfStreamEntries() {
        List<String> urls = Arrays.asList("http://example.com/123.S08E24.720p.HDTV.X264.[testtest].zip",
                "http://example.com/1.zip",
                "http://example.com/12.zip",
                "http://example.com/123.zip");
        Set<String> seen = new HashSet<String>(urls.size());
        for(RSSReader.Item item : reader.getItems()){
            assertThat(urls, hasItem(item.getUrl()));
            assertThat(seen, not(hasItem(item.getUrl())));
            seen.add(item.getUrl());
        }
    }
}
