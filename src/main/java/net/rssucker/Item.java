package net.rssucker;


public class Item {
    private String url;
    private String title;

    public Item(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public Item() {
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }
}
