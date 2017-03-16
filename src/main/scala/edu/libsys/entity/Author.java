package edu.libsys.entity;

public class Author {
    private int id;
    private String name;
    private int workCount;
    private int likeCount = 0;
    private int disLikeCount = 0;

    public Author(String name, int workCount) {
        this.setName(name);
        this.setWorkCount(workCount);
    }

    //empty constructor
    public Author() {
    }

    public Author(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    //no setter of id

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getDisLikeCount() {
        return disLikeCount;
    }

    public int getWorkCount() {
        return workCount;
    }

    public void setWorkCount(int workCount) {
        this.workCount = workCount;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", workCount=" + workCount +
                ", likeCount=" + likeCount +
                ", disLikeCount=" + disLikeCount +
                '}';
    }
}
