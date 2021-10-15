package com.example.wirelesscapacitor;

public class Multidevice {
    public Multidevice(String deviceId, String capacitance, String temperature, String time) {
        this.id = deviceId;
        this.capacitance = capacitance;
        this.temperature = temperature;
        this.tiem = time;
    }

    public static Multidevice Instance = null;
    public Multidevice(){
        Multidevice.Instance = this;
    }

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

    public String id;//设备id
    public String tiem;//时间
    public String capacitance;//电容
    public String temperature;//温度
}
