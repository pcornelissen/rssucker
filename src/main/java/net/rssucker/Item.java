package net.rssucker;


import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Item {
    private String url;
    private String title;
    private Episode episode=null;
    private Quality quality=null;

    public Item(String title, String url) {
        this.title = title;
        this.url = url;
        if(!extractAAxBBSchema(title) && ! extractsAAeBBSchema(title)){
            throw new RuntimeException("No Episode/season found");
        }
        extractQuality(title);

    }

    private boolean extractAAxBBSchema(String title) {
        final Pattern p = Pattern.compile(".*(?<season>\\d+)x(?<episode>\\d+).*",Pattern.CASE_INSENSITIVE);
        final Matcher m = p.matcher(title);
        if(m.find()){
            episode = new Episode(Integer.valueOf(m.group("season")),Integer.valueOf(m.group("episode")));
            return true;
        }
        return false;
    }
    private boolean extractsAAeBBSchema(String title) {
        final Pattern p = Pattern.compile(".*s(?<season>\\d+)e(?<episode>\\d+).*",Pattern.CASE_INSENSITIVE);
        final Matcher m = p.matcher(title);
        if(m.find()){
            episode = new Episode(Integer.valueOf(m.group("season")),Integer.valueOf(m.group("episode")));
            return true;
        }
        return false;
    }

    private void extractQuality(String title) {
        for(Quality quality:Quality.values()){
            for(String key:quality.getPossibleKeys()){
                if(title.toLowerCase().contains(key.toLowerCase())){
                    this.quality=quality;
                    return;
                }
            }
        }
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

    public static class ItemComparator implements Comparator<Item>{
        @Override
        public int compare(Item o1, Item o2) {
            return o1.getEpisode().compareTo(o2.getEpisode());
        }
    }
}
