package net.rssucker;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ItemTest {
    @Test
    public void canDetectAXBBEpisodeSchema() {
        Item item = new Item("TestShow 3X06 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
        assertThat(item.getEpisode(), is(6));
        assertThat(item.getSeason(), is(3));
    }

    @Test
    public void canDetectAxBBEpisodeSchema() {
        Item item = new Item("TestShow 3x06 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
        assertThat(item.getEpisode(), is(6));
        assertThat(item.getSeason(), is(3));
    }

    @Test
    public void canDetectSAAEBBEpisodeSchema() {
        Item item = new Item("TestShow S03E06 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
        assertThat(item.getEpisode(), is(6));
        assertThat(item.getSeason(), is(3));
    }

    @Test
    public void canDetermineQualityHigh(){
        Item item = new Item("TestShow S03E06 (720p HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
        assertThat(item.getQuality(), is(Quality.HIGH));

    }

    @Test
    public void canDetermineQualityMedium(){
        Item item = new Item("TestShow S03E06 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
        assertThat(item.getQuality(), is(Quality.MEDIUM));

    }

    @Test
    public void comparatorDetectsBiggerSeason(){
        Item item1 = new Item("TestShow S05E06 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
        Item item2 = new Item("TestShow S04E06 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
        Item.ItemComparator itemComparator = new Item.ItemComparator();
        assertThat(itemComparator.compare(item1,item2),is(1));
    }
    @Test
    public void comparatorDetectsSmallerSeason(){
        Item item1 = new Item("TestShow S03E06 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
        Item item2 = new Item("TestShow S04E06 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
        Item.ItemComparator itemComparator = new Item.ItemComparator();
        assertThat(itemComparator.compare(item1,item2),is(-1));
    }
    @Test
    public void comparatorDetectsBiggerEpisode(){
        Item item1 = new Item("TestShow S04E07 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
        Item item2 = new Item("TestShow S04E06 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
        Item.ItemComparator itemComparator = new Item.ItemComparator();
        assertThat(itemComparator.compare(item1,item2),is(1));
    }
    @Test
    public void comparatorDetectsSmallerEpisode(){
        Item item1 = new Item("TestShow S04E05 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
        Item item2 = new Item("TestShow S04E06 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
        Item.ItemComparator itemComparator = new Item.ItemComparator();
        assertThat(itemComparator.compare(item1,item2),is(-1));
    }

}
