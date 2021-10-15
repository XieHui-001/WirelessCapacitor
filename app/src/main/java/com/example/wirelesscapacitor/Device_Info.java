package com.example.wirelesscapacitor;

public class Device_Info {
    public static Device_Info Instance = null;
    public Device_Info(){
        Device_Info.Instance = this;
    }
    public String nOTnuLL;

    public String getnOTnuLL() {
        return nOTnuLL;
    }

    public void setnOTnuLL(String nOTnuLL) {
        this.nOTnuLL = nOTnuLL;
    }

    public String getNoTnull_id() {
        return noTnull_id;
    }

    public void setNoTnull_id(String noTnull_id) {
        this.noTnull_id = noTnull_id;
    }

    public String noTnull_id;
}
