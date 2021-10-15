package com.example.wirelesscapacitor;

public class MultideviceForDeviceName {
    public MultideviceForDeviceName(String name) {
        this.name = name;
    }

    public static MultideviceForDeviceName Instance = null;

    public MultideviceForDeviceName() {
        MultideviceForDeviceName.Instance = this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String name;
}
