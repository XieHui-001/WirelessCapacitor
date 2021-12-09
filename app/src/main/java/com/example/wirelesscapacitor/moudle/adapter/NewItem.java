package com.example.wirelesscapacitor.moudle.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wirelesscapacitor.DeviceName_Install;
import com.example.wirelesscapacitor.Device_Info;
import com.example.wirelesscapacitor.Historical_Data_Value;
import com.example.wirelesscapacitor.MainActivity;
import com.example.wirelesscapacitor.MainBean;
import com.example.wirelesscapacitor.MyErrorLog;
import com.example.wirelesscapacitor.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewItem extends RecyclerView.Adapter<NewItem.LinerViewHolder> {
    private Context context;
    private List<MainBean> Historical_Item_List;
    private List<DeviceName_Install> deviceName_installs = new ArrayList<>();
    DeviceName_Install deviceName_install = null;
    private List<String> temporary_Str = new ArrayList<>();

    public NewItem(Context context, List<MainBean> item_List) {
        this.context = context;
        this.Historical_Item_List = item_List;
    }

    @NonNull
    @Override
    public NewItem.LinerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewItem.LinerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_main, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewItem.LinerViewHolder holder, int position) {
        try {
            boolean isture = false;
            DeviceName_Install deviceName_install = null;
            if (Device_Info.Instance.nOTnuLL == null || !Device_Info.Instance.nOTnuLL.equals(Historical_Item_List.get(position).id)) {
                final String ISnull = MainActivity.Instance.TimerTask_GetNewDeviceName(Historical_Item_List.get(position).id);
                if (ISnull != null) {
                    if (temporary_Str.size() < 1) {
                        if (!temporary_Str.contains(ISnull)) {
                            temporary_Str.add(ISnull);
                            deviceName_install = new DeviceName_Install(Historical_Item_List.get(position).id, ISnull);
                            deviceName_installs.add(deviceName_install);
                        }
                    } else {
                        boolean ss = false;
                        for (int i = 0; i < temporary_Str.size(); i++) {
                            if (temporary_Str.get(i).equals(Historical_Item_List.get(position).id)) {
                                ss = true;
                                break;
                            }
                        }
                        if (ss) {
                            temporary_Str.add(Historical_Item_List.get(position).id);
                            deviceName_install = new DeviceName_Install(Historical_Item_List.get(position).id, ISnull);
                            deviceName_installs.add(deviceName_install);
                        }
                    }
                    boolean isopen = false;
                    for (int i = 0; i < deviceName_installs.size(); i++) {
                        if (Historical_Item_List.get(position).id.equals(deviceName_installs.get(i).ID)) {
                            holder.test1.setText(deviceName_installs.get(i).Name);
                            isopen = true;
                            break;
                        }
                    }
                    if (!isopen) {
                        holder.test1.setText(Historical_Item_List.get(position).id);
                    }
                } else {
                    if (deviceName_installs.size() > 0) {
                        for (int i = 0; i < deviceName_installs.size(); i++) {
                            if (Historical_Item_List.get(position).id.equals(deviceName_installs.get(i).ID)) {
                                isture = true;
                                holder.test1.setText(deviceName_installs.get(i).Name);
                                break;
                            }
                        }
                    }
                    if (!isture) {
                        holder.test1.setText(Historical_Item_List.get(position).id);
                    }
                }
            } else {
                MyErrorLog.e("3333", "");
                holder.test1.setText(Historical_Item_List.get(position).id);
            }
            if (true) {
                Pattern pattern = Pattern.compile("\t|\r|\n|\\s*");
                Matcher matcher = pattern.matcher(Historical_Item_List.get(position).capacitance);
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
                    holder.dataType.setText("DC");
                    if (GetNOWVALUE.contains("ERR")) {
                        GetNOWVALUE.replace("ERR", "");
                        holder.dataUint.setText(GetNOWVALUE);
                    }
                } else if (GetNOWVALUE.contains("AC")) {
                    GetNOWVALUE.replace("AC", "");
                    holder.dataType.setText("AC");
                    if (GetNOWVALUE.contains("ERR")) {
                        GetNOWVALUE.replace("ERR", "");
                        holder.dataUint.setText("Ω");
                    }
                } else if (GetNOWVALUE.contains("Hz")) {
                    holder.dataType.setText("频率");
                } else if (GetNOWVALUE.contains("ERR") || GetNOWVALUE.contains("-ERR") || GetNOWVALUE.contains("?") || GetNOWVALUE.contains(".") || GetNOWVALUE.contains(":")) {
                    holder.dataValue.setText("ERR");
                    GetNOWVALUE.replace("ERR", "");
                    GetNOWVALUE.replace("-ERR", "");
                    GetNOWVALUE.replace("?", "");
                    GetNOWVALUE.replace(".", "");
                    GetNOWVALUE.replace(":", "");
                }

                if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("V")) {
                    holder.dataUint.setText("mV");
                } else if (GetNOWVALUE.contains("M") && GetNOWVALUE.contains("Ω")) {
                    holder.dataType.setText("电阻");
                    holder.dataUint.setText("MΩ");
                } else if (GetNOWVALUE.contains("Hz")) {
                    holder.dataUint.setText("Hz");
                } else if (GetNOWVALUE.contains("u") && GetNOWVALUE.contains("A")) {
                    holder.dataUint.setText("uA");
                } else if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("A")) {
                    holder.dataUint.setText("mA");
                } else if (GetNOWVALUE.contains("A") && GetNOWVALUE.contains("V")) {
                    holder.dataUint.setText("V");
                } else if (GetNOWVALUE.contains("A")) {
                    holder.dataUint.setText("A");
                } else if (GetNOWVALUE.contains("n") && GetNOWVALUE.contains("f")) {
                    holder.dataType.setText("电容");
                    holder.dataUint.setText("nF");
                } else if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("f")) {
                    holder.dataType.setText("电容");
                    holder.dataUint.setText("nF");
                } else if (GetNOWVALUE.contains("u") && GetNOWVALUE.contains("f")) {
                    holder.dataType.setText("电容");
                    holder.dataUint.setText("uF");
                } else if (GetNOWVALUE.contains("f")) {
                    holder.dataType.setText("电容");
                    holder.dataUint.setText("F");
                } else if (GetNOWVALUE.contains("k") && GetNOWVALUE.contains("Ω")) {
                    holder.dataType.setText("电阻");
                    holder.dataUint.setText("kΩ");
                } else if (GetNOWVALUE.contains("Diode") && GetNOWVALUE.contains("V")) {
                    holder.dataType.setText("二极管");
                    holder.dataUint.setText("V");
                } else if (GetNOWVALUE.contains("Ω")) {
                    holder.dataType.setText("电阻");
                    holder.dataUint.setText("Ω");

                }

                if (GetNOWVALUE.contains("ERRDC") || GetNOWVALUE.contains("ERRVC")) {
                    holder.dataValue.setText("ERR");
                }

                if (GetNOWVALUE.contains("-ERR")) {
                    GetNOWVALUE.replace("-ERR", "");
                    holder.dataValue.setText("GetNOWVALUE");
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
                        holder.dataValue.setText("ERR");
                    } else if (CutString.contains("?")) {
                        holder.dataValue.setText("ERR");
                    } else {
                        if (CutString.contains("ERRDC") || CutString.contains("ERRAC")) {
                            holder.dataValue.setText("ERR");
                        } else if (CutString.contains("?")) {
                            holder.dataValue.setText("ERR");
                        } else {
                            holder.dataValue.setText(CutString);
                        }
                    }
                }
                holder.test3.setText(Historical_Item_List.get(position).temperature);
                holder.test4.setText(Historical_Item_List.get(position).tiem);
            }
        } catch (Exception exception) {
            MyErrorLog.e("适配器错误 Item Error ", "info :" + exception.toString());
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int getItemCount() {
        return Historical_Item_List.size();
    }

    class LinerViewHolder extends RecyclerView.ViewHolder {
        private TextView test1;
        private TextView dataType;
        private TextView dataValue;
        private TextView dataUint;
        private TextView test3;
        private TextView test4;

        public LinerViewHolder(@NonNull View itemView) {
            super(itemView);
            test1 = itemView.findViewById(R.id.test1);
            dataType = itemView.findViewById(R.id.dataType);
            dataValue = itemView.findViewById(R.id.dataValue);
            dataUint = itemView.findViewById(R.id.dataUint);
            test3 = itemView.findViewById(R.id.test3);
            test4 = itemView.findViewById(R.id.test4);
        }
    }
}