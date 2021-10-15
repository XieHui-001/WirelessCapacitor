package com.example.wirelesscapacitor;

import android.os.ParcelUuid;

import org.greenrobot.eventbus.EventBus;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Decoder {

    public static String printHexString(byte[] b) {
        String ostr = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ostr = ostr + hex;
        }
        return ostr;
    }

    public static byte[] hexStr2Byte(String hex) {
        ByteBuffer bf = ByteBuffer.allocate(hex.length() / 2);
        for (int i = 0; i < hex.length(); i++) {
            String hexStr = hex.charAt(i) + "";
            i++;
            hexStr += hex.charAt(i);
            byte b = (byte) Integer.parseInt(hexStr, 16);
            bf.put(b);
        }
        return bf.array();
    }

    public static String Uints = null;

    public void Test() {
        String hex = "AAAF3900570010574241393933202B30303030203129040080000D0A54656D70203D2033332E31313020202048756D69203D2035332E3732300D0A87";
        byte[] rbuf = hexStr2Byte(hex);
        Decoder c = new Decoder();
        c.Decode(rbuf, rbuf.length);
    }

    public void Decode(byte[] rbuf, int len) {

        int needMaxLen = 60;
        if (len < needMaxLen) {
            return;
        }

        int firstIdx = -1;
        {
            for (int j = 0; j < len - 1; j++) {
                boolean b = rbuf[j] == (byte) 0xAA && rbuf[j + 1] == (byte) 0xAF;
                if (b) {
                    firstIdx = j;
                    break;
                }
            }
            if (firstIdx < 0) {
                return;
            }

            if (len - firstIdx < needMaxLen) {
                return;
            }

            boolean crc = false;
            byte crcVal = rbuf[firstIdx + needMaxLen - 1];
            byte cCrc = 0;
            for (int j = firstIdx; j < firstIdx + needMaxLen - 1; j++) {
                cCrc += rbuf[j];
            }
            crc = crcVal == cCrc;
            if (!crc) {
                return;
            }

        }

        SerData serData = new SerData();
        serData.Init();

        int idFirstIndx = firstIdx + 2;
        int idLastIndx = idFirstIndx + 12;
        {
            byte[] dBuf = Arrays.copyOfRange(rbuf, idFirstIndx, idLastIndx);
            serData.ID = this.IDData(dBuf);//id
        }

        int dFirstIndx = idLastIndx;
        int dLastIndx = dFirstIndx + 14;
        {
            byte[] dBuf = Arrays.copyOfRange(rbuf, dFirstIndx, dLastIndx);
            serData.DianROng = this.DianRongData(dBuf);//电容
            MyErrorLog.e("电容数据", serData.DianROng);
        }


        int wenduFirstIndx = dLastIndx;
        int wenduLastIndx = wenduFirstIndx + 31;
        {
            byte[] dBuf = Arrays.copyOfRange(rbuf, wenduFirstIndx, wenduLastIndx);
            String s = new String(dBuf);
            serData.Temptur = s;//温度
        }

        {
            //跳过 crc
            byte[] dBuf = Arrays.copyOfRange(rbuf, wenduLastIndx + 1, len);
            this.Decode(dBuf, dBuf.length);
        }

        //发送按钮状态
        EventBus.getDefault().post(new SwitchStatusEvent(serData.ID, serData.DianROng, serData.DateTime, serData.Temptur));

    }

    private String IDData(byte[] dat) {
        return printHexString(dat);
    }

    private String DianRongData(byte[] dat) {
        if (dat.length < 14) {
            return "";
        }

        String rstr = "";
        String sing = this.Sing(dat[0]);
        rstr += sing;

        String dataType = this.DataByte(dat);
        this.Space(dat[5]);
        dataType = this.Point(dataType, dat[6]);
        MyErrorLog.e("数据状态", dataType);
        rstr += dataType;

        String sb1 = this.SB1(dat[7]);
        rstr += sb1;

        String sb2 = this.SB2(dat[8]);
        rstr += sb2;

        String sb3 = this.SB3(dat[9]);
        rstr += sb3;

        String sb4 = this.SB4(dat[10]);
        rstr += sb4;

        this.BAR(dat[11]);
        this.EOF(dat[12]);
        this.ENTER(dat[13]);

        return rstr;
    }


    private String Sing(byte dat) {
        String sing = "";
        if (dat == 0x2b) {
            sing = "+";
        } else if (dat == 0x2d) {
            sing = "-";
        }
        return sing;
    }

    private String DataByte(byte[] dat) {
        byte b[] = {dat[1], dat[2], dat[3], dat[4]};
        return new String(b);
    }

    private void Space(byte dat) {

    }

    private String Point(String val, byte dat) {
        StringBuffer s1 = new StringBuffer(val);
        String YesValue = "0.000";
        MyErrorLog.e("原始Value", val + "原始 dat:" + dat);
        int point = dat;
        switch (point) {
            case 0x30:
                break;
            case 0x31:
                if (val.contains("?")) {
                    return YesValue;
                } else {
                    s1.insert(1, ".");
                    break;
                }
            case 0x32:
                if (val.contains("?")) {
                    return YesValue;
                } else {
                    s1.insert(2, ".");
                    break;
                }
            case 0x33:
                if (val.contains("?")) {
                    return YesValue;
                } else {
                    s1.insert(3, ".");
                    break;
                }
            default:
                if (val.contains("?")) {
                    return YesValue;
                } else {
                    return s1.insert(3, ".").toString();
                }
//            case 0x34:
//                return "ERR";
////                int idx = -1;
////                int zerocnt = 0;
////                int noZeroCnt = 0;
////                for (int i = 0; i < val.length(); i++) {
////                    char item = val.charAt(i);
////                    if (item == '0') {
////                        ++zerocnt;
////                    }
////                    if (item != '0') {
////                        idx = i;
////                        break;
////                    }
////                }
////                if (idx < 0) {
////                    return val;
////                }
////                s1.insert(idx, ".");
////                break;
//            default:
//                String hex = Integer.toHexString(point & 0xFF);
//                return "x:" + hex;
        }

        return s1.toString();

    }


    public String SB1(byte dat) {

        String rstr = "";

        int BPN = dat & 0x01;
        int HOLD = (dat >> 1) & 0x01;
        int REL = (dat >> 2) & 0x01;
        int AC = (dat >> 3) & 0x01;
        int DC = (dat >> 4) & 0x01;
        int AUTO = (dat >> 5) & 0x01;

        if (AC == 1) {
            // 交流
            rstr += "AC - ";
            Uints = "AC";
        }

        if (DC == 1) {
            // 直流
            rstr += "DC - ";
            Uints = "DC";
        }

        if (AUTO == 1) {
            // 自动
            rstr += "AUTO - ";
        }
        return rstr;
    }

    private String SB2(byte dat) {
        String rstr = "";
        int Z3 = dat & 0x01;
        int n = (dat >> 1) & 0x01;
        int Bat = (dat >> 2) & 0x01;
        int APO = (dat >> 3) & 0x01;
        int MIN = (dat >> 4) & 0x01;
        int MAX = (dat >> 5) & 0x01;
        int Z2 = (dat >> 6) & 0x01;
        int Z1 = (dat >> 7) & 0x01;

        if (Z3 == 1) {
            rstr += "Z3 - ";
        } else if (n == 1) {
            rstr += "n - ";
        } else if (Bat == 1) {
            rstr += "Bat - ";
        } else if (APO == 1) {
            rstr += "APO - ";
        } else if (MIN == 1) {
            rstr += "MIN - ";
        } else if (MAX == 1) {
            rstr += "MAX - ";
        } else if (Z2 == 1) {
            rstr += "Z2 - ";
        } else if (Z1 == 1) {
            rstr += "Z1 - ";
        }
        return rstr;
    }

    private String SB3(byte dat) {
        String rstr = "";

        int Z4 = dat & 0x01;
        int MOD = (dat >> 1) & 0x01;
        int Diode = (dat >> 2) & 0x01;
        int Beep = (dat >> 3) & 0x01;
        int M = (dat >> 4) & 0x01;
        int k = (dat >> 5) & 0x01;
        int m = (dat >> 6) & 0x01;
        int u = (dat >> 7) & 0x01;

        if (Z4 == 1) {
            rstr += "Z4 - ";
        } else if (MOD == 1) {
            rstr += "MOD - ";
        } else if (Diode == 1) {
            rstr += "Diode - ";
        } else if (Beep == 1) {
            rstr += "Beep - ";
        } else if (M == 1) {
            rstr += "M - ";
        } else if (k == 1) {
            rstr += "k - ";
        } else if (m == 1) {
            rstr += "m - ";
        } else if (u == 1) {
            rstr += "u - ";
        }
        return rstr;
    }

    private String SB4(byte dat) {
        String rstr = "";

        int F = dat & 0x01;
        int C = (dat >> 1) & 0x01;
        int f = (dat >> 2) & 0x01;
        int Hz = (dat >> 3) & 0x01;
        int hFE = (dat >> 4) & 0x01;
        int Q = (dat >> 5) & 0x01;
        int A = (dat >> 6) & 0x01;
        int V = (dat >> 7) & 0x01;

        if (F == 1) {
            // 电容单位
            rstr += "F - ";
        } else if (C == 1) {
            // 电容类型
            rstr += "C - ";
        } else if (f == 1) {
            // 直流电容单位
            rstr += "f - ";
        } else if (Hz == 1) {
            // 电流频率
            rstr += "Hz - ";
        } else if (hFE == 1) {
            // 三极管直流电流
            rstr += "hFE - ";
        } else if (Q == 1) {
            //
            rstr += "Ω - ";
        } else if (A == 1) {
            // 电流
            rstr += "A - ";
        } else if (V == 1) {
            // 电压
            rstr += "V - ";
        }
        return rstr;
    }

    private void BAR(byte dat) {

    }

    private void EOF(byte dat) {

    }

    private void ENTER(byte dat) {

    }
}
