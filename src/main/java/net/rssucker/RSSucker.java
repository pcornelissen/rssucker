package net.rssucker;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RSSucker {

    private RSSReader rssReader;

    public void addLocalFeed(InputStream inputStream) {

        rssReader = new RSSReader();
        rssReader.parse(inputStream);
    }

    public List<Item> fetchNewItems(int season, int episode, Quality quality) {
        List<Item> items = new ArrayList<Item>();
        for(Item item:rssReader.getItems()){
            if(passesCriteria(season, episode, quality, item)){
                items.add(item);
            }
        }
        return items;

    }

    private boolean passesCriteria(int season, int episode, Quality quality, Item item) {
        return isAfterGivenEpisode(season, episode, item)
                && isCorrectQuality(quality, item);
    }

    private boolean isCorrectQuality(Quality quality, Item item) {
        return (item.getQuality()==quality || quality== Quality.ANY);
    }

    private boolean isAfterGivenEpisode(int season, int episode, Item item) {
        return isLaterSeason(season, item) || laterEpisodeInSeason(season, episode, item);
    }

    private boolean laterEpisodeInSeason(int season, int episode, Item item) {
        return (item.getSeason()==season && item.getEpisode()>episode);
    }

    private boolean isLaterSeason(int season, Item item) {
        return item.getSeason()>season;
    }
}
