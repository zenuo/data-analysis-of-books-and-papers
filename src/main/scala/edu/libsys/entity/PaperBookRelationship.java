package edu.libsys.entity;

public class PaperBookRelationship {
    private int id;
    private int paperId;
    private int bookId;

    public PaperBookRelationship() {

    }

    public PaperBookRelationship(int paperId, int bookId) {
        this.paperId = paperId;
        this.bookId = bookId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPaperId() {
        return paperId;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    @Override
    public String toString() {
        return "PaperBookRelationship{" +
                "id=" + id +
                ", paperId=" + paperId +
                ", bookId=" + bookId +
                '}';
    }
}
