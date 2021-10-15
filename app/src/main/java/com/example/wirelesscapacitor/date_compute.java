package com.example.wirelesscapacitor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.util.List;

public class date_compute extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_compute);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public static void normalDialog(final Context context, List<Multidevice> data) {
        //得到自定义对话框
        final View DialogView = LayoutInflater.from(context).inflate(R.layout.activity_dialog_edit, null);
        String Index_1 = null;
        String Index_2 = null;
        String Index_3 = null;
        String total = Index_1 + "\n" + Index_2 + "\n" + Index_3;
        String Error = "Miscalculation";
        for (int i = 0; i < data.size(); i++) {
            if (i == 0) {
                Index_1 = data.get(i).capacitance;
            } else if (i == 1) {
                Index_2 = data.get(i).capacitance;
            } else if (i == 2) {
                Index_3 = data.get(i).capacitance;
            }
        }


        new AlertDialog.Builder(context).setTitle("计算当前设备数据")
                .setMessage(total.length() < 1 ? Error : total)
                .setIcon(R.drawable.sum)
                .setView(DialogView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setNegativeButton("取消", null).show();

    }

}