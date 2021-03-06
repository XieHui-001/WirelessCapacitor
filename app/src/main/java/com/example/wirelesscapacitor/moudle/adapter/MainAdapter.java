package com.example.wirelesscapacitor.moudle.adapter;

import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wirelesscapacitor.DeviceName;
import com.example.wirelesscapacitor.DeviceName_Install;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private List<DeviceName_Install> deviceName_installs = new ArrayList<>();
    private List<String> temporary_Str = new ArrayList<>();
    DeviceName_Install deviceName_install = null;
    private boolean ist = false;

    // notifyItemRangeChanged(i, mModels.size());
    // Changed  item  Remove item Show
    @Override
    protected void convert(BaseViewHolder helper, MainBean item) {
        try {
            boolean isture = false;
            DeviceName_Install deviceName_install = null;
            if (Device_Info.Instance.nOTnuLL == null || !Device_Info.Instance.nOTnuLL.equals(item.id)) {
                final String ISnull = MainActivity.Instance.TimerTask_GetNewDeviceName(item.id);
                if (ISnull != null) {
                    if (temporary_Str.size() < 1) {
                        if (!temporary_Str.contains(ISnull)) {
                            temporary_Str.add(ISnull);
                            deviceName_install = new DeviceName_Install(item.id, ISnull);
                            deviceName_installs.add(deviceName_install);
                        }
                    } else {
                        boolean ss = false;
                        for (int i = 0; i < temporary_Str.size(); i++) {
                            if (temporary_Str.get(i).equals(item.id)) {
                                ss = true;
                                break;
                            }
                        }
                        if (ss) {
                            temporary_Str.add(item.id);
                            deviceName_install = new DeviceName_Install(item.id, ISnull);
                            deviceName_installs.add(deviceName_install);
                        }
                    }
                    boolean isopen = false;
                    for (int i = 0; i < deviceName_installs.size(); i++) {
                        if (item.id.equals(deviceName_installs.get(i).ID)) {
                            helper.setText(R.id.test1, deviceName_installs.get(i).Name);
                            isopen = true;
                            break;
                        }
                    }
                    if (!isopen) {
                        helper.setText(R.id.test1, item.id);
                    }
                } else {
                    if (deviceName_installs.size() > 0) {
                        for (int i = 0; i < deviceName_installs.size(); i++) {
                            if (item.id.equals(deviceName_installs.get(i).ID)) {
                                isture = true;
                                helper.setText(R.id.test1, deviceName_installs.get(i).Name);
                                break;
                            }
                        }
                    }
                    if (!isture) {
                        helper.setText(R.id.test1, item.id);
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
                        helper.setText(R.id.dataUint, "??");
                    }
                } else if (GetNOWVALUE.contains("Hz")) {
                    helper.setText(R.id.dataType, "??????");
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
                } else if (GetNOWVALUE.contains("M") && GetNOWVALUE.contains("??")) {
                    helper.setText(R.id.dataType, "??????");
                    helper.setText(R.id.dataUint, "M??");
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
                    helper.setText(R.id.dataType, "??????");
                    helper.setText(R.id.dataUint, "nF");
                } else if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("f")) {
                    helper.setText(R.id.dataType, "??????");
                    helper.setText(R.id.dataUint, "mF");
                } else if (GetNOWVALUE.contains("u") && GetNOWVALUE.contains("f")) {
                    helper.setText(R.id.dataType, "??????");
                    helper.setText(R.id.dataUint, "uF");
                } else if (GetNOWVALUE.contains("f")) {
                    helper.setText(R.id.dataType, "??????");
                    helper.setText(R.id.dataUint, "F");
                } else if (GetNOWVALUE.contains("k") && GetNOWVALUE.contains("??")) {
                    helper.setText(R.id.dataType, "??????");
                    helper.setText(R.id.dataUint, "k??");
                } else if (GetNOWVALUE.contains("Diode") && GetNOWVALUE.contains("V")) {
                    helper.setText(R.id.dataType, "?????????");
                    helper.setText(R.id.dataUint, "V");
                } else if (GetNOWVALUE.contains("??")) {
                    helper.setText(R.id.dataType, "??????");
                    helper.setText(R.id.dataUint, "??");

                } else if (GetNOWVALUE.contains("V") || GetNOWVALUE.contains("v")) {
                    helper.setText(R.id.dataUint, "V");
                }

                MyErrorLog.e("????????????",GetNOWVALUE);
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
                helper.setText(R.id.test3, item.getTemperature()); //??????
                helper.setText(R.id.test4, item.getTiem()); //??????
            }
        } catch (Exception exception) {
            MyErrorLog.e("??????????????? Item Error ", "info :" + exception.toString());
        }
    }
}
