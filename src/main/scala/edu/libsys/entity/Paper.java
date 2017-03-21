package edu.libsys.entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Paper {
    private int id;
    private String paperId;
    private String title;
    private String searchWord;
    private String source;
    private String url;
    private String intro;
    private int likeCount;
    private int disLikeCount;

    public Paper() {
    }

    public Paper(String paperId, String title, String searchWord, String source, String url, String intro, int likeCount, int disLikeCount, int site) {
        this.paperId = paperId;
        this.title = title;
        this.searchWord = searchWord;
        this.source = source;
        this.url = url;
        this.intro = intro;
        this.likeCount = likeCount;
        this.disLikeCount = disLikeCount;
    }

    @XmlAttribute(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlAttribute(name = "paperId")
    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    @XmlAttribute(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlAttribute(name = "searchWord")
    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    @XmlAttribute(name = "source")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @XmlAttribute(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @XmlAttribute(name = "intro")
    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @XmlAttribute(name = "likeCount")
    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    @XmlAttribute(name = "disLikeCount")
    public int getDisLikeCount() {
        return disLikeCount;
    }

    public void setDisLikeCount(int disLikeCount) {
        this.disLikeCount = disLikeCount;
    }

    @Override
    public String toString() {
        return "Paper{" +
                "id=" + id +
                ", paperId='" + paperId + '\'' +
                ", title='" + title + '\'' +
                ", searchWord='" + searchWord + '\'' +
                ", source='" + source + '\'' +
                ", url='" + url + '\'' +
                ", intro='" + intro + '\'' +
                ", likeCount=" + likeCount +
                ", disLikeCount=" + disLikeCount +
                '}';
    }
}
