package edu.libsys.entity;

public class LendRecord {
    private String certId;
    private String time;
    private int marcRecId;

    public LendRecord(String certId, String time, int marcRecId) {
        this.certId = certId;
        this.time = time;
        this.marcRecId = marcRecId;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
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
                "certId='" + certId + '\'' +
                ", time='" + time + '\'' +
                ", marcRecId=" + marcRecId +
                '}';
    }
}
