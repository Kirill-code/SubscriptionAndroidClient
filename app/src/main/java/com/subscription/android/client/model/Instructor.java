package com.subscription.android.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kirill_code on 06.02.2019.
 */
public class Instructor implements Serializable {


    private long id;
    private String name, surname, uid;
    private List<Subscription> instructorSubscriptions= new ArrayList<>();

    public List<Subscription> getInstructorSubscriptions() {
        return instructorSubscriptions;
    }

    public void setInstructorSubscriptions(List<Subscription> instructorSubscriptions) {
        this.instructorSubscriptions = instructorSubscriptions;
    }

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
