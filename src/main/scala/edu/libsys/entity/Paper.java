package edu.libsys.entity;

/**
 * Created by spark on 3/11/17.
 */

public class Paper {
    private String id;
    private String title;
    private String searchWord;
    private String source;
    private String url;
    private String intro;
    private int likeCount;
    private int disLikeCount;
    private int site;

    public Paper(String id, String title, String searchWord, String source, String url, String intro, int site) {
        this.id = id;
        this.title = title;
        this.searchWord = searchWord;
        this.source = source;
        this.url = url;
        this.intro = intro;
        this.site = site;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDisLikeCount() {
        return disLikeCount;
    }

    public void setDisLikeCount(int disLikeCount) {
        this.disLikeCount = disLikeCount;
    }

    public int getSite() {
        return site;
    }

    public void setSite(int site) {
        this.site = site;
    }

    @Override
    public String toString() {
        return String.format("id: %s, title: %s, searchWord: %s, source: %s, url: %s, likeCount: %s, disLikeCount: %s",
                String.valueOf(id), title, searchWord, source, url, String.valueOf(likeCount), String.valueOf(disLikeCount));
    }
}
