package com.example.wirelesscapacitor.moudle.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wirelesscapacitor.MainActivity;
import com.example.wirelesscapacitor.MainBean;
import com.example.wirelesscapacitor.MyLog;
import com.example.wirelesscapacitor.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter extends BaseQuickAdapter<MainBean, BaseViewHolder> {
    public MainAdapter(@Nullable List<MainBean> data) {
        super(R.layout.item_main, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainBean item) {
        MainActivity.CurrentIndex = MainActivity.CurrentIndex + 1;
//        helper.setText(R.id.name, item.getId());//设备名
//        helper.setText(R.id.time, item.getTiem());//时间
//        helper.setText(R.id.data, item.getCapacitance());//数据
//        helper.setText(R.id.unit, item.getTemperature());//温度
        String capact = item.getCapacitance();
        Pattern pattern = Pattern.compile("\t|\r|\n|\\s*");
        Matcher matcher = pattern.matcher(capact);
        String dest = matcher.replaceAll("");
        String CutAuio = dest.replace("AUTO", "");
        String Now_ = CutAuio.replace("+", "");
        String Now__ = Now_.replace(" ", "");
        String GetNOWVALUE = Now__.substring(0, Now_.length() - 1);
        GetNOWVALUE.replace("-", "");
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
            helper.setText(R.id.dataType, "MΩ");
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
        }

        if (GetNOWVALUE.contains("ERRDC") || GetNOWVALUE.contains("ERRVC")) {
            helper.setText(R.id.dataValue, "ERR");
        }

        if (GetNOWVALUE.contains("-ERR")) {
            GetNOWVALUE.replace("-ERR", "");
            helper.setText(R.id.dataValue, GetNOWVALUE);
        }
        if (GetNOWVALUE.length() > 5) {
            String Cut_Str = GetNOWVALUE.replace("-", "");
            String CutString = Cut_Str.substring(0, 5);
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
        helper.setText(R.id.test1, item.getId()); //设备名
//        helper.setText(R.id.test2, item.getCapacitance()); //数据
        helper.setText(R.id.test3, item.getTemperature()); //温度
        helper.setText(R.id.test4, item.getTiem()); //时间
    }
}
