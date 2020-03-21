package com.subscription.android.client.model;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Kirill_code on 01.02.2019.
 */
public class VisitDate {
    private long id, instr_id, time;
    private Date date;
    private String uid;

    public VisitDate(long id, Date date) {
        this.id = id;
/*maybe date format need
**/
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getInstr_id() {
        return instr_id;
    }

    public void setInstr_id(long instr_id) {
        this.instr_id = instr_id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

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
