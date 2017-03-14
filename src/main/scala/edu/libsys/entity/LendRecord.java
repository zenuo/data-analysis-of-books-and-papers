package edu.libsys.entity;

public class LendRecord {
    private int certId;
    private String time;
    private int marcRecId;

    public LendRecord(int certId, String time, int marcRecId) {
        this.certId = certId;
        this.time = time;
        this.marcRecId = marcRecId;
    }

    public int getCertId() {
        return certId;
    }

    public void setCertId(int certId) {
        this.certId = certId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMarcRecId() {
        return marcRecId;
    }

    public void setMarcRecId(int marcRecId) {
        this.marcRecId = marcRecId;
    }

    @Override
    public String toString() {
        return "LendRecord{" +
                "certId=" + certId +
                ", time='" + time + '\'' +
                ", marcRecId=" + marcRecId +
                '}';
    }
}
