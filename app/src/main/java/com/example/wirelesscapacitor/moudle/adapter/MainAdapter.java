package com.example.wirelesscapacitor.moudle.adapter;

import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wirelesscapacitor.DeviceName;
import com.example.wirelesscapacitor.Device_Info;
import com.example.wirelesscapacitor.MainActivity;
import com.example.wirelesscapacitor.MainBean;
import com.example.wirelesscapacitor.MyErrorLog;
import com.example.wirelesscapacitor.MyLog;
import com.example.wirelesscapacitor.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter extends BaseQuickAdapter<MainBean, BaseViewHolder> {
    public MainAdapter(@Nullable List<MainBean> data) {
        super(R.layout.item_main, data);
    }

    private String myData = "null";
    private List<String> IsNULL = new ArrayList<>();

    // notifyItemRangeChanged(i, mModels.size());
    // Changed  item  Remove item Show
    @Override
    protected void convert(BaseViewHolder helper, MainBean item) {
        try {
            MainActivity.CurrentIndex = MainActivity.CurrentIndex + 1;
            if (Device_Info.Instance.nOTnuLL == null || !Device_Info.Instance.nOTnuLL.equals(item.id)) {
                final String ISnull = MainActivity.Instance.TimerTask_GetNewDeviceName(item.id);
                if (ISnull != null) {
                    MyErrorLog.e("Qut Device Name VAlue", ":" + ISnull);
                    Device_Info.Instance.setnOTnuLL(ISnull);
                    Device_Info.Instance.setNoTnull_id(item.id);
                    helper.setText(R.id.test1, ISnull != null ? ISnull : ISnull);
                } else {
                    final String TwoName = MainActivity.Instance.TimerTask_GetNewDeviceName(item.id);
                    MyErrorLog.e("2222", "");
                    if (TwoName == null) {
                        helper.setText(R.id.test1, (TwoName != null ? TwoName : Device_Info.Instance.nOTnuLL));
                    } else {
                        helper.setText(R.id.test1, TwoName != null ? TwoName : item.id);
                    }
                }
            } else {
                MyErrorLog.e("3333", "");
                helper.setText(R.id.test1, item.id); //Device_Info.Instance.nOTnuLL
            }
            if (myData != item.getCapacitance()) {
                myData = item.getCapacitance();
                Pattern pattern = Pattern.compile("\t|\r|\n|\\s*");
                Matcher matcher = pattern.matcher(myData);
                String dest = matcher.replaceAll("");
                String CutAuio = dest.replace("AUTO", "");
                String Now_ = CutAuio.replace("+", "");
                String Now__ = Now_.replace(" ", "");


                String GetNOWVALUE_ = Now__.substring(0, Now_.length() - 1);
                String IndexoFCut = GetNOWVALUE_.substring(1, GetNOWVALUE_.length());
                String NoeStr = GetNOWVALUE_.substring(0, 1);
                String SUmStr = IndexoFCut.replace("-", " ");
                String GetNOWVALUE = NoeStr + SUmStr;
                if (GetNOWVALUE.contains("DC")) {
                    GetNOWVALUE.replace("DC", "");
                    helper.setText(R.id.dataType, "DC");
                    if (GetNOWVALUE.contains("ERR")) {
                        GetNOWVALUE.replace("ERR", "");
                        helper.setText(R.id.dataUint, GetNOWVALUE);
                    }
                } else if (GetNOWVALUE.contains("AC")) {
                    GetNOWVALUE.replace("AC", "");
                    helper.setText(R.id.dataType, "AC");
                    if (GetNOWVALUE.contains("ERR")) {
                        GetNOWVALUE.replace("ERR", "");
                        helper.setText(R.id.dataUint, "Ω");
                    }
                } else if (GetNOWVALUE.contains("Hz")) {
                    helper.setText(R.id.dataType, "频率");
                } else if (GetNOWVALUE.contains("ERR") || GetNOWVALUE.contains("-ERR") || GetNOWVALUE.contains("?") || GetNOWVALUE.contains(".") || GetNOWVALUE.contains(":")) {
                    helper.setText(R.id.dataValue, "ERR");
                    GetNOWVALUE.replace("ERR", "");
                    GetNOWVALUE.replace("-ERR", "");
                    GetNOWVALUE.replace("?", "");
                    GetNOWVALUE.replace(".", "");
                    GetNOWVALUE.replace(":", "");
                }

                if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("V")) {
                    helper.setText(R.id.dataUint, "mV");
                } else if (GetNOWVALUE.contains("M") && GetNOWVALUE.contains("Ω")) {
                    helper.setText(R.id.dataType, "电阻");
                    helper.setText(R.id.dataUint, "MΩ");
                } else if (GetNOWVALUE.contains("Hz")) {
                    helper.setText(R.id.dataUint, "Hz");
                } else if (GetNOWVALUE.contains("u") && GetNOWVALUE.contains("A")) {
                    helper.setText(R.id.dataUint, "uA");
                } else if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("A")) {
                    helper.setText(R.id.dataUint, "mA");
                } else if (GetNOWVALUE.contains("A") && GetNOWVALUE.contains("V")) {
                    helper.setText(R.id.dataUint, "V");
                } else if (GetNOWVALUE.contains("A")) {
                    helper.setText(R.id.dataUint, "A");
                } else if (GetNOWVALUE.contains("n") && GetNOWVALUE.contains("f")) {
                    helper.setText(R.id.dataType, "电容");
                    helper.setText(R.id.dataUint, "nF");
                } else if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("f")) {
                    helper.setText(R.id.dataType, "电容");
                    helper.setText(R.id.dataUint, "mF");
                } else if (GetNOWVALUE.contains("u") && GetNOWVALUE.contains("f")) {
                    helper.setText(R.id.dataType, "电容");
                    helper.setText(R.id.dataUint, "uF");
                } else if (GetNOWVALUE.contains("f")) {
                    helper.setText(R.id.dataType, "电容");
                    helper.setText(R.id.dataUint, "F");
                } else if (GetNOWVALUE.contains("k") && GetNOWVALUE.contains("Ω")) {
                    helper.setText(R.id.dataType, "电阻");
                    helper.setText(R.id.dataUint, "kΩ");
                } else if (GetNOWVALUE.contains("Diode") && GetNOWVALUE.contains("V")) {
                    helper.setText(R.id.dataType, "二极管");
                    helper.setText(R.id.dataUint, "V");
                } else if (GetNOWVALUE.contains("Ω")) {
                    helper.setText(R.id.dataType, "电阻");
                    helper.setText(R.id.dataUint, "Ω");

                }

                if (GetNOWVALUE.contains("ERRDC") || GetNOWVALUE.contains("ERRVC")) {
                    helper.setText(R.id.dataValue, "ERR");
                }

                if (GetNOWVALUE.contains("-ERR")) {
                    GetNOWVALUE.replace("-ERR", "");
                    helper.setText(R.id.dataValue, GetNOWVALUE);
                }
                if (GetNOWVALUE.length() > 5) {
                    String Cut_Str = GetNOWVALUE.replace(" ", "");
                    String CutString = null;
                    if (!Cut_Str.contains("-")) {
                        CutString = Cut_Str.substring(0, 5);
                    } else {
                        CutString = Cut_Str.substring(0, 6);
                    }

                    if (CutString.contains("ERRDC") || CutString.contains("ERRVC")) {
                        helper.setText(R.id.dataValue, "ERR");
                    } else if (CutString.contains("?")) {
                        helper.setText(R.id.dataValue, "ERR");
                    } else {
                        if (CutString.contains("ERRDC") || CutString.contains("ERRAC")) {
                            helper.setText(R.id.dataValue, "ERR");
                        } else if (CutString.contains("?")) {
                            helper.setText(R.id.dataValue, "ERR");
                        } else {
                            helper.setText(R.id.dataValue, CutString);
                        }
                    }
                }
//                if (DeviceName.Instance.DeviceName_ != null && DeviceName.Instance.DeviceName_ != "") {
//                    MyErrorLog.e("不等于空进入", DeviceName.Instance.DeviceName_);
//                    helper.setText(R.id.test1, DeviceName.Instance.DeviceName_);  // Update New Device Name
//                }


                helper.setText(R.id.test3, item.getTemperature()); //温度
                helper.setText(R.id.test4, item.getTiem()); //时间
            }
        } catch (Exception exception) {
            MyErrorLog.e("适配器错误 Item Error ", "info :" + exception.toString());
        }
    }
}
