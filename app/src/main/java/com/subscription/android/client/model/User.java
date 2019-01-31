package com.subscription.android.client.model;

/**
 * Created by Kirill_code on 25.01.2019.
 */
public class User {
    String uid, email;
  /*  Boolean adminclaim;
    public Boolean getAdminclaim() {
        return adminclaim;
    }

    public void setAdminclaim(boolean adminclaim) {
        this.adminclaim = adminclaim;
    }

    public User(String uid, String email, Boolean adminClaim) {
        this.uid = uid;
        this.email = email;
      this.adminclaim=adminClaim;

    }
*/

    public User(String uid, String email) {
        this.uid = uid;
        this.email = email;
    }

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
}
