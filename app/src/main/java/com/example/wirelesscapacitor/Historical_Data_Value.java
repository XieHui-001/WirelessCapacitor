package com.example.wirelesscapacitor;

public class Historical_Data_Value {
    public String DeviceID;
    public String Value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String type;
    public String unit;
    public  Historical_Data_Value(String deviceID, String value,String type,String unit, String temp, String humi, String nowdate){
        this.DeviceID = deviceID;
        this.Value = value;
        this.Temp = temp;
        this.Humi = humi;
        this.Nowdate = nowdate;
        this.unit = unit;
        this.type = type;
    }
    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getTemp() {
        return Temp;
    }

    public void setTemp(String temp) {
        Temp = temp;
    }

    public String getHumi() {
        return Humi;
    }

    public void setHumi(String humi) {
        Humi = humi;
    }

    public String getNowdate() {
        return Nowdate;
    }

    public void setNowdate(String nowdate) {
        Nowdate = nowdate;
    }

    public String Temp;
    public String Humi;
    public String Nowdate;
}
