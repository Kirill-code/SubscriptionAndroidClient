package com.subscription.android.client.model;

import java.util.Date;

/**
 * Created by Kirill_code on 01.02.2019.
 */
public class VisitDate {
    private long id;
    private Date date;


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
