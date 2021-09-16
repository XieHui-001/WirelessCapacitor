package com.example.wirelesscapacitor;

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

public class dialog_edit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_edit);
    }

    public static void normalDialog(final Context context, final EditText editText){
        //得到自定义对话框
        final View DialogView = LayoutInflater.from(context).inflate(R.layout.activity_dialog_edit, null);
        //初始化edit值，把当前值显示到对话框里。
        final EditText init = (EditText)DialogView.findViewById(R.id.et);
        init.setText(editText.getText());
        //控制光标显示位置
        init.setSelection(init.getText().length());
        new AlertDialog.Builder(context).setTitle("修改当前连接设备名称")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(DialogView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        //输入后点击“确定”，开始获取我们要的内容 DialogView就是AlertDialog弹出的Activity
//                        editText.setText(init.getText().toString());
//                        //控制光标显示位置
//                        editText.setSelection(editText.getText().length());
                        File dirPath = Environment.getExternalStorageDirectory();
                        File Fils = new File(dirPath + "/LocalAppLogs/log/Device/Device.log");
                        if (!Fils.exists()){
                            LocalDeviceName.e("",MainBean.Instance.getId()+":"+init.getText());
                        }
                        DeviceName.Instance.setDeviceName(init.getText().toString());
                        MainActivity.Instance.NewTimerTask_GetNewDeviceName(init.getText().toString());
                    }
                }).setNegativeButton("取消",null).show();
    }
}