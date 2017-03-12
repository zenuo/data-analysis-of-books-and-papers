package edu.libsys.entity;

public class LendRecord {
    private int certId;
    private String time;
    private int marcRecId;
    private String callNo;

    public LendRecord(int cert_id, String time, int marcRecId, String call_no) {
        this.setCertId(cert_id);
        this.setTime(time);
        this.setMarcRecId(marcRecId);
        this.setCallNo(call_no);
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

    public String getCallNo() {
        return callNo;
    }

    public void setCallNo(String callNo) {
        this.callNo = callNo;
    }

    @Override
    public String toString() {
        return "LendRecord{" +
                "certId=" + certId +
                ", time='" + time + '\'' +
                ", marcRecId=" + marcRecId +
                ", callNo='" + callNo + '\'' +
                '}';
    }
}
