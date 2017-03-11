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

    public Item(int propId,int marcRecId,int lendCount){
        this.setPropId(propId);
        this.setMarcRecId(marcRecId);
        this.setLendCount(lendCount);
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
        return String.format("propId: %d, marcRecId: %d, lendCount: %d, likeCount: %d, dislikeCount: %d",
                propId, marcRecId, lendCount, likeCount, disLikeCount);
    }
}
