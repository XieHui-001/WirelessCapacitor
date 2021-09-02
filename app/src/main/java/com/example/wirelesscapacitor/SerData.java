package com.example.wirelesscapacitor;

import java.util.Date;
import java.text.SimpleDateFormat;


public class SerData {

    public String DateTime;
    public String ID;
    public String DianROng;
    public String Temptur;

    public void Init() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        this.DateTime = df.format(new Date());

    }


}
