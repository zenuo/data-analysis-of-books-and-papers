package edu.libsys.entity;

/**
 * Created by spark on 3/11/17.
 */
public class Item {
    private int propId;
    private int marcRecId;
    private int lendCount;
    private int likeCount = 0;
    private int disLikeCount = 0;

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
        return "propId: " + String.valueOf(propId)
                + ", marcRecId: " + String.valueOf(marcRecId)
                + ", lendCount: " + String.valueOf(lendCount)
                + ", likeCount: " + String.valueOf(lendCount)
                + ", dislikeCount: " + String.valueOf(disLikeCount);
    }
}
