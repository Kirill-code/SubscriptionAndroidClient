package com.subscription.android.client.model.DTO;

import java.util.Date;

public class VisitsDTO {
    private String date;
    private double  instr_id, visits_count;

    public double getInstr_id() {
        return instr_id;
    }

    public void setInstr_id(double instr_id) {
        this.instr_id = instr_id;
    }

    public double getVisits_count() {
        return visits_count;
    }

    public void setVisits_count(double visits_count) {
        this.visits_count = visits_count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
