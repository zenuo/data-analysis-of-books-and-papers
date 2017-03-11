package edu.libsys.entity;

/**
 * Created by spark on 3/11/17.
 */
public class LendRecord {
    private int cert_id;
    private String time;
    private int marc_rec_id;
    private String call_no;

    public int getCert_id() {
        return cert_id;
    }

    public void setCert_id(int cert_id) {
        this.cert_id = cert_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMarc_rec_id() {
        return marc_rec_id;
    }

    public void setMarc_rec_id(int marc_rec_id) {
        this.marc_rec_id = marc_rec_id;
    }

    public String getCall_no() {
        return call_no;
    }

    public void setCall_no(String call_no) {
        this.call_no = call_no;
    }
}
