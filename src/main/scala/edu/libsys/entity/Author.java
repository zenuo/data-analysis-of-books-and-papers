package edu.libsys.entity;

/**
 * Created by spark on 3/11/17.
 */
public class Author {
    private int id;
    private String name;
    private int workCount = 0;
    private int likeCount = 0;
    private int disLikeCount = 0;

    public Author(String name, int workCount) {
        this.setName(name);
        this.setWorkCount(workCount);
    }

    //empty constructor
    public Author() {
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
        return String.format("id: %d, name: %s, workCount: %d, likeCount: %d, dislikeCount: %d",
                id, name, workCount, likeCount, disLikeCount);
    }

}
