package com.subscription.android.client.model;

import java.util.List;

/**
 * Created by Kirill_code on 15.01.2019.
 */
public class Subscriptions {
    private long id, userid, instructorid, price;
    private String saleDate, finishDate, description;
    private List<VisitDate> visitDates;

    public Subscriptions(long id, long userid, long instructorid, long price, String saleDate, String finishDate, String description) {
        this.id = id;
        this.userid = userid;
        this.instructorid = instructorid;
        this.price = price;
        this.saleDate = saleDate;
        this.finishDate = finishDate;
        this.description = description;
    }

    public List<VisitDate> getVisitDates() {
        return visitDates;
    }

    public void setVisitDates(List<VisitDate> visitDates) {
        this.visitDates = visitDates;
    }

    public long getId() {
        return id;
    }

    public long getUserid() {
        return userid;
    }

    public long getInstructorid() {
        return instructorid;
    }

    public long getPrice() {
        return price;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public String getDescription() {
        return description;
    }
}
