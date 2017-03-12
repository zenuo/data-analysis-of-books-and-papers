package edu.libsys.entity;

public class Book {
    private int marcRecId;
    private String callId;
    private String title;
    private int author;
    private String publisher;
    private String pubYear;
    private String isbn;
    private int likeCount;
    private int disLikeCount;

    public Book() {
    }

    public Book(int marcRecId, String callId, String title, int author, String publisher, String pubYear, String isbn, int likeCount, int disLikeCount) {
        this.marcRecId = marcRecId;
        this.callId = callId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.pubYear = pubYear;
        this.isbn = isbn;
        this.likeCount = likeCount;
        this.disLikeCount = disLikeCount;
    }

    @Override
    public String toString() {
        return "Book{" +
                "marcRecId=" + marcRecId +
                ", callId='" + callId + '\'' +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", publisher='" + publisher + '\'' +
                ", pubYear='" + pubYear + '\'' +
                ", isbn='" + isbn + '\'' +
                ", likeCount=" + likeCount +
                ", disLikeCount=" + disLikeCount +
                '}';
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

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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

}
