package com.example.wirelesscapacitor;

public class DeviceName {
    public static DeviceName Instance = null;
    public DeviceName(){
        DeviceName.Instance = this;
    }
    public String getDeviceName() {
        return DeviceName_;
    }

    public void setDeviceName(String deviceName) {
        DeviceName_ = deviceName;
    }

    public String DeviceName_;
}
