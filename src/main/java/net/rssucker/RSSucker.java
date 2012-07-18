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

    public List<Item> fetchNewItems(int season, int episode) {
        List<Item> items = new ArrayList<Item>();
        for(Item item:rssReader.getItems()){
            if(isAfterGivenEpisode(season, episode, item)){
                items.add(item);
            }
        }
        return items;

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
