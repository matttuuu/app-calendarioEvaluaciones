package com.example.calendarapp_idra;

public class EventsTerapeutico {
    String EVENTTERA, TIMETERA, DATETERA, MONTHTERA, YEARTERA;

    public String getEVENTTERA() {
        return EVENTTERA;
    }

    public void setEVENTTERA(String EVENT) {
        this.EVENTTERA = EVENTTERA;
    }

    public String getTIMETERA() {
        return TIMETERA;
    }

    public void setTIMETERA(String TIME) {
        this.TIMETERA = TIMETERA;
    }

    public String getDATETERA() {
        return DATETERA;
    }

    public void setDATETERA(String DATE) {
        this.DATETERA = DATETERA;
    }

    public String getMONTHTERA() {
        return MONTHTERA;
    }

    public void setMONTHTERA(String MONTH) {
        this.MONTHTERA = MONTHTERA;
    }

    public String getYEARTERA() {
        return YEARTERA;
    }

    public void setYEARTERA(String YEAR) {
        this.YEARTERA = YEARTERA;
    }

    public EventsTerapeutico(String EVENTTERA, String TIMETERA, String DATETERA, String MONTHTERA, String YEARTERA) {
        this.EVENTTERA = EVENTTERA;
        this.TIMETERA = TIMETERA;
        this.DATETERA = DATETERA;
        this.MONTHTERA = MONTHTERA;
        this.YEARTERA = YEARTERA;
    }


}
