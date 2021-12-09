package com.example.wirelesscapacitor;

public class DeviceName_Install {
    public static DeviceName_Install Instance = null;

    public DeviceName_Install() {
        DeviceName_Install.Instance = this;
    }

    public DeviceName_Install(String id, String name) {
        this.ID = id;
        this.Name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String ID;
    public String Name;
}
