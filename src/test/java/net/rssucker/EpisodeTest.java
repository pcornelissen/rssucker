package net.rssucker;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class EpisodeTest {
	private Episode episode;

	@Before
	public void setup() {
		episode = new Episode(21, 42);
	}

	@Test
	public void testCompareToForEqualEpisode() {
		assertThat(episode).isEqualByComparingTo(Episode.parseEpisode("[S21E42]"));
	}

	@Test
	public void testCompareToForEarlierEpisode() {
		assertThat(episode.compareTo(Episode.parseEpisode("[S21E41]"))).isEqualTo(1);
	}

	@Test
	public void testCompareToForEarlierSeason() {
		assertThat(episode.compareTo(Episode.parseEpisode("[S20E43]"))).isEqualTo(1);
	}

	@Test
	public void testCompareToForLaterSeason() {
		assertThat(episode.compareTo(Episode.parseEpisode("[S22E42]"))).isEqualTo(-1);
	}

	@Test
	public void testCompareToForLaterEpisode() {
		assertThat(episode.compareTo(Episode.parseEpisode("[S21E43]"))).isEqualTo(-1);
	}

	@Test
	public void testHashCodeForEqualEpisodes() {
		assertThat(episode).hasSameHashCodeAs(Episode.parseEpisode("[S21E42]"));
	}

	@Test
	public void testHashCodeForUnEqualEpisodeSeason() {
		assertThat(episode).doesNotHaveSameHashCodeAs(Episode.parseEpisode("[S20E42]"));
	}

	@Test
	public void testHashCodeForEqualEpisodeNumber() {
		assertThat(episode).doesNotHaveSameHashCodeAs(Episode.parseEpisode("[S21E43]"));
	}

	@Test
	public void testEqualsForEqualEpisode() {
		assertThat(episode).isEqualTo(Episode.parseEpisode("[S21E42]"));
	}

	@Test
	public void testEqualsForUnEqualEpisodeSeason() {
		assertThat(episode.equals(Episode.parseEpisode("[S20E42]"))).isFalse();
	}

	@Test
	public void testEqualsForUnEqualEpisodeNumber() {
		assertThat(episode.equals(Episode.parseEpisode("[S21E41]"))).isFalse();
	}

	@SuppressWarnings("ObjectEqualsNull")
	@Test
	public void testEqualsForNull() {
		assertThat(episode.equals(null)).isFalse();
	}

	@SuppressWarnings("EqualsBetweenInconvertibleTypes")
	@Test
	public void testEqualsForOtherObj() {
		assertThat(episode.equals("meepmeep")).isFalse();
	}

	@Test
	public void testParseEpisodeSuccessAAxBBPattern() {
		episode = Episode.parseEpisode("blabla 21x42 blabla");
		assertThat(episode).isNotNull();
		assertThat(episode.getNumber()).isEqualTo(42);
		assertThat(episode.getSeason()).isEqualTo(21);
	}

	@Test
	public void testParseEpisodeSuccessHashBBPattern() {
		episode = Episode.parseEpisode("blabla #42 blabla");
		assertThat(episode).isNotNull();
		assertThat(episode.getNumber()).isEqualTo(42);
		assertThat(episode.getSeason()).isZero();
	}

	@Test
	public void testParseEpisodeSuccessSAAEBBPattern() {
		episode = Episode.parseEpisode("blabla S21E42 blabla");
		assertThat(episode).isNotNull();
		assertThat(episode.getNumber()).isEqualTo(42);
		assertThat(episode.getSeason()).isEqualTo(21);
	}

	@Test
	public void testToString() {
		assertThat(episode).hasToString("[S21E42]");
	}

	@Test
	public void testIsAfterGivenEpisodeForEqualEpisode() {
		assertThat(episode.isAfterGivenEpisode(Episode.parseEpisode("[S21E42]"))).isFalse();
	}

	@Test
	public void testIsAfterGivenEpisodeForEarlierEpisode() {
		assertThat(episode.isAfterGivenEpisode(Episode.parseEpisode("[S21E41]"))).isTrue();
	}

	@Test
	public void testCIsAfterGivenEpisodeForEarlierSeason() {
		assertThat(episode.isAfterGivenEpisode(Episode.parseEpisode("[S20E42]"))).isTrue();
	}

	@Test
	public void testIsAfterGivenEpisodeForLaterSeason() {
		assertThat(episode.isAfterGivenEpisode(Episode.parseEpisode("[S22E42]"))).isFalse();
	}

	@Test
	public void testIsAfterGivenEpisodeForLaterEpisode() {
		assertThat(episode.isAfterGivenEpisode(Episode.parseEpisode("[S21E43]"))).isFalse();
	}

	@Test
	public void testImplementsComparable() {
		assertThat(episode).isInstanceOf(Comparable.class);
	}

}
