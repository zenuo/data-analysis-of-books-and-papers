package edu.libsys.entity;

public class Behavior {
    private int id;
    private int userId;
    private int itemId;
    private int type;//like:0, dislike:1
    private String time;
    private String content;

    public Behavior(int id, int userId, int itemId, int type, String time, String content) {
        this.id = id;
        this.userId = userId;
        this.itemId = itemId;
        this.type = type;
        this.time = time;
        this.content = content;
    }

    public Behavior() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Behavior{" +
                "id=" + id +
                ", userId=" + userId +
                ", itemId=" + itemId +
                ", type=" + type +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
