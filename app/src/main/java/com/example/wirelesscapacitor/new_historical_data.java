package com.example.wirelesscapacitor;

import static android.view.KeyEvent.KEYCODE_BACK;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wirelesscapacitor.moudle.adapter.Historical_Item;

import org.apache.log4j.chainsaw.Main;

import java.util.ArrayList;
import java.util.List;

public class new_historical_data extends AppCompatActivity {
    private RecyclerView myRecyclerView;
    private Context context;
    private List<Historical_Data_Value> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_historical_data);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        myRecyclerView = (RecyclerView) findViewById(R.id.new_historical_item_);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(new_historical_data.this));
        myRecyclerView.setAdapter(new Historical_Item(new_historical_data.this, MainActivity.historical_data_values) {
        });
        TextView textView = (TextView) findViewById(R.id.historical_datatime);
        textView.setText("??????????????????:" + MainActivity.historical_data_info);
        LinearLayout back = (LinearLayout) findViewById(R.id.historical_date);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KEYCODE_BACK) {
            finish();
            return true;
        }
        return true;
    }
//
//    private Boolean ISBACK = true;
//
//    private boolean isCosumenBackKey() {
//        // ????????????????????????????????????????????????????????????????????????true???????????????false,????????????????????????back???????????????????????????
//        AlertDialog.Builder bb = new AlertDialog.Builder(this);
//        bb.setPositiveButton("??????", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Intent Href = new Intent();
//                Href.setClass(new_historical_data.this, MainActivity.class);
//                startActivity(Href);
//                ISBACK = false;
//            }
//        });
//        bb.setMessage("???????????????");
//        bb.setTitle("?????? ???????????? ????????????????????????????????????????????? ?????????????????????");
//        bb.show();
//        return ISBACK;
//    }
}