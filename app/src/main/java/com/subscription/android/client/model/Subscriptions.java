package com.subscription.android.client.model;

/**
 * Created by Kirill_code on 15.01.2019.
 */
public class Subscriptions {
    private int id, userid, instructorid, price;
    private String saleDate, finishDate, description;

    public Subscriptions(int id, int userid, int instructorid, int price, String saleDate, String finishDate, String description) {
        this.id = id;
        this.userid = userid;
        this.instructorid = instructorid;
        this.price = price;
        this.saleDate = saleDate;
        this.finishDate = finishDate;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getUserid() {
        return userid;
    }

    public int getInstructorid() {
        return instructorid;
    }

    public int getPrice() {
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
