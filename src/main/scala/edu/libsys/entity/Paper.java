package edu.libsys.entity;

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

    public Paper() {
    }

    public Paper(String id, String title, String searchWord, String source, String url, String intro, int likeCount, int disLikeCount, int site) {
        this.id = id;
        this.title = title;
        this.searchWord = searchWord;
        this.source = source;
        this.url = url;
        this.intro = intro;
        this.likeCount = likeCount;
        this.disLikeCount = disLikeCount;
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
        return "Paper{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", searchWord='" + searchWord + '\'' +
                ", source='" + source + '\'' +
                ", url='" + url + '\'' +
                ", intro='" + intro + '\'' +
                ", likeCount=" + likeCount +
                ", disLikeCount=" + disLikeCount +
                ", site=" + site +
                '}';
    }
}
