package edu.libsys.entity;

/**
 * Created by spark on 3/11/17.
 */
public class User {
    private int id;
    private String name;
    private int type;
    private String email;
    private String passwd;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public String toString(){
        return "id: " + String.valueOf(id)
                + ", name: " + name
                + ", type: " + String.valueOf(type)
                + ", email: " + email;
    }
}
