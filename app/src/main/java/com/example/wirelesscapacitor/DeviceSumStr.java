package com.example.wirelesscapacitor;

public class DeviceSumStr {
//    public static DeviceSumStr Instance = null;

    public String getDeviceStr() {
        return DeviceStr;
    }

    public void setDeviceStr(String deviceStr) {
        DeviceStr = deviceStr;
    }

    public String DeviceStr;

    public DeviceSumStr(String str){
        this.DeviceStr = str;
    }
}
