package edu.libsys.entity;

/**
 * Created by spark on 3/11/17.
 */
public class Item {
    private int prop_id;
    private int marc_rec_id;
    private int count;

    public int getProp_id() {
        return prop_id;
    }

    public void setProp_id(int prop_id) {
        this.prop_id = prop_id;
    }

    public int getMarc_rec_id() {
        return marc_rec_id;
    }

    public void setMarc_rec_id(int marc_rec_id) {
        this.marc_rec_id = marc_rec_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
