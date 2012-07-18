package net.rssucker;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class ConfigTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private File initConfigFile(String feedName) throws IOException, URISyntaxException {
        testFolder.create();
        File configFile = testFolder.newFile();
        URL resource = this.getClass().getClassLoader().getResource(feedName);
        File template = new File(resource.toURI());
        Files.copy(template.toPath(), configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return configFile;
    }

    @Test
    public void initConfigWithMissingFile() throws IOException {
        Config config = new Config(new File("test"));
    }

    @Test
    public void initConfigWithExistingFileWithOneFeed() throws IOException, URISyntaxException {
        File configFile = initConfigFile("oneFeed.json");
        Config config = new Config(configFile);
        assertThat(config.getFeedCount(),is(1));
    }

    @Test
    public void fetchAddressFromConfigFeed() throws IOException, URISyntaxException {
        File configFile = initConfigFile("oneFeed.json");
        Config config = new Config(configFile);
        assertThat(config.getFeeds().get(0).getAddress(),is("full.xml"));
        assertThat(config.getFeeds().get(0).getEpisode(),is(new Episode(3,4)));
    }
    @Test
    public void fetchAddressFromConfigFeedWithoutSeason() throws IOException, URISyntaxException {
        File configFile = initConfigFile("oneFeedWithoutSeason.json");
        Config config = new Config(configFile);
        assertThat(config.getFeeds().get(0).getAddress(),is("full.xml"));
        assertThat(config.getFeeds().get(0).getEpisode(),is(new Episode(0,4)));
    }
    @Test
    public void fetchAddressFromConfigFeedWithoutEpisode() throws IOException, URISyntaxException {
        File configFile = initConfigFile("oneFeedWithoutEpisode.json");
        Config config = new Config(configFile);
        assertThat(config.getFeeds().get(0).getAddress(),is("full.xml"));
        assertThat(config.getFeeds().get(0).getEpisode(),is(new Episode(3,0)));
    }
}
