package net.rssucker;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemTest {
	@Test
	public void canDetectAXBBEpisodeSchema() {
		Item item = new Item("TestShow 3X06 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
		assertThat(item.getEpisode().getNumber()).isEqualTo(6);
		assertThat(item.getEpisode().getSeason()).isEqualTo(3);
	}

	@Test
	public void canDetectAxBBEpisodeSchema() {
		Item item = new Item("TestShow 3x06 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
		assertThat(item.getEpisode().getNumber()).isEqualTo(6);
		assertThat(item.getEpisode().getSeason()).isEqualTo(3);
	}

	@Test
	public void canDetectSAAEBBEpisodeSchema() {
		Item item = new Item("TestShow S03E06 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
		assertThat(item.getEpisode().getNumber()).isEqualTo(6);
		assertThat(item.getEpisode().getSeason()).isEqualTo(3);
	}

	@Test(expected = RuntimeException.class)
	public void throwExceptionWithoutEpisodeSchemaDetectable() {
		new Item("TestShow pilot (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.pilot.(HDTV-x264-ASAP).[providertag].file");
	}

	@Test
	public void canDetermineQualityHigh() {
		Item item = new Item("TestShow S03E06 (720p HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
		assertThat(item.getQuality()).isEqualTo(Quality.HIGH);

	}

	@Test
	public void canDetermineQualityMedium() {
		Item item = new Item("TestShow S03E06 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
		assertThat(item.getQuality()).isEqualTo(Quality.MEDIUM);

	}

	@Test
	public void comparatorDetectsBiggerSeason() {
		Item item1 = new Item("TestShow S05E06 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
		Item item2 = new Item("TestShow S04E06 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
		Item.ItemComparator itemComparator = new Item.ItemComparator();
		assertThat(itemComparator.compare(item1, item2)).isEqualTo(1);
	}

	@Test
	public void comparatorDetectsSmallerSeason() {
		Item item1 = new Item("TestShow S03E06 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
		Item item2 = new Item("TestShow S04E06 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
		Item.ItemComparator itemComparator = new Item.ItemComparator();
		assertThat(itemComparator.compare(item1, item2)).isEqualTo(-1);
	}

	@Test
	public void comparatorDetectsBiggerEpisode() {
		Item item1 = new Item("TestShow S04E07 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
		Item item2 = new Item("TestShow S04E06 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
		Item.ItemComparator itemComparator = new Item.ItemComparator();
		assertThat(itemComparator.compare(item1, item2)).isEqualTo(1);
	}

	@Test
	public void comparatorDetectsSmallerEpisode() {
		Item item1 = new Item("TestShow S04E05 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
		Item item2 = new Item("TestShow S04E06 (HDTV-x264-ASAP) [providertag]", "http://example.com/TestShow.3X06.(HDTV-x264-ASAP).[providertag].file");
		Item.ItemComparator itemComparator = new Item.ItemComparator();
		assertThat(itemComparator.compare(item1, item2)).isEqualTo(-1);
	}

}
