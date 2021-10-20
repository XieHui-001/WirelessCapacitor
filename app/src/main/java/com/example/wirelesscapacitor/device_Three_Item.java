package com.example.wirelesscapacitor;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class device_Three_Item extends RecyclerView.Adapter<device_Three_Item.LinearViewHolder> {
    private Context context;
    private List<Multidevice> Mylist;
    private List<String> GetDeviceName = new ArrayList<>();
    private List<String> NewName = new ArrayList<>();

    public device_Three_Item(Context context, List<Multidevice> mylist) {
        this.context = context;
        this.Mylist = mylist;
    }

    @NonNull
    @Override
    public LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_name_item,
//                parent, false);
//        LinearViewHolder holder = new LinearViewHolder(view);
        View view = LayoutInflater.from(context).inflate(R.layout.device_name_item, parent, false);
//        return  new leftViewHolde(view);
        return new LinearViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull device_Three_Item.LinearViewHolder holder, int position) {
        MyErrorLog.e("Get Data List Size", Mylist.size() + "::::当前设备id:" + Mylist.get(position).id + position);
        int indexof = position;
        try {
            if (!GetDeviceName.contains(Mylist.get(indexof).id)) {
                GetDeviceName.add(Mylist.get(indexof).id);
//                String NowValue = null;
//                NowValue = MainActivity.Instance.TimerTask_GetNewDeviceName(Mylist.get(position).id);
//                if (NowValue != null) {
//                    if (!NewName.contains(NowValue)) {
//                        // 添加设备名称 匹配
//                        NewName.add(NowValue);
//                        holder.NewDevieName_View.setText(NowValue == null ? Mylist.get(indexof).id : NowValue);
//                    }
//                }
                holder.NewDevieName_View.setText("设备:" + position);
                String capact = Mylist.get(position).capacitance;
                Pattern pattern = Pattern.compile("\t|\r|\n|\\s*");
                Matcher matcher = pattern.matcher(capact);
                String dest = matcher.replaceAll("");
                String CutAuio = dest.replace("AUTO", "");
                String Now_ = CutAuio.replace("", "");
                String Now__ = Now_.replace(" ", "");
                String GetNOWVALUE = Now__.substring(0, Now_.length() - 1);
                String IndexoFCut = GetNOWVALUE.substring(1, GetNOWVALUE.length());
                String NoeStr = GetNOWVALUE.substring(0, 1);
                String SUmStr = IndexoFCut.replace("-", " ");
                String NowValue_ = NoeStr + SUmStr;
                String regEx = "[`~!@#$%^&*()+=|{}':;'\\[\\]<>/?~！@#￥%……&*（）|{}【】'；：”“’。、？]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(NowValue_);
                String toSpeechText = m.replaceAll("").trim();
                if (toSpeechText.contains("ERR")) {
                    holder.capacitance_1_View.setText("ERR");
                } else if (toSpeechText.contains("?")) {
                    holder.capacitance_1_View.setText("ERR");
                } else if (toSpeechText.contains("m") && GetNOWVALUE.contains("V")) {
                    toSpeechText.replace("-m-V", "mV");
                    String NOwValue = toSpeechText;
                    holder.capacitance_1_View.setText(NOwValue);
                } else {
                    holder.capacitance_1_View.setText(toSpeechText);
                }
                MyErrorLog.e("获取异常数据", toSpeechText);
                //if (GetNOWVALUE.contains("AC")) {
                //                            NowImgSum.setImageResource(R.mipmap.ic_current);
                //                            NowElectricity.setText("交流电流");
                //                        } else if (GetNOWVALUE.contains("DC")) {
                //                            NowImgSum.setImageResource(R.mipmap.ic_voltage);
                //                            NowElectricity.setText("直流电流");
                //                        } else
                if (GetNOWVALUE.contains("Hz")) {
                    holder.ImgSum_1_View.setImageResource(R.drawable.hz);
                    holder.Electricity_1_View.setText("频率");
                } else if (GetNOWVALUE.contains("DC") && GetNOWVALUE.contains("V")) {
                    holder.ImgSum_1_View.setImageResource(R.mipmap.ic_current);
                    holder.Electricity_1_View.setText("直流电压");
                } else if (GetNOWVALUE.contains("DC") && GetNOWVALUE.contains("A")) {
                    holder.ImgSum_1_View.setImageResource(R.mipmap.ic_voltage);
                    holder.Electricity_1_View.setText("直流电流");
                } else if (GetNOWVALUE.contains("AC") && GetNOWVALUE.contains("V")) {
                    holder.ImgSum_1_View.setImageResource(R.mipmap.ic_current);
                    holder.Electricity_1_View.setText("交流电压");
                } else if (GetNOWVALUE.contains("AC") && GetNOWVALUE.contains("V")) {
                    holder.ImgSum_1_View.setImageResource(R.mipmap.ic_voltage);
                    holder.Electricity_1_View.setText("交流电流");
                } else if (GetNOWVALUE.contains("n") && GetNOWVALUE.contains("f")) {
                    holder.Electricity_1_View.setText("电容");
                } else if (GetNOWVALUE.contains("Diode") && GetNOWVALUE.contains("V")) {
                    holder.Electricity_1_View.setText("二极管");
                } else if (GetNOWVALUE.contains("k") && GetNOWVALUE.contains("Ω")) {
                    holder.ImgSum_1_View.setImageResource(R.mipmap.ic_resistance);
                    holder.Electricity_1_View.setText("电阻");
                } else if (GetNOWVALUE.contains("Ω")) {
                    holder.ImgSum_1_View.setImageResource(R.mipmap.ic_resistance);
                    holder.Electricity_1_View.setText("电阻");
                }
            }


            // 温湿度
            String NowTamps = Mylist.get(position).getTemperature();
            Pattern pattern = Pattern.compile("\t|\r|\n|\\s*");
            Matcher matcher = pattern.matcher(NowTamps);
            String dest = matcher.replaceAll("");
            String index_1 = dest.replace(" ", "");
            String index_2 = index_1.replace("Temp=", "");
            String index_3 = index_2.replace("Humi=", "");
            String sumindex = index_3;
            int CutLenght = sumindex.length() / 2;
            String Temp = index_3.substring(0, CutLenght);
            String humidity = index_3.substring(CutLenght, index_3.length());
            String humidity_Show = humidity + " RH %";
            humidity_Show.replace(" ", "");
            String CutString = "设备号:" + Mylist.get(position).id + ":数据:" + Mylist.get(position).capacitance + ":温度:" + Temp + ":湿度:" + humidity_Show + ":记录时间:" + Mylist.get(position).getTiem();
            Pattern pattern1 = Pattern.compile("\t|\r|\n|\\s*");
            Matcher matcher1 = pattern1.matcher(CutString);
            String dest1 = matcher1.replaceAll("");
            MyLog.e("", dest1.replace(" ", ""));
            holder.temps_1_View.setText(Temp.length() <= 0 ? Mylist.get(position).getTemperature() : Temp + " ℃");
            holder.humis_1_View.setText(humidity_Show.length() <= 0 ? Mylist.get(position).getTemperature() : humidity_Show);


            holder.update_new_View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String NowValue = Mylist.get(indexof).id;
                        if (NowValue.length() > 0) {
                            Boolean Succ = MainActivity.Instance.UpdateDeviceNamePublic(NowValue) == true;
                        }
                    } catch (Exception ex) {
                        MyErrorLog.e("Get Edit Value Error ", "::" + ex);
                    }
                }
            });
//            holder.sumitem_View.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    try {
//
//                    } catch (Exception exception) {
//
//                    }
//                }
//            });

            try {
                if (Mylist.size() > 1) {
                    String Device_Index = null;
                    String Device_Index_1 = null;
                    String Device_Index_2 = null;
                    String Index_1 = null;
                    String Index_2 = null;
                    String Index_3 = null;
                    String Error = "Do not need to calculate";
                    for (int i = 0; i < Mylist.size(); i++) {
                        if (i == 0) {
                            Index_1 = Mylist.get(i).capacitance;
                            Device_Index = Mylist.get(i).id;
                        } else if (i == 1) {
                            Index_2 = Mylist.get(i).capacitance;
                            Device_Index_1 = Mylist.get(i).id;
                        } else if (i == 2) {
                            Index_3 = Mylist.get(i).capacitance;
                            Device_Index_2 = Mylist.get(i).id;
                        }
                    }
                    if (Index_1 != null && Index_2 != null && Index_3 == null) {
                        String MyUint = SunUint(Index_1);
                        String MyType = SunType(Index_1);
                        String MyValue = SunValue(Index_1);

                        String MyUint_1 = SunUint(Index_2);
                        String MyType_1 = SunType(Index_2);
                        String MyValue_1 = SunValue(Index_2);
                        if (!MyValue.contains("ERR") && !MyValue_1.contains("ERR")) {
                            if (MyType.equals(MyType_1)) {
                                if (MyUint.equals(MyUint_1)) {
                                    float MyValue_int = Float.parseFloat(MyValue);
                                    float MyValue_int_1 = Float.parseFloat(MyValue_1);
                                    // 1 - 2
                                    float Count = (MyValue_int + MyValue_int_1) / 2;
                                    float QutCount = (MyValue_int - Count) / MyValue_int;
                                    float QutCOunt_Two = (MyValue_int_1 - Count) / MyValue_int_1;

                                    DecimalFormat decimalFormat = new DecimalFormat(".00");
                                    String _QutCount = decimalFormat.format(QutCount * 100);
                                    String _QutCOunt_Two = decimalFormat.format(QutCOunt_Two * 100);
                                    MyErrorLog.e("结算", Count);
                                    holder.sumitem_View.setText(_QutCount + "%" + "\n" + _QutCOunt_Two + "%");

                                } else {
                                    MyErrorLog.e("数据单位不一致", "ERR");
                                }
                            } else {
                                MyErrorLog.e("数据状态不匹配", "ERR");
                            }
                        } else {
                            MyErrorLog.e("异常数据无法解析", "存在ERR错误");
                        }
                    } else if (Index_1 != null && Index_2 != null && Index_3 != null) {
                        String MyUint = SunUint(Index_1);
                        String MyType = SunType(Index_1);
                        String MyValue = SunValue(Index_1);

                        String MyUint_1 = SunUint(Index_2);
                        String MyType_1 = SunType(Index_2);
                        String MyValue_1 = SunValue(Index_2);

                        String MyUint_2 = SunUint(Index_3);
                        String MyType_2 = SunType(Index_3);
                        String MyValue_2 = SunValue(Index_3);
                        if (!MyValue.contains("ERR") && !MyValue_1.contains("ERR") && !MyValue_2.contains("ERR")) {
                            if (MyType.equals(MyType_1) && MyType.equals(MyType_2)) {
                                if (MyUint.equals(MyUint_1) && MyUint.equals(MyUint_2)) {
                                    float MyValue_int = Float.parseFloat(MyValue);
                                    float MyValue_int_1 = Float.parseFloat(MyValue_1);
                                    float MyValue_int_2 = Float.parseFloat(MyValue_2);

                                    float SumValue = (MyValue_int + MyValue_int_1 + MyValue_int_2) / 3;
                                    float OneVlaue = (MyValue_int - SumValue) / MyValue_int;
                                    float TwoValue = (MyValue_int_1 - SumValue) / MyValue_int_1;
                                    float Thre = (MyValue_int_2 - SumValue) / MyValue_int_2;


                                    DecimalFormat decimalFormat = new DecimalFormat(".00");
                                    String _OneVlaue = decimalFormat.format(OneVlaue * 100);
                                    String _TwoValue = decimalFormat.format(TwoValue * 100);
                                    String _Thre = decimalFormat.format(Thre * 100);
//                                    // 1 - 2;
//                                    float Count = MyValue_int - MyValue_int_1;
//                                    // 1 - 3
//                                    float Count_1 = MyValue_int - MyValue_int_2;
//                                    // 2 -3
//                                    float Count_2 = MyValue_int_1 - MyValue_int_2;
//                                    MyErrorLog.e("结算", Count);
                                    holder.sumitem_View.setText("\n" + _OneVlaue + "%" + "\n" + _TwoValue + "%" + "\n" + _Thre + "%");
//                                holder.sumitem_View.setText("设备1:" + Device_Index + "\n设备2" + Device_Index_1 + "\n设备3" + Device_Index_2 + "\n数据差值：\n设备1 - 设备2：" + Count + "\n设备1 - 设备3: " + Count_1 + "\n设备2 - 设备三: " + Count_2);
                                }
                            }
                        } else {
                            MyErrorLog.e("异常数据无法解析", "存在ERR错误");
                        }
                    }
                } else {
                    holder.sumitem_View.setText("0");
                }
            } catch (Exception exception) {
                // 计算错误
                MyErrorLog.e("Multiple device data calculation errors", "Err Logs:" + exception);
            }
        } catch (Exception exception) {
            MyErrorLog.e("Now Get Capacitance Found ", "Error info " + exception);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int getItemCount() {
        return Mylist.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        // Odl Value
//        private TextView item_device_name_View;
//        private TextView item_device_data_View; //updatedevice
//        private ImageView updatedevice_View;
//        private TextView temp_View;
//        private TextView humi_View;
//        private EditText edit_value_View;
//        private ImageView sundataView;
        private ImageView ImgSum_1_View;
        private TextView capacitance_1_View;
        private TextView temps_1_View;
        private TextView humis_1_View;
        private TextView sumitem_View;
        private ImageView update_new_View;
        private TextView NewDevieName_View;
        private TextView Electricity_1_View;

        public LinearViewHolder(View itemView) {
            super(itemView);
            // odl View
//            item_device_name_View = itemView.findViewById(R.id.item_device_name);
//            item_device_data_View = itemView.findViewById(R.id.item_device_data);
//            updatedevice_View = itemView.findViewById(R.id.updatedevice);
//            temp_View = itemView.findViewById(R.id.temp);
//            humi_View = itemView.findViewById(R.id.humi);
//            sundataView = itemView.findViewById(R.id.sumdevice);
//            edit_value_View = itemView.findViewById(R.id.edit_value);

            // New View
            ImgSum_1_View = itemView.findViewById(R.id.ImgSum_1);
            capacitance_1_View = itemView.findViewById(R.id.capacitance_1);
            temps_1_View = itemView.findViewById(R.id.temps_1);
            humis_1_View = itemView.findViewById(R.id.humis_1);
            sumitem_View = itemView.findViewById(R.id.sumitem);
            update_new_View = itemView.findViewById(R.id.update_new);
            NewDevieName_View = itemView.findViewById(R.id.NewDevieName);
            Electricity_1_View = itemView.findViewById(R.id.Electricity_1);

        }
    }


    // 单位
    private String SunUint(String data) {
        String dataType = null;
        String dataUint = "";
        String dataValue = null;
        Pattern pattern = Pattern.compile("\t|\r|\n|\\s*");
        Matcher matcher = pattern.matcher(data);
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
            dataType = "DC";
            if (GetNOWVALUE.contains("ERR")) {
                GetNOWVALUE.replace("ERR", "");
                dataUint = GetNOWVALUE;
            }
        } else if (GetNOWVALUE.contains("AC")) {
            GetNOWVALUE.replace("AC", "");
            dataType = "AC";
            if (GetNOWVALUE.contains("ERR")) {
                GetNOWVALUE.replace("ERR", "");
                dataUint = "Ω";
            }
        } else if (GetNOWVALUE.contains("Hz")) {
            dataType = "频率";
        } else if (GetNOWVALUE.contains("ERR") || GetNOWVALUE.contains("-ERR") || GetNOWVALUE.contains("?") || GetNOWVALUE.contains(".") || GetNOWVALUE.contains(":")) {
            dataValue = "ERR";
            GetNOWVALUE.replace("ERR", "");
            GetNOWVALUE.replace("-ERR", "");
            GetNOWVALUE.replace("?", "");
            GetNOWVALUE.replace(".", "");
            GetNOWVALUE.replace(":", "");
        }

        if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("V")) {
            dataUint = "mV";
        } else if (GetNOWVALUE.contains("M") && GetNOWVALUE.contains("Ω")) {
            dataType = "电阻";
            dataUint = "MΩ";
        } else if (GetNOWVALUE.contains("Hz")) {
            dataUint = "Hz";
        } else if (GetNOWVALUE.contains("u") && GetNOWVALUE.contains("A")) {
            dataUint = "uA";
        } else if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("A")) {
            dataUint = "mA";
        } else if (GetNOWVALUE.contains("A") && GetNOWVALUE.contains("V")) {
            dataUint = "V";
        } else if (GetNOWVALUE.contains("A")) {
            dataUint = "A";
        } else if (GetNOWVALUE.contains("n") && GetNOWVALUE.contains("f")) {
            dataType = "电容";
            dataUint = "nF";
        } else if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("f")) {
            dataType = "电容";
            dataUint = "mF";
        } else if (GetNOWVALUE.contains("u") && GetNOWVALUE.contains("f")) {
            dataType = "电容";
            dataUint = "uF";
        } else if (GetNOWVALUE.contains("f")) {
            dataType = "电容";
            dataUint = "F";
        } else if (GetNOWVALUE.contains("k") && GetNOWVALUE.contains("Ω")) {
            dataType = "电阻";
            dataUint = "kΩ";
        } else if (GetNOWVALUE.contains("Diode") && GetNOWVALUE.contains("V")) {
            dataType = "二极管";
            dataUint = "V";
        } else if (GetNOWVALUE.contains("Ω")) {
            dataType = "电阻";
            dataUint = "Ω";

        }

        if (GetNOWVALUE.contains("ERRDC") || GetNOWVALUE.contains("ERRVC")) {
            dataValue = "ERR";
        }

        if (GetNOWVALUE.contains("-ERR")) {
            GetNOWVALUE.replace("-ERR", "");
            dataValue = GetNOWVALUE;
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
                dataValue = "ERR";
            } else if (CutString.contains("?")) {
                dataValue = "ERR";
            } else {
                if (CutString.contains("ERRDC") || CutString.contains("ERRAC")) {
                    dataValue = "ERR";
                } else if (CutString.contains("?")) {
                    dataValue = "ERR";
                } else {
                    dataValue = CutString;
                }
            }
        }
        return dataUint;
    }


    private String SunType(String data) {
        String dataType = null;
        String dataUint = null;
        String dataValue = null;
        Pattern pattern = Pattern.compile("\t|\r|\n|\\s*");
        Matcher matcher = pattern.matcher(data);
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
            dataType = "DC";
            if (GetNOWVALUE.contains("ERR")) {
                GetNOWVALUE.replace("ERR", "");
                dataUint = GetNOWVALUE;
            }
        } else if (GetNOWVALUE.contains("AC")) {
            GetNOWVALUE.replace("AC", "");
            dataType = "AC";
            if (GetNOWVALUE.contains("ERR")) {
                GetNOWVALUE.replace("ERR", "");
                dataUint = "Ω";
            }
        } else if (GetNOWVALUE.contains("Hz")) {
            dataType = "频率";
        } else if (GetNOWVALUE.contains("ERR") || GetNOWVALUE.contains("-ERR") || GetNOWVALUE.contains("?") || GetNOWVALUE.contains(".") || GetNOWVALUE.contains(":")) {
            dataValue = "ERR";
            GetNOWVALUE.replace("ERR", "");
            GetNOWVALUE.replace("-ERR", "");
            GetNOWVALUE.replace("?", "");
            GetNOWVALUE.replace(".", "");
            GetNOWVALUE.replace(":", "");
        }

        if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("V")) {
            dataUint = "mV";
        } else if (GetNOWVALUE.contains("M") && GetNOWVALUE.contains("Ω")) {
            dataType = "电阻";
            dataUint = "MΩ";
        } else if (GetNOWVALUE.contains("Hz")) {
            dataUint = "Hz";
        } else if (GetNOWVALUE.contains("u") && GetNOWVALUE.contains("A")) {
            dataUint = "uA";
        } else if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("A")) {
            dataUint = "mA";
        } else if (GetNOWVALUE.contains("A") && GetNOWVALUE.contains("V")) {
            dataUint = "V";
        } else if (GetNOWVALUE.contains("A")) {
            dataUint = "A";
        } else if (GetNOWVALUE.contains("n") && GetNOWVALUE.contains("f")) {
            dataType = "电容";
            dataUint = "nF";
        } else if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("f")) {
            dataType = "电容";
            dataUint = "mF";
        } else if (GetNOWVALUE.contains("u") && GetNOWVALUE.contains("f")) {
            dataType = "电容";
            dataUint = "uF";
        } else if (GetNOWVALUE.contains("f")) {
            dataType = "电容";
            dataUint = "F";
        } else if (GetNOWVALUE.contains("k") && GetNOWVALUE.contains("Ω")) {
            dataType = "电阻";
            dataUint = "kΩ";
        } else if (GetNOWVALUE.contains("Diode") && GetNOWVALUE.contains("V")) {
            dataType = "二极管";
            dataUint = "V";
        } else if (GetNOWVALUE.contains("Ω")) {
            dataType = "电阻";
            dataUint = "Ω";

        }

        if (GetNOWVALUE.contains("ERRDC") || GetNOWVALUE.contains("ERRVC")) {
            dataValue = "ERR";
        }

        if (GetNOWVALUE.contains("-ERR")) {
            GetNOWVALUE.replace("-ERR", "");
            dataValue = GetNOWVALUE;
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
                dataValue = "ERR";
            } else if (CutString.contains("?")) {
                dataValue = "ERR";
            } else {
                if (CutString.contains("ERRDC") || CutString.contains("ERRAC")) {
                    dataValue = "ERR";
                } else if (CutString.contains("?")) {
                    dataValue = "ERR";
                } else {
                    dataValue = CutString;
                }
            }
        }
        return dataType;
    }

    private String SunValue(String data) {
        String dataType = null;
        String dataUint = null;
        String dataValue = null;
        Pattern pattern = Pattern.compile("\t|\r|\n|\\s*");
        Matcher matcher = pattern.matcher(data);
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
            dataType = "DC";
            if (GetNOWVALUE.contains("ERR")) {
                GetNOWVALUE.replace("ERR", "");
                dataUint = GetNOWVALUE;
            }
        } else if (GetNOWVALUE.contains("AC")) {
            GetNOWVALUE.replace("AC", "");
            dataType = "AC";
            if (GetNOWVALUE.contains("ERR")) {
                GetNOWVALUE.replace("ERR", "");
                dataUint = "Ω";
            }
        } else if (GetNOWVALUE.contains("Hz")) {
            dataType = "频率";
        } else if (GetNOWVALUE.contains("ERR") || GetNOWVALUE.contains("-ERR") || GetNOWVALUE.contains("?") || GetNOWVALUE.contains(".") || GetNOWVALUE.contains(":")) {
            dataValue = "ERR";
            GetNOWVALUE.replace("ERR", "");
            GetNOWVALUE.replace("-ERR", "");
            GetNOWVALUE.replace("?", "");
            GetNOWVALUE.replace(".", "");
            GetNOWVALUE.replace(":", "");
        }

        if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("V")) {
            dataUint = "mV";
        } else if (GetNOWVALUE.contains("M") && GetNOWVALUE.contains("Ω")) {
            dataType = "电阻";
            dataUint = "MΩ";
        } else if (GetNOWVALUE.contains("Hz")) {
            dataUint = "Hz";
        } else if (GetNOWVALUE.contains("u") && GetNOWVALUE.contains("A")) {
            dataUint = "uA";
        } else if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("A")) {
            dataUint = "mA";
        } else if (GetNOWVALUE.contains("A") && GetNOWVALUE.contains("V")) {
            dataUint = "V";
        } else if (GetNOWVALUE.contains("A")) {
            dataUint = "A";
        } else if (GetNOWVALUE.contains("n") && GetNOWVALUE.contains("f")) {
            dataType = "电容";
            dataUint = "nF";
        } else if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("f")) {
            dataType = "电容";
            dataUint = "mF";
        } else if (GetNOWVALUE.contains("u") && GetNOWVALUE.contains("f")) {
            dataType = "电容";
            dataUint = "uF";
        } else if (GetNOWVALUE.contains("f")) {
            dataType = "电容";
            dataUint = "F";
        } else if (GetNOWVALUE.contains("k") && GetNOWVALUE.contains("Ω")) {
            dataType = "电阻";
            dataUint = "kΩ";
        } else if (GetNOWVALUE.contains("Diode") && GetNOWVALUE.contains("V")) {
            dataType = "二极管";
            dataUint = "V";
        } else if (GetNOWVALUE.contains("Ω")) {
            dataType = "电阻";
            dataUint = "Ω";

        }

        if (GetNOWVALUE.contains("ERRDC") || GetNOWVALUE.contains("ERRVC")) {
            dataValue = "ERR";
        }

        if (GetNOWVALUE.contains("-ERR")) {
            GetNOWVALUE.replace("-ERR", "");
            dataValue = GetNOWVALUE;
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
                dataValue = "ERR";
            } else if (CutString.contains("?")) {
                dataValue = "ERR";
            } else {
                if (CutString.contains("ERRDC") || CutString.contains("ERRAC")) {
                    dataValue = "ERR";
                } else if (CutString.contains("?")) {
                    dataValue = "ERR";
                } else {
                    dataValue = CutString;
                }
            }
        }
        return dataValue;
    }

}
