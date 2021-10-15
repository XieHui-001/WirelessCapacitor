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
import android.widget.TextView;

import java.io.File;

public class dialog_edit extends AppCompatActivity {
    private String GetDeviceNameValue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_edit);
    }

    public static void normalDialog(final Context context, final EditText editText, String id) {
        //得到自定义对话框
        final View DialogView = LayoutInflater.from(context).inflate(R.layout.activity_dialog_edit, null);
        //初始化edit值，把当前值显示到对话框里。
        final EditText init = (EditText) DialogView.findViewById(R.id.et);
        init.setText(editText.getText());
        String nul = MainActivity.Instance.TimerTask_GetNewDeviceName(id);
        //控制光标显示位置
        init.setSelection(init.getText().length());
        new AlertDialog.Builder(context).setTitle("修改当前连接设备名称")
                .setMessage(nul == null ? id : nul)
                .setIcon(R.drawable.update)
                .setView(DialogView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        File dirPath = Environment.getExternalStorageDirectory();
                        File Fils = new File(dirPath + "/LocalAppLogs/log/Device/Device.log");
                        if (!Fils.exists()) {
                            LocalDeviceName.e("", MainBean.Instance.getId() + ":" + init.getText());
                        } else {
                            DeviceName.Instance.setDeviceName(init.getText().toString());
                            MainActivity.Instance.NewTimerTask_GetNewDeviceName(init.getText().toString(), id);
                            MainActivity.Instance.ToskInfo(true);
                        }
                    }
                }).setNegativeButton("取消", null).show();

    }
}