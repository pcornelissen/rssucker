package net.rssucker;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class EpisodeTest {
    Episode episode;
    @Before
    public void setup(){
        episode = new Episode(21,42);
    }

    @Test
    public void testCompareToForEqualEpisode() {
        assertThat(episode.compareTo((Episode.parseEpisode("[S21E42]"))), is(0));
    }

    @Test
    public void testCompareToForEarlierEpisode() {
        assertThat(episode.compareTo((Episode.parseEpisode("[S21E41]"))), is(1));
    }

    @Test
    public void testCompareToForEarlierSeason() {
        assertThat(episode.compareTo((Episode.parseEpisode("[S20E43]"))), is(1));
    }

    @Test
    public void testCompareToForLaterSeason() {
        assertThat(episode.compareTo((Episode.parseEpisode("[S22E42]"))), is(-1));
    }

    @Test
    public void testCompareToForLaterEpisode() {
        assertThat(episode.compareTo((Episode.parseEpisode("[S21E43]"))), is(-1));
    }

    @Test
    public void testHashCodeForEqualEpisodes() {
        assertThat(episode.hashCode(), is(Episode.parseEpisode("[S21E42]").hashCode()));
    }
    @Test
    public void testHashCodeForUnEqualEpisodeSeason() {
        assertThat(episode.hashCode(), is(not(Episode.parseEpisode("[S20E42]").hashCode())));
    }
    @Test
    public void testHashCodeForEqualEpisodeNumber() {
        assertThat(episode.hashCode(), is(not(Episode.parseEpisode("[S21E43]").hashCode())));
    }

    @Test
    public void testEqualsForEqualEpisode() {
        assertThat(episode.equals(Episode.parseEpisode("[S21E42]")),is(true));
    }

    @Test
    public void testEqualsForUnEqualEpisodeSeason() {
        assertThat(episode.equals(Episode.parseEpisode("[S20E42]")),is(false));
    }

    @Test
    public void testEqualsForUnEqualEpisodeNumber() {
        assertThat(episode.equals(Episode.parseEpisode("[S21E41]")),is(false));
    }

    @Test
    public void testEqualsForNull() {
        assertThat(episode.equals(null),is(false));
    }

    @Test
    public void testEqualsForOtherObj() {
        assertThat(episode.equals("meepmeep"),is(false));
    }

    @Test
    public void testParseEpisodeSuccessAAxBBPattern() {
        episode = Episode.parseEpisode("blabla 21x42 blabla");
        assertThat(episode,is(notNullValue()));
        assertThat(episode.getNumber(),is(42));
        assertThat(episode.getSeason(),is(21));
    }

    @Test
    public void testParseEpisodeSuccessSAAEBBPattern() {
        episode = Episode.parseEpisode("blabla S21E42 blabla");
        assertThat(episode,is(notNullValue()));
        assertThat(episode.getNumber(),is(42));
        assertThat(episode.getSeason(),is(21));
    }

    @Test
    public void testToString() {
        assertThat(episode.toString(),is("[S21E42]"));
    }

    @Test
    public void testIsAfterGivenEpisode() {

    }

    @Test
    public void testIsAfterGivenEpisodeForEqualEpisode() {
        assertThat(episode.isAfterGivenEpisode(Episode.parseEpisode("[S21E42]")), is(false));
    }

    @Test
    public void testIsAfterGivenEpisodeForEarlierEpisode() {
        assertThat(episode.isAfterGivenEpisode(Episode.parseEpisode("[S21E41]")), is(true));
    }

    @Test
    public void testCIsAfterGivenEpisodeForEarlierSeason() {
        assertThat(episode.isAfterGivenEpisode(Episode.parseEpisode("[S20E42]")), is(true));
    }

    @Test
    public void testIsAfterGivenEpisodeForLaterSeason() {
        assertThat(episode.isAfterGivenEpisode(Episode.parseEpisode("[S22E42]")), is(false));
    }

    @Test
    public void testIsAfterGivenEpisodeForLaterEpisode() {
        assertThat(episode.isAfterGivenEpisode(Episode.parseEpisode("[S21E43]")), is(false));
    }

    @Test
    public void testImplementsComparable() {
        assertThat(episode instanceof  Comparable, is(true));
    }

}
