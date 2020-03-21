package com.subscription.android.client.model;

public class UserAdmins {
    String uid, email;
    Boolean claim;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getClaim() {
        return claim;
    }

    public void setClaim(Boolean claim) {
        this.claim = claim;
    }
}
