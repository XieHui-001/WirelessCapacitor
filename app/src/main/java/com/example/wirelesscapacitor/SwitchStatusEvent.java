package com.example.wirelesscapacitor;


public class SwitchStatusEvent {

    private String departId;
    private String departCapacitance;
    private String departTime;
    private String departTemperature;


    public SwitchStatusEvent(String departId, String departCapacitance, String departTime, String departTemperature) {
        this.departId = departId;
        this.departCapacitance = departCapacitance;
        this.departTime = departTime;
        this.departTemperature = departTemperature;

    }


    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }


    public String getDepartCapacitance() {
        return departCapacitance;
    }

    public void setDepartCapacitance(String departCapacitance) {
        this.departCapacitance = departCapacitance;
    }

    public String getDepartTime() {
        return departTime;
    }

    public void setDepartTime(String departTime) {
        this.departTime = departTime;
    }

    public String getDepartTemperature() {
        return departTemperature;
    }

    public void setDepartTemperature(String departTemperature) {
        this.departTemperature = departTemperature;
    }
}
