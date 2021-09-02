package com.example.wirelesscapacitor;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.wirelesscapacitor.moudle.adapter.MainAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tw.com.prolific.driver.pl2303.PL2303Driver;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_date)
    ImageView mainDate;
    @BindView(R.id.main_current)
    LinearLayout mainCurrent;
    @BindView(R.id.main_voltage)
    LinearLayout mainVoltage;
    @BindView(R.id.main_resistance)
    LinearLayout mainResistance;
    @BindView(R.id.historical_pictures_rv)
    RecyclerView historicalPicturesRv;
    @BindView(R.id.read_data)
    Button readData;


    private TimePickerView mPickerOptions;//时间选择器
    private long startingTime;//开始时间
    private long endTime;//结束时间
    private MainAdapter adapter;
    private List<MainBean> mData = new ArrayList<>();


    // debug settings
    // private static final boolean SHOW_DEBUG = false;
    private static final boolean SHOW_DEBUG = true;
    // Defines of Display Settings
    private static final int DISP_CHAR = 0;
    // Linefeed Code Settings
    //    private static final int LINEFEED_CODE_CR = 0;
    private static final int LINEFEED_CODE_CRLF = 1;
    private static final int LINEFEED_CODE_LF = 2;
    private PL2303Driver mSerial;
    //    private ScrollView mSvText;
    //   private StringBuilder mText = new StringBuilder();
    String TAG = "PL2303HXD_APLog";
    private Button btWrite;
    private EditText etWrite;
    private Button btRead;
    private EditText etRead;
    private Button btLoopBack;
    private ProgressBar pbLoopBack;
    private TextView tvLoopBack;
    private Button btGetSN;
    private TextView tvShowSN;
    private int mDisplayType = DISP_CHAR;
    private int mReadLinefeedCode = LINEFEED_CODE_LF;
    private int mWriteLinefeedCode = LINEFEED_CODE_LF;
    //BaudRate.B4800, DataBits.D8, StopBits.S1, Parity.NONE, FlowControl.RTSCTS
    private PL2303Driver.BaudRate mBaudrate = PL2303Driver.BaudRate.B9600;
    private PL2303Driver.DataBits mDataBits = PL2303Driver.DataBits.D8;
    private PL2303Driver.Parity mParity = PL2303Driver.Parity.NONE;
    private PL2303Driver.StopBits mStopBits = PL2303Driver.StopBits.S1;
    private PL2303Driver.FlowControl mFlowControl = PL2303Driver.FlowControl.OFF;
    private static final String ACTION_USB_PERMISSION = "com.prolific.pl2303hxdsimpletest.USB_PERMISSION";
    private static final String NULL = null;
    // Linefeed
    //    private final static String BR = System.getProperty("line.separator");
    public Spinner PL2303HXD_BaudRate_spinner;
    public int PL2303HXD_BaudRate;
    public String PL2303HXD_BaudRate_str = "B4800";
    private String strStr;
    private Timer timer;
    private TimerTask timerTask;
    private Handler handler = new Handler();


    private int i = 0;
    private int TIME = 1000; // 每隔1s执行
    Runnable runnable;
    //  private List<SerData> mData = new ArrayList<>();
    private MainBean mainBean;
    private String id;
    private String capacitance;
    private String time;
    private String temperature;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏标题栏以及状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE); /**标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题**/
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initCustomTimePicker();
        adapter = new MainAdapter(mData);
        historicalPicturesRv.setLayoutManager(new LinearLayoutManager(this));
        historicalPicturesRv.setAdapter(adapter);


        // get service
        mSerial = new PL2303Driver((UsbManager) getSystemService(Context.USB_SERVICE),
                this, ACTION_USB_PERMISSION);

        // check USB host function.
        if (!mSerial.PL2303USBFeatureSupported()) {


            Log.d(TAG, "No Support USB host API");

            mSerial = null;

        }


        handler.postDelayed(runnable, TIME); // 开启定时器更新UI


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    protected void onStop() {
        handler.removeCallbacks(runnable);//停止计时器
        Log.d(TAG, "Enter onStop");
        super.onStop();
        Log.d(TAG, "Leave onStop");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Enter onDestroy");

        if (mSerial != null) {
            mSerial.end();
            mSerial = null;
        }
        EventBus.getDefault().unregister(this);//注销消息监听
        super.onDestroy();
        Log.d(TAG, "Leave onDestroy");
    }

    public void onStart() {
        Log.d(TAG, "Enter onStart");
        super.onStart();
        Log.d(TAG, "Leave onStart");
    }

    public void onResume() {
        Log.d(TAG, "Enter onResume");
        super.onResume();
        String action = getIntent().getAction();
        Log.d(TAG, "onResume:" + action);

        //if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action))
        if (!mSerial.isConnected()) {
            if (SHOW_DEBUG) {
                Log.d(TAG, "New instance : " + mSerial);
            }

            if (!mSerial.enumerate()) {

                Toast.makeText(this, "no more devices found", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Log.d(TAG, "onResume:enumerate succeeded!");
            }
            try {
                Thread.sleep(1500);
                openUsbSerial();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }//if isConnected
        //Toast.makeText(this, "attached", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "Leave onResume");
    }


    private void openUsbSerial() {
        Log.d(TAG, "Enter  openUsbSerial");

        if (mSerial == null) {

            Log.d(TAG, "No mSerial");
            return;

        }


        if (mSerial.isConnected()) {
            if (SHOW_DEBUG) {
                Log.d(TAG, "openUsbSerial : isConnected ");
            }
//            String str = PL2303HXD_BaudRate_spinner.getSelectedItem().toString();
            int baudRate = 115200;
            switch (baudRate) {
                case 9600:
                    mBaudrate = PL2303Driver.BaudRate.B9600;
                    break;
                case 19200:
                    mBaudrate = PL2303Driver.BaudRate.B19200;
                    break;
                case 115200:
                    mBaudrate = PL2303Driver.BaudRate.B115200;
                    break;
                default:
                    mBaudrate = PL2303Driver.BaudRate.B9600;
                    break;
            }
            Log.d(TAG, "baudRate:" + baudRate);
            // if (!mSerial.InitByBaudRate(mBaudrate)) {
            if (!mSerial.InitByBaudRate(mBaudrate, 700)) {
                if (!mSerial.PL2303Device_IsHasPermission()) {
                    Toast.makeText(this, "cannot open, maybe no permission", Toast.LENGTH_SHORT).show();
                }

                if (mSerial.PL2303Device_IsHasPermission() && (!mSerial.PL2303Device_IsSupportChip())) {
                    Toast.makeText(this, "cannot open, maybe this chip has no support, please use PL2303HXD / RA / EA chip.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "cannot open, maybe this chip has no support, please use PL2303HXD / RA / EA chip.");
                }
            } else {

                Toast.makeText(this, "connected : OK", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "connected : OK");
                Log.d(TAG, "Exit  openUsbSerial");


            }
        }//isConnected
        else {
            Toast.makeText(this, "Connected failed, Please plug in PL2303 cable again!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "connected failed, Please plug in PL2303 cable again!");


        }
    }//openUsbSerial


    private Decoder decoder = new Decoder();

    private void readDataFromSerial() {
    //    decoder.Test();

        int len;
        // byte[] rbuf = new byte[4096];
        byte[] rbuf = new byte[512];
        StringBuffer sbHex = new StringBuffer();
        Log.d(TAG, "Enter readDataFromSerial");

        if (null == mSerial)
            return;

        if (!mSerial.isConnected())
            return;

        len = mSerial.read(rbuf);
        if (len < 0) {
            Log.d(TAG, "Fail to bulkTransfer(read data)");
            return;
        }


        decoder.Decode(rbuf, len);
    }//readDataFromSerial



    /**
     * 接收消息按钮状态
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(SwitchStatusEvent msg) {
        id = msg.getDepartId();
        capacitance = msg.getDepartCapacitance();
        time = msg.getDepartTime();
        temperature = msg.getDepartTemperature();

        List<MainBean> list = new ArrayList<>();
        MainBean device = new MainBean();
        device.setId(id);//设备id
        device.setTiem(time);//时间
        device.setCapacitance(capacitance);
        device.setTemperature(temperature);
        list.add(device);
        adapter.addData(list);
    }


    @OnClick({R.id.main_capacitance, R.id.main_date, R.id.main_current, R.id.main_voltage, R.id.main_resistance, R.id.read_data})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_date:
                mPickerOptions.show();
                break;
            case R.id.main_current:
                break;
            case R.id.main_voltage:
                break;
            case R.id.main_resistance:
                break;
            case R.id.read_data://读取数据
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        //要做的事情
                        readDataFromSerial();
                        handler.postDelayed(this, 2000);
                    }
                };
                handler.postDelayed(runnable, 2000);//每两秒执行一次runnable.
                break;

            case R.id.main_capacitance:
                break;
        }
    }


    /**
     * 初始化时间选择器
     */
    private void initCustomTimePicker() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();//开始时间
        startDate.add(Calendar.YEAR, -1);
        //时间选择器 ，自定义布局
        mPickerOptions = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调

                initEndTimePicker(date.getTime());

            }
        })
                .setDate(selectedDate)//设置默认选择时间
                .setRangDate(startDate, selectedDate)//设置开始时间  和结束时间
                //添加自定义布局的回调方法
                .setContentTextSize(18)
                .setTitleText("选择开始时间")//标题
                .setType(new boolean[]{true, true, true, false, false, false})//显示年月日时分秒 需要显示true
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(2.2f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();

    }

    /**
     * 结束时间
     */
    private void initEndTimePicker(long startTime) {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();//开始时间
        startDate.setTime(new Date(startTime));
        //时间选择器 ，自定义布局
        TimePickerView endPicker = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                Calendar start = Calendar.getInstance();
                start.setTime(new Date(startTime));
                start.set(Calendar.HOUR_OF_DAY, 0);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 1);
                Calendar end = Calendar.getInstance();
                end.setTime(date);

                startingTime = start.getTime().getTime();//开始
                endTime = end.getTime().getTime();//结束

            }
        })
                .setDate(selectedDate)//设置默认选择时间
                .setRangDate(startDate, selectedDate)//设置开始时间  和结束时间
                //添加自定义布局的回调方法
                .setContentTextSize(18)
                .setTitleText("选择结束日期")//标题
                .setType(new boolean[]{true, true, true, false, false, false})//显示年月日时分秒 需要显示true
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(2.2f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();
        endPicker.show();

    }


}