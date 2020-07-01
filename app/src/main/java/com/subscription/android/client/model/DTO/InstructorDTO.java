package com.subscription.android.client.model.DTO;


import java.io.Serializable;

/**
 * Created by Kirill_code on 21.06.2020.
 */
public class InstructorDTO implements Serializable {


    private long id;
    private String name, surname, uid;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
