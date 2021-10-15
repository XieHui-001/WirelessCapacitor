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

import com.example.wirelesscapacitor.Historical_Data_Value;
import com.example.wirelesscapacitor.MainActivity;
import com.example.wirelesscapacitor.MyErrorLog;
import com.example.wirelesscapacitor.R;

import java.util.List;

public class Historical_Item extends RecyclerView.Adapter<Historical_Item.LinerViewHolder> {
    private Context context;
    private List<Historical_Data_Value> Historical_Item_List;

    public Historical_Item(Context context, List<Historical_Data_Value> item_List) {
        this.context = context;
        this.Historical_Item_List = item_List;
    }

    @NonNull
    @Override
    public LinerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinerViewHolder(LayoutInflater.from(context).inflate(R.layout.historical_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinerViewHolder holder, int position) {
        try {
//            String DeviceName = MainActivity.Instance.TimerTask_GetNewDeviceName(Historical_Item_List.get(position).getDeviceID());
            holder.devicename.setText(Historical_Item_List.get(position).getDeviceID());
            holder.datetype.setText(Historical_Item_List.get(position).getType());
            holder.datevalue.setText(Historical_Item_List.get(position).getValue());
            holder.dataunit.setText(Historical_Item_List.get(position).getUnit());
            holder.temp.setText(Historical_Item_List.get(position).getTemp());
            holder.newDate.setText(Historical_Item_List.get(position).getNowdate());
        } catch (Exception exception) {
            MyErrorLog.e("Old Data:", exception);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int getItemCount() {
        return Historical_Item_List.size();
    }

    class LinerViewHolder extends RecyclerView.ViewHolder {
        private TextView devicename;
        private TextView datetype;
        private TextView datevalue;
        private TextView dataunit;
        private TextView temp;
        private TextView newDate;

        public LinerViewHolder(@NonNull View itemView) {
            super(itemView);
            devicename = itemView.findViewById(R.id.device_view);
            datetype = itemView.findViewById(R.id.new_dataType);
            datevalue = itemView.findViewById(R.id.new_dataValue);
            dataunit = itemView.findViewById(R.id.new_dataUint);
            temp = itemView.findViewById(R.id.new_temp);
            newDate = itemView.findViewById(R.id.new_date);
        }
    }
}
