package edu.libsys.entity;

/**
 * Created by spark on 3/11/17.
 */
public class Book {
    private int marcRecId;
    private String callId;
    private String title;
    private String publisher;
    private String pubYear;
    private int likeCount;
    private int disLikeCount;

    public Book(int marcRecId, String callId, String title, String publisher, String pubYear, int likeCount, int disLikeCount) {
        this.setMarcRecId(marcRecId);
        this.setCallId(callId);
        this.setTitle(title);
        this.setPublisher(publisher);
        this.setPubYear(pubYear);
        this.setLikeCount(likeCount);
        this.setDisLikeCount(disLikeCount);
    }

    public int getMarcRecId() {
        return marcRecId;
    }

    public void setMarcRecId(int marcRecId) {
        this.marcRecId = marcRecId;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPubYear() {
        return pubYear;
    }

    public void setPubYear(String pubYear) {
        this.pubYear = pubYear;
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

    @Override
    public String toString() {
        return String.format("marcRecId: %d, callId: %s, title: %s, publisher: %s, pubYear: %s, likeCount: %d, disLikeCount: %d",
                marcRecId, callId, title, publisher, pubYear, likeCount, disLikeCount);
    }
}
