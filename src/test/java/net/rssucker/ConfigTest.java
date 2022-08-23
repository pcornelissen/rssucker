package net.rssucker;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static org.assertj.core.api.Assertions.assertThat;


public class ConfigTest {
	public static final String[] EMPTY_ARGS = new String[]{};
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
		Config config = new Config(new File("test"), EMPTY_ARGS);
		assertThat(config.getFeedCount()).isZero();
	}

	@Test
	public void verboseIsOffByDefault() throws IOException {
		Config config = new Config(new File("test"), EMPTY_ARGS);
		assertThat(config.isVerbose()).isFalse();
	}

	@Test
	public void verboseCanBeSetViaArgs() throws IOException {
		Config config = new Config(new File("oneFeed.json"), new String[]{"-v"});
		assertThat(config.isVerbose()).isTrue();
	}

	@Test
	public void verboseCanBeSetViaCorrectArgs() throws IOException {
		Config config = new Config(new File("oneFeed.json"), new String[]{"-V", "z", "v", "-w", "-ve", "-a"});
		assertThat(config.isVerbose()).isFalse();
	}

	@Test
	public void initConfigWithExistingFileWithOneFeed() throws IOException, URISyntaxException {
		File configFile = initConfigFile("oneFeed.json");
		Config config = new Config(configFile, EMPTY_ARGS);
		assertThat(config.getFeedCount()).isEqualTo(1);
	}

	@Test
	public void fetchAddressFromConfigFeed() throws IOException, URISyntaxException {
		File configFile = initConfigFile("oneFeed.json");
		Config config = new Config(configFile, EMPTY_ARGS);
		assertThat(config.getFeeds().get(0).getAddress()).isEqualTo("full.xml");
		assertThat(config.getFeeds().get(0).getEpisode()).isEqualTo(new Episode(3, 4));
	}

	@Test
	public void fetchAddressFromConfigFeedWithoutSeason() throws IOException, URISyntaxException {
		File configFile = initConfigFile("oneFeedWithoutSeason.json");
		Config config = new Config(configFile, EMPTY_ARGS);
		assertThat(config.getFeeds().get(0).getAddress()).isEqualTo("full.xml");
		assertThat(config.getFeeds().get(0).getEpisode()).isEqualTo(new Episode(0, 4));
	}

	@Test
	public void fetchAddressFromConfigFeedWithoutEpisode() throws IOException, URISyntaxException {
		File configFile = initConfigFile("oneFeedWithoutEpisode.json");
		Config config = new Config(configFile, EMPTY_ARGS);
		assertThat(config.getFeeds().get(0).getAddress()).isEqualTo("full.xml");
		assertThat(config.getFeeds().get(0).getEpisode()).isEqualTo(new Episode(3, 0));
	}

	@Test
	public void fetch2FeedsFromConfig() throws IOException, URISyntaxException {
		File configFile = initConfigFile("twoFeed.json");
		Config config = new Config(configFile, EMPTY_ARGS);
		assertThat(config.getFeedCount()).isEqualTo(2);
		assertThat(config.getFeeds().get(1).getAddress()).isEqualTo("full2.xml");
		assertThat(config.getFeeds().get(1).getEpisode()).isEqualTo(new Episode(32, 42));
	}

	@Test
	public void writeUpdatedEpisodeToConfig() throws IOException, URISyntaxException {
		File configFile = initConfigFile("twoFeed.json");
		Config config = new Config(configFile, EMPTY_ARGS);
		assertThat(config.getFeedCount()).isEqualTo(2);
		config.getFeeds().get(1).setEpisode(new Episode(23, 422));
		config.write();

		Config updatedConfig = new Config(configFile, EMPTY_ARGS);
		assertThat(updatedConfig.getFeedCount()).isEqualTo(2);
		assertThat(updatedConfig.getFeeds().get(1).getEpisode()).isEqualTo(new Episode(23, 422));
	}

	@Test
	public void writtenConfigContainsDownloadDir() throws IOException, URISyntaxException {
		File configFile = initConfigFile("twoFeed.json");

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(configFile);
		assertThat(rootNode.has("downloaddir")).isFalse();

		Config config = new Config(configFile, EMPTY_ARGS);
		config.write();

		rootNode = mapper.readTree(configFile);
		assertThat(rootNode.has("downloaddir")).isTrue();
	}
}
