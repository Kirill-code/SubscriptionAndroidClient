package com.subscription.android.client.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Kirill_code on 15.01.2019.
 */
public class Subscription {
    private Date saleDate,
            finishDate;

    private long id,
            price,
            instructorId;

    private String userid,
            description,
            instrName,
            instrSurname;
    private List<VisitDate> visitDates = new ArrayList<>();
    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(long instructorId) {
        this.instructorId = instructorId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstrName() {
        return instrName;
    }

    public void setInstrName(String instrName) {
        this.instrName = instrName;
    }

    public String getInstrSurname() {
        return instrSurname;
    }

    public void setInstrSurname(String instrSurname) {
        this.instrSurname = instrSurname;
    }

    public List<VisitDate> getVisitDates() {
        return visitDates;
    }

    public void setVisitDates(List<VisitDate> visitDates) {
        this.visitDates = visitDates;
    }
}
