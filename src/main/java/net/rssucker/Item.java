package net.rssucker;


import java.util.Comparator;

class Item {
	private final String url;
	private final String title;
	private Episode episode = null;
	private Quality quality = null;

	public Item(String title, String url) {
		this.title = title;
		this.url = url;
		this.episode = Episode.parseEpisode(title);
		if (episode == null) {
			throw new RuntimeException("No Episode/season found");
		}
		this.quality = Quality.parseFromString(title);
	}

	public String getUrl() {
		return url;
	}

	public String getTitle() {
		return title;
	}

	public Episode getEpisode() {
		return episode;
	}


	public Quality getQuality() {
		return quality;
	}

	public boolean isCorrectQuality(Quality quality) {
		return (getQuality() == quality || quality == Quality.ANY);
	}

	boolean passesCriteria(FeedConfig config) {
		Quality quality = config.getQuality();
		return getEpisode().isAfterGivenEpisode(config.getEpisode())
				&& isCorrectQuality(quality);
	}

	public static class ItemComparator implements Comparator<Item> {
		@Override
		public int compare(Item o1, Item o2) {
			if(o1.getEpisode().equals(o2.getEpisode())){
				return o1.getQuality().compareTo(o2.getQuality());
			}
			return o1.getEpisode().compareTo(o2.getEpisode());
		}
	}

	@Override
	public String toString() {
		return "Item{" +
				"title='" + title + '\'' +
				", url='" + url + '\'' +
				", episode=" + episode +
				", quality=" + quality +
				'}';
	}
}
