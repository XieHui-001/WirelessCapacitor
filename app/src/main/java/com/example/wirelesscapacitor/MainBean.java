package com.example.wirelesscapacitor;

import java.io.Serializable;

public class MainBean implements Serializable {
    private String id;//设备id
    private String tiem;//时间
    private String capacitance;//电容
    private String temperature;//温度


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTiem() {
        return tiem;
    }

    public void setTiem(String tiem) {
        this.tiem = tiem;
    }

    public String getCapacitance() {
        return capacitance;
    }

    public void setCapacitance(String capacitance) {
        this.capacitance = capacitance;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
