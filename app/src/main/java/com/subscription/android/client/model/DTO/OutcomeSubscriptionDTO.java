package com.subscription.android.client.model.DTO;

import java.util.Date;


public class OutcomeSubscriptionDTO {


    private long id,
            price,
            instructorId,
            associatedUserId;

    private String
            description;



    public void setId(long id) {
        this.id = id;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setInstructorId(long instructorId) {
        this.instructorId = instructorId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public long getPrice() {
        return price;
    }

    public long getInstructorId() {
        return instructorId;
    }

    public String getDescription() {
        return description;
    }

    public long getAssociatedUserId() {
        return associatedUserId;
    }

    public void setAssociatedUserId(long associatedUserId) {
        this.associatedUserId = associatedUserId;
    }
}
