package com.example.calendarapp_idra;

public class EventsSeguridad {
    String EVENTSEG, TIMESEG, DATESEG, MONTHSEG, YEARSEG;

    public String getEVENTSEG() {
        return EVENTSEG;
    }

    public void setEVENTSEG(String EVENT) {
        this.EVENTSEG = EVENTSEG;
    }

    public String getTIMESEG() {
        return TIMESEG;
    }

    public void setTIMESEG(String TIME) {
        this.TIMESEG = TIMESEG;
    }

    public String getDATESEG() {
        return DATESEG;
    }

    public void setDATESEG(String DATE) {
        this.DATESEG = DATESEG;
    }

    public String getMONTHSEG() {
        return MONTHSEG;
    }

    public void setMONTHSEG(String MONTH) {
        this.MONTHSEG = MONTHSEG;
    }

    public String getYEARSEG() {
        return YEARSEG;
    }

    public void setYEARSEG(String YEAR) {
        this.YEARSEG = YEARSEG;
    }

    public EventsSeguridad(String EVENTSEG, String TIMESEG, String DATESEG, String MONTHSEG, String YEARSEG) {
        this.EVENTSEG = EVENTSEG;
        this.TIMESEG = TIMESEG;
        this.DATESEG = DATESEG;
        this.MONTHSEG = MONTHSEG;
        this.YEARSEG = YEARSEG;
    }
}
