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

    public Author(int id, String name, int workCount,int likeCount, int disLikeCount) {
        this.setId(id);
        this.setName(name);
        this.setWorkCount(workCount);
        this.setLikeCount(likeCount);
        this.setDisLikeCount(disLikeCount);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getWorkCount() {
        return workCount;
    }

    public void setWorkCount(int workCount) {
        this.workCount = workCount;
    }

    @Override
    public String toString() {
        return String.format("id: %d, name: %s,workCount: %d, likeCount: %d, dislikeCount: %d",
                id, name, workCount, likeCount, disLikeCount);
    }

}
