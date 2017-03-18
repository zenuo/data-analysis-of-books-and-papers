package edu.libsys.entity;

public class Item {
    private int marcRecId;
    private int propId;
    private int lendCount;
    private int likeCount = 0;
    private int disLikeCount = 0;

    public Item(int marcRecId, int propId, int lendCount) {
        this.marcRecId = marcRecId;
        this.propId = propId;
        this.lendCount = lendCount;
    }

    public int getPropId() {
        return propId;
    }

    public void setPropId(int propId) {
        this.propId = propId;
    }

    public int getMarcRecId() {
        return marcRecId;
    }

    public void setMarcRecId(int marcRecId) {
        this.marcRecId = marcRecId;
    }


    public int getLendCount() {
        return lendCount;
    }

    public void setLendCount(int lendCount) {
        this.lendCount = lendCount;
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
        return "Item{" +
                "propId=" + propId +
                ", marcRecId=" + marcRecId +
                ", lendCount=" + lendCount +
                ", likeCount=" + likeCount +
                ", disLikeCount=" + disLikeCount +
                '}';
    }
}
