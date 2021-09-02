package com.example.wirelesscapacitor.moudle.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wirelesscapacitor.MainBean;
import com.example.wirelesscapacitor.R;

import java.util.List;

import androidx.annotation.Nullable;

public class MainAdapter extends BaseQuickAdapter<MainBean, BaseViewHolder> {

    public MainAdapter(@Nullable List<MainBean> data) {
        super(R.layout.item_main, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, MainBean item) {
        helper.setText(R.id.name, item.getId());//设备名
        helper.setText(R.id.time, item.getTiem());//时间
        helper.setText(R.id.data, item.getCapacitance());//数据
        helper.setText(R.id.unit, item.getTemperature());//温度
    }


}
