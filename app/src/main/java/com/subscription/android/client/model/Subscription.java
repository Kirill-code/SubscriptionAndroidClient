package com.subscription.android.client.model;

import java.util.List;

/**
 * Created by Kirill_code on 15.01.2019.
 */
public class Subscription {
    private long id,  price;
    private String saleDate, finishDate, description, userid;
    private List<VisitDate> visitDates;
    private Instructor associatedInstructor;

    public Instructor getInstructor() {
        return associatedInstructor;
    }

    public void setInstructor(Instructor instructor) {
        this.associatedInstructor = instructor;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setAssociatedInstructor(Instructor associatedInstructor) {
        this.associatedInstructor = associatedInstructor;
    }

    public Subscription(long id, String userid, long price, String saleDate, String finishDate, String description) {
        this.id = id;
        this.userid = userid;
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

    public String getUserid() {
        return userid;
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
