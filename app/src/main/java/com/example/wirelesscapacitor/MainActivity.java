package com.example.wirelesscapacitor;

import static android.view.KeyEvent.KEYCODE_BACK;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.wirelesscapacitor.moudle.adapter.MainAdapter;
import com.example.wirelesscapacitor.moudle.adapter.NewItem;
import com.wang.avi.AVLoadingIndicatorView;

import org.apache.log4j.chainsaw.Main;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.w3c.dom.Entity;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import cz.msebera.android.httpclient.util.EntityUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import tw.com.prolific.driver.pl2303.PL2303Driver;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_date)
    LinearLayout mainDate;
    @BindView(R.id.main_current)
    LinearLayout mainCurrent;
    @BindView(R.id.main_voltage)
    LinearLayout mainVoltage;
    //    @BindView(R.id.main_resistance)
//    LinearLayout mainResistance;
    @BindView(R.id.historical_pictures_rv)
    RecyclerView historicalPicturesRv;
    @BindView(R.id.read_data)
    Button readData;
//    @BindView(R.id.Device_1)
//    TextView Device_1;
//    @BindView(R.id.Device_2)
//    TextView Device_2;
//    @BindView(R.id.Device_3)
//    TextView Device_3;
//    @BindView(R.id.historical_data)
//    ImageView historical_data;


    private TimePickerView mPickerOptions;//???????????????
    private long startingTime;//????????????
    private long endTime;//????????????
    private MainAdapter adapter;
    private device_Three_Item adapter_2;
    private List<MainBean> mData = new ArrayList<>();

    private List<ValueUtil> valueUtilList = new ArrayList<>();
    public static List<Historical_Data_Value> historical_data_values = new ArrayList<>();
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
    private int TIME = 1000; // ??????1s??????
    Runnable runnable;
    //  private List<SerData> mData = new ArrayList<>();
    private MainBean mainBean;
    private String id;
    private String capacitance;
    private String time;
    private String temperature;
    public static int CurrentIndex = 1;

    private String NowTemp = null;
    private String NowHumi = null;
    private RecyclerView recyclerView;

    public static MainActivity Instance = null;
    private String Remote_update_address = null;
    private String RemoteAppVersion = null;
    private String Remote_description = null;
    private String Remote_AppName = null;
    private Context context;
    private AVLoadingIndicatorView avi;
    private TimePickerView pvTime;
    public static String historical_data_info = null;
    public static String NewDeviceName = null;
    public static List<String> StrList = new ArrayList<>();
    private RecyclerView myRecy;
    private PowerManager.WakeLock wakeLock = null;

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private ReadWriteLock rwl = new ReentrantReadWriteLock();
    private List<MainBean> MAIN = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//??????????????????????????????
        requestWindowFeature(Window.FEATURE_NO_TITLE); /**???????????????View?????????????????????????????????????????????????????????????????????,??????????????????**/
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, MainActivity.class.getName());
//        wakeLock.acquire();
        context = MainActivity.this;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        OpenAnimata();
//        initCustomTimePicker();
        verifyStoragePermissions(MainActivity.this);
//        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(mLayoutManager);

        InitNew();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.updateDeviceName);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new MainAdapter(mData);
        historicalPicturesRv.setHasFixedSize(true);
        historicalPicturesRv.setNestedScrollingEnabled(false);
        historicalPicturesRv.setLayoutManager(new LinearLayoutManager(this));
        historicalPicturesRv.setAdapter(adapter);
//        historicalPicturesRv.setAdapter(new NewItem(MainActivity.this, MAIN));
        ReloadView();
//        EditText UpdateEdit = (EditText) findViewById(R.id.input_deviceName);
//        UpdateEdit.setBackgroundColor(Color.argb(255, 0, 255, 0));
        try {
            OkHttpVersionRequst();
//            isUIProcess();
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
        }


        // Start Timer Just Get Tamp Hum Data
//        Timer NowTimer = new Timer();
//        NowTimer.schedule(ViewTask, 1000, 1000);
        Timer Scroll = new Timer();
        Scroll.schedule(ScroolView, 200, 200);

        Timer test = new Timer();
        test.schedule(Adapter_item, 1000, 1000);
//        Timer DeviceNames = new Timer();
//        DeviceNames.schedule(DeviceTimerTask, 2000, 2000);


//        try {
//            if (MainBean.Instance.id!=null && MainBean.Instance.id!=""){
//                TimerTask_GetNewDeviceName();
//            }
//        }catch (Exception exception){
//            MyErrorLog.e("???????????????????????????","????????????????????????");
//        }
        ImageView historical_data = (ImageView) findViewById(R.id.historical_data);
        historical_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTimePicker();
                pvTime.show();
            }
        });

        TextView temBut = (TextView) findViewById(R.id.TextTemBut);
        temBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainBean.Instance.getTemperature() != null) {
                    AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("????????????")//??????
                            .setMessage(MainBean.Instance.getTemperature() + "??????C??")//??????
                            .setIcon(R.drawable.tem)//??????
                            .create();
                    alertDialog1.show();
                }
            }
        });
        TextView UpdateDeviceName = (TextView) findViewById(R.id.devicename);
        UpdateDeviceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
//                    if (MainBean.Instance.id != null && MainBean.Instance.id != "") {
//                        String GetDeviceNameValue = null;
//
//                        //updatetest
//                        EditText device_updatetest = (EditText) findViewById(R.id.updatetest);
//                        TextView device_count = (TextView) findViewById(R.id.devicecount);
//                        TextView device_data = (TextView) findViewById(R.id.devicedata);
//                        if (multideviceForDeviceNameList.size() > 0) {
//                            for (int i = 0; i < multideviceForDeviceNameList.size(); i++) {
//                                if (!StrList.contains(multideviceForDeviceNameList.get(i).name)) {
//                                    StrList.add(multideviceForDeviceNameList.get(i).name);
////                                    GetDeviceNameValue = GetDeviceNameValue + "|" + multideviceForDeviceNameList.get(i).name;
//                                }
//                            }
//                        }
//                        for (int i = 0; i < StrList.size(); i++) {
//                            GetDeviceNameValue = "|" + StrList.get(i);
//                        }
//                        device_data.setText(GetDeviceNameValue);
//                        device_count.setText(StrList.size() + "");
//                        EditText device_TextNames = (EditText) findViewById(R.id.DeviceInfo);
//                        dialog_edit.normalDialog(MainActivity.this, device_TextNames, MainBean.Instance.id);
//                    }
//                } catch (Exception exception) {
//                    MyErrorLog.e("????????????????????????", exception.toString());
//                }
            }
        });
        LinearLayout Realod = (LinearLayout) findViewById(R.id.reloadView);
        Realod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "?????????????????????.....", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, reload_view.class));
            }
        });

        LinearLayout imgdate = (LinearLayout) findViewById(R.id.main_date);
        imgdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "?????????????????????....", Toast.LENGTH_SHORT).show();
                OpenExcel();
            }
        });
        // get service
        mSerial = new PL2303Driver((UsbManager) getSystemService(Context.USB_SERVICE),
                this, ACTION_USB_PERMISSION);

        // check USB host function.
        if (!mSerial.PL2303USBFeatureSupported()) {
            Log.d(TAG, "No Support USB host API");
            mSerial = null;

        }
        handler.postDelayed(runnable, TIME); // ?????????????????????UI
    }

    private void OpenAnimata() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    setContentView(R.layout.activity_main);
//                    String indicator = getIntent().getStringExtra("game");
//                    avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
//                    avi.setIndicator(indicator);
//                    avi.hide();


                    TextView dalog = (TextView) findViewById(R.id.dalog);
                    dalog.setVisibility(View.INVISIBLE);
                } catch (Exception exception) {
                    MyErrorLog.e("?????????????????????????????????", exception);
                }
            }
        }).start();
    }

    //    private void Index(){
//        if (mData!=null){
//        recyclerView.scrollToPosition(CurrentIndex);
//        LinearLayoutManager mLayoutManager =  (LinearLayoutManager) recyclerView.getLayoutManager();
//        mLayoutManager.scrollToPositionWithOffset(CurrentIndex, 0);
//        }
//    }
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

    public MainActivity() {
        MainActivity.Instance = this;
    }

    protected void onStop() {
        handler.removeCallbacks(runnable);//???????????????
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
        EventBus.getDefault().unregister(this);//??????????????????
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
                Toast.makeText(this, "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
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
                    MyErrorLog.d(TAG, "cannot open, maybe this chip has no support, please use PL2303HXD / RA / EA chip.");
                }
            } else {
                Toast.makeText(this, "connected : OK", Toast.LENGTH_SHORT).show();
                MyErrorLog.d(TAG, "connected : OK");
                MyErrorLog.d(TAG, "Exit  openUsbSerial");
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        //???????????????
                        readDataFromSerial();
                        handler.postDelayed(this, 1000);
                    }
                };
                handler.postDelayed(runnable, 1000);
            }
        }//isConnected
        else {
            Toast.makeText(this, "Connected failed, Please plug in PL2303 cable again!", Toast.LENGTH_SHORT).show();
            MyErrorLog.d(TAG, "connected failed, Please plug in PL2303 cable again!");
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

    public List<Multidevice> multidevices = new ArrayList<>();
    private List<MultideviceForDeviceName> multideviceForDeviceNameList = new ArrayList<>();
    private List<String> temporary_Str = new ArrayList<>();

    /**
     * ????????????????????????   /?????????
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(SwitchStatusEvent msg) {
        rwl.writeLock().lock();
        try {
            if (!temporary_Str.contains(msg.getDepartId())) {
                temporary_Str.add(msg.getDepartId());
                Multidevice multidevice = new Multidevice(msg.getDepartId(), msg.getDepartCapacitance(), msg.getDepartTemperature(), msg.getDepartTime());
                multidevices.add(multidevice);
                MyErrorLog.e("????????????", "Success" + multidevices.size());
            } else {
                MyErrorLog.e("????????????", "333");
                for (int i = 0; i < multidevices.size(); i++) {
                    MyErrorLog.e(multidevices.get(i).id + "----" + i, msg.getDepartId());
                    if (multidevices.get(i).id.equals(msg.getDepartId())) {
                        MyErrorLog.e("???????????? ???" + i, multidevices.get(i));
                        multidevices.remove(multidevices.get(i));
                    }
                }
                id = msg.getDepartId();
                Multidevice multidevice = new Multidevice(msg.getDepartId(), msg.getDepartCapacitance(), msg.getDepartTemperature(), msg.getDepartTime());
                multidevices.add(multidevice);
            }
            capacitance = msg.getDepartCapacitance();
            time = msg.getDepartTime();
            temperature = msg.getDepartTemperature();
            for (int i = 0; i < MAIN.size(); i++) {
                if (MAIN.get(i).id.equals(id)) {
                    MAIN.remove(i);
                }
            }
//            MainBean device = new MainBean(id, time, capacitance, temperature);
//            MAIN.add(device);
            List<MainBean> list = new ArrayList<>();
            MainBean device = new MainBean();
            device.setId(id);//??????id
            device.setTiem(time);//??????
            device.setCapacitance(capacitance);
            device.setTemperature(temperature);
            list.add(device);
            adapter.addData(list);

//            historicalPicturesRv.setAdapter(new NewItem(MainActivity.this, MAIN));
            MyErrorLog.e("??????????????????", id + ":" + capacitance + ":" + temperature + ":" + time);
        } catch (Exception exception) {
            MyErrorLog.e("??????????????????", exception);
        } finally {
            rwl.writeLock().unlock();
        }
    }


    @OnClick({R.id.main_capacitance, R.id.main_current, R.id.main_voltage, R.id.read_data})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //, R.id.main_resistance
//            case R.id.main_date:
//
////                mPickerOptions.show();
//                break;
            case R.id.main_current:
                break;
            case R.id.main_voltage:
                break;
//            case R.id.main_resistance:
//                break;
            case R.id.read_data://????????????

                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        //???????????????
                        readDataFromSerial();
                        handler.postDelayed(this, 1000);
                    }
                };
                handler.postDelayed(runnable, 1000);//?????????????????????runnable.
                break;

            case R.id.main_capacitance:
                break;
        }
    }


    /**
     * ????????????????????????
     */
    private void initCustomTimePicker() {
        Calendar selectedDate = Calendar.getInstance();//??????????????????
        Calendar startDate = Calendar.getInstance();//????????????
        startDate.add(Calendar.YEAR, -1);
        //??????????????? ??????????????????
        mPickerOptions = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//??????????????????
                // ????????????????????????
                initEndTimePicker(date.getTime());
            }
        })
                .setDate(selectedDate)//????????????????????????
                .setRangDate(startDate, selectedDate)//??????????????????  ???????????????
                //????????????????????????????????????
                .setContentTextSize(14)
                .setTitleText("??????????????????")//??????
                .setType(new boolean[]{true, true, true, false, false, false})//???????????????????????? ????????????true
                .setLabel("???", "???", "???", "???", "???", "???")
                .setLineSpacingMultiplier(2.2f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //?????????????????????????????????label?????????false?????????item???????????????label???
                .setDividerColor(0xFF24AD9D)
                .build();

    }

    /**
     * ????????????
     */
    private void initEndTimePicker(long startTime) {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();//????????????
        startDate.setTime(new Date(startTime));
        //??????????????? ??????????????????
        TimePickerView endPicker = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//??????????????????
                Calendar start = Calendar.getInstance();
                start.setTime(new Date(startTime));
                start.set(Calendar.HOUR_OF_DAY, 0);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 1);
                Calendar end = Calendar.getInstance();
                end.setTime(date);

                startingTime = start.getTime().getTime();//??????
                endTime = end.getTime().getTime();//??????

            }
        })
                .setDate(selectedDate)//????????????????????????
                .setRangDate(startDate, selectedDate)//??????????????????  ???????????????
                //????????????????????????????????????
                .setContentTextSize(18)
                .setTitleText("??????????????????")//??????
                .setType(new boolean[]{true, true, true, false, false, false})//???????????????????????? ????????????true
                .setLabel("???", "???", "???", "???", "???", "???")
                .setLineSpacingMultiplier(2.2f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //?????????????????????????????????label?????????false?????????item???????????????label???
                .setDividerColor(0xFF24AD9D)
                .build();
        endPicker.show();

    }


    // Now TimerTask Get Data
    TimerTask ViewTask = new TimerTask() {
        @Override
        public void run() {
            ThreadTask();
        }
    };

    private void ThreadTask() {
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                SxView();
            }
        }, 1, TimeUnit.SECONDS);
    }

    TimerTask ScroolView = new TimerTask() {
        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (MainBean.Instance.getTemperature() != null) {
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.historical_pictures_rv);
                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.scrollToPosition(mData.size() - 1);
                            }

                        });
//                    ScrollView scrollView=(ScrollView) findViewById(R.id.scrollView);
//                    scrollView.post(new Runnable() {
//                    public void run() {
//                        scrollView.fullScroll(scrollView.FOCUS_DOWN);
//                    }
//                });
//                    scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                        @Override
//                        public void onGlobalLayout() {
//                            scrollView.post(new Runnable() {
//                                public void run() {
//                                    scrollView.fullScroll(View.FOCUS_DOWN);
//                                }
//                            });
//                        }
//                    });
                    }
                }
            });

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        startActivityForResult(intent, 10086);

    }

    private void AviHide() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                avi.hide();
            }
        });
    }

    // Enabling external Applications Just Now
    public void _ApplicationsForStart() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (getPackageManager().canRequestPackageInstalls()) {
                    File dir = Environment.getExternalStorageDirectory();
                    String path = dir.getPath() + "/";//Android_Test
                    File dirs = new File(path + "/1/app-debug.apk");
                    openFile(MainActivity.this, dirs.getPath(), "camera.photos.fileprovider");
                    MyErrorLog.d("privilege", "Install Apk");
                } else {
                    // ???????????????
                    startInstallPermissionSettingActivity();
                    MyErrorLog.d("privilege", "Applying for System Permissions");
                }
            }
        } catch (Exception exception) {
            MyErrorLog.d("privilege Error", "System Errore: " + exception);
        }
    }

    // pathName ????????????
    // authority  ???????????????
    public static void openFile(Context context, String pathName, String authority) {
        try {
            if (pathName == null) {
                return;
            }
            File file = new File(pathName);
            if (file == null || !file.exists()) {
                return;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(FileProvider.getUriForFile(context, authority, file), "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
        } catch (Exception exception) {
            MyErrorLog.d("System File", "Failed to apply for system permission. Procedure:" + exception);
        }
    }

    // ????????????
    private final int REQUEST_EXTERNAL_STORAGE = 1;
    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    private void ViewTst() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TextView Ts = (TextView) findViewById(R.id.dalog);
                Ts.setVisibility(View.VISIBLE);
            }
        }).start();

    }

    private void OpenExcel() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    Time time = new Time("GMT+8");
                    time.setToNow();
                    int year = time.year;
                    int month = time.month;
                    int day = time.monthDay;
                    int minute = time.minute;
                    int hour = time.hour;
                    int sec = time.second;
                    if (month == 0) {
                        month = 1;
                    } else {
                        month = month + 1;
                    }

                    new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            ViewTst();
                            if (month == 0) {
                                month = 1;
                            }
                            List<ValueUtil> valueUtilList = new ArrayList<>();
                            String data = String.format("%d-%d-%d", year, month + 1, dayOfMonth);//???????????????
                            String OutFileName = data + "ClassExeLogs.log";
                            OutFileName.replace(" ", "");
                            MyErrorLog.d("File Name:", OutFileName);
                            File dir = Environment.getExternalStorageDirectory();
                            File dirs = new File(dir.getPath() + "/LocalAppLogs/log/" + OutFileName + "");
                            MyErrorLog.d("File Path", dirs.getPath());
                            if (dirs.exists()) {
                                MyErrorLog.e("??????", "????????????");
                                File file = new File(Environment.getExternalStorageDirectory(),
                                        "LocalAppLogs/log/Export/" + data + ".xls");
                                if (!file.exists()) {
                                    FileInputStream fileInputStream = null;
                                    try {
                                        // 1.????????????
                                        fileInputStream = new FileInputStream(dirs);
                                        // 2.????????? ????????????byte 10001??? -----> ?????????????????????ASCLL ?????????
                                        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                                        String line = null;
                                        StringBuilder builder = new StringBuilder();
                                        ExecutorService executorService = Executors.newFixedThreadPool(5);
                                        while ((line = reader.readLine()) != null) {
                                            String finalLine = line;
                                            Runnable syncRunnable = new Runnable() {
                                                @Override
                                                public void run() {
//                                            MyErrorLog.e("??????:", Thread.currentThread().getName());
                                                    builder.append(finalLine);
                                                    try {
                                                        String DataType = null;
                                                        String DataUnit = null;
                                                        String DataValues = null;
                                                        String CutIndexOF_1 = finalLine.replace(" ", "");
                                                        String compare = CutIndexOF_1.substring(CutIndexOF_1.indexOf("?????????"), CutIndexOF_1.length());
                                                        String CutDevice = compare.replace("?????????:", "?????????");
                                                        // ?????????
                                                        String CutIndexOF_2 = CutDevice.substring(CutDevice.indexOf("?????????") + 3, CutDevice.indexOf(":"));

                                                        // ??????
                                                        String CutIndexOF_3 = compare.replace("?????????:" + CutIndexOF_2 + ":??????:", "??????");
                                                        String DataValue = CutIndexOF_3.substring(CutIndexOF_3.indexOf("??????") + 2, CutIndexOF_3.indexOf(":"));
                                                        Pattern pattern = Pattern.compile("\t|\r|\n|\\s*");
                                                        Matcher matcher = pattern.matcher(DataValue);
                                                        String dest = matcher.replaceAll("");
                                                        String CutAuio = dest.replace("AUTO", "");
                                                        String Now_ = CutAuio.replace("+", "");
                                                        String Now__ = Now_.replace(" ", "");
                                                        String GetNOWVALUE = Now__.substring(0, Now_.length() - 1);
                                                        GetNOWVALUE.replace("-", "");
                                                        if (GetNOWVALUE.contains("??")) {
                                                            DataType = "??????";
                                                        } else if (GetNOWVALUE.contains("Diode")) {
                                                            DataType = "?????????";
                                                        } else if (GetNOWVALUE.contains("DC")) {
                                                            GetNOWVALUE.replace("DC", "");
                                                            DataType = "DC";
                                                            if (GetNOWVALUE.contains("ERR")) {
                                                                GetNOWVALUE.replace("ERR", "");
                                                                DataUnit = GetNOWVALUE;
                                                            }
                                                        } else if (GetNOWVALUE.contains("AC")) {
                                                            GetNOWVALUE.replace("AC", "");
                                                            DataType = "AC";
                                                            if (GetNOWVALUE.contains("ERR")) {
                                                                GetNOWVALUE.replace("ERR", "");
                                                                DataUnit = "ERR";
                                                            }
                                                        } else if (GetNOWVALUE.contains("Hz")) {
                                                            DataType = "??????";
                                                        } else if (GetNOWVALUE.contains("ERR") || GetNOWVALUE.contains("-ERR") || GetNOWVALUE.contains("?") || GetNOWVALUE.contains(".") || GetNOWVALUE.contains(":")) {
                                                            DataValues = "ERR";
                                                            GetNOWVALUE = GetNOWVALUE.replace("ERR", "");
                                                            GetNOWVALUE = GetNOWVALUE.replace("-ERR", "");
//                                            GetNOWVALUE = GetNOWVALUE.replace("?", "");
//                                            GetNOWVALUE = GetNOWVALUE.replace(":", "");
                                                        }

                                                        if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("V")) {
                                                            DataUnit = "mV";
                                                        } else if (GetNOWVALUE.contains("M") && GetNOWVALUE.contains("Q")) {
                                                            DataType = "M??";
                                                            DataUnit = "M??";
                                                        } else if (GetNOWVALUE.contains("Hz")) {
                                                            DataUnit = "Hz";
                                                        } else if (GetNOWVALUE.contains("u") && GetNOWVALUE.contains("A")) {
                                                            DataUnit = "uA";
                                                        } else if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("A")) {
                                                            DataUnit = "mA";
                                                        } else if (GetNOWVALUE.contains("A") && GetNOWVALUE.contains("V")) {
                                                            DataUnit = "V";
                                                        } else if (GetNOWVALUE.contains("n") && GetNOWVALUE.contains("f")) {
                                                            DataType = "??????";
                                                            DataUnit = "nF";
                                                        } else if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("f")) {
                                                            DataType = "??????";
                                                            DataUnit = "mF";
                                                        } else if (GetNOWVALUE.contains("u") && GetNOWVALUE.contains("f")) {
                                                            DataType = "??????";
                                                            DataUnit = "uF";
                                                        } else if (GetNOWVALUE.contains("f")) {
                                                            DataType = "??????";
                                                            DataUnit = "F";
                                                        } else if (GetNOWVALUE.contains("k") && GetNOWVALUE.contains("??")) {
                                                            DataType = "??????";
                                                            DataUnit = "k??";
                                                        } else if (GetNOWVALUE.contains("Diode") && GetNOWVALUE.contains("V")) {
                                                            DataType = "?????????";
                                                            DataUnit = "V";
                                                        } else if (GetNOWVALUE.contains("M") && GetNOWVALUE.contains("??")) {
                                                            DataType = "??????";
                                                            DataUnit = "M??";
                                                        } else if (GetNOWVALUE.contains("??")) {
                                                            DataType = "??????";
                                                            DataUnit = "??";
                                                        } else if (GetNOWVALUE.contains("A")) {
                                                            DataUnit = "A";
                                                        }
                                                        if (GetNOWVALUE.contains("ERRDC") || GetNOWVALUE.contains("ERRVC")) {
                                                            DataValues = "ERR";
                                                        }

                                                        if (GetNOWVALUE.contains("-ERR")) {
                                                            GetNOWVALUE.replace("-ERR", "");
                                                            DataUnit = GetNOWVALUE;
                                                        }
                                                        if (GetNOWVALUE.length() >= 4) {
                                                            String Cut_Str = GetNOWVALUE.replace("-", "");
                                                            String CutString = Cut_Str.substring(0, 5);
                                                            if (CutString.contains("ERRDC") || CutString.contains("ERRVC")) {
                                                                DataValues = "ERR";
                                                            } else if (CutString.contains("?")) {
                                                                DataValues = "ERR";
                                                            } else {
                                                                if (CutString.contains("ERRDC") || CutString.contains("ERRAC")) {
                                                                    DataValues = "ERR";
                                                                } else if (CutString.contains("?")) {
                                                                    DataValues = "ERR";
                                                                } else {
                                                                    DataValues = CutString;
                                                                }
                                                            }
                                                        }

                                                        String CutIndexOF_4 = compare.replace("?????????:" + CutIndexOF_2 + ":??????:" + DataValue + "", "");
                                                        String CutIndexOF_5 = CutIndexOF_4.replace(":??????:", "??????");
                                                        // ??????
                                                        String DataTemp = CutIndexOF_5.substring(CutIndexOF_5.indexOf("??????") + 2, CutIndexOF_5.indexOf(":"));

                                                        String CutIndexOF_6 = compare.replace("?????????:" + CutIndexOF_2 + ":??????:" + DataValue + ":??????:" + DataTemp + "", "");
                                                        String CutIndexOF_7 = CutIndexOF_6.replace(":??????:", "??????");
                                                        // ??????
                                                        String DataHumi = CutIndexOF_7.substring(CutIndexOF_7.indexOf("??????") + 2, CutIndexOF_7.indexOf(":"));

                                                        String CutIndexOF_8 = compare.replace("?????????:" + CutIndexOF_2 + ":??????:" + DataValue + ":??????:" + DataTemp + ":??????:" + DataHumi + "", "");
                                                        String CutIndexOF_9 = CutIndexOF_8.replace(":????????????:", "????????????");
                                                        // ????????????
                                                        String DataTime = CutIndexOF_9.substring(CutIndexOF_9.indexOf("????????????") + 4, CutIndexOF_9.length());

                                                        if (DataType == null) {
                                                            DataType = "ERR";
                                                        }
                                                        if (DataValues == null) {
                                                            DataValues = "ERR";
                                                        }
                                                        if (DataUnit == null) {
                                                            DataUnit = "ERR";
                                                        }
                                                        if (DataUnit.contains("-ERRDC-m-V") || DataUnit.contains("-ERRAC-m-V")) {
                                                            DataUnit = "mV";
                                                        }
                                                        ValueUtil valueUtil = new ValueUtil(CutIndexOF_2, DataValues, DataType, DataUnit, DataTemp, DataHumi, DataTime);
                                                        valueUtilList.add(valueUtil);
                                                    } catch (Exception exception) {
                                                        OpenAnimata();
                                                        MyErrorLog.e("??????????????????", "????????????:" + exception);
                                                    }
                                                }
                                            };
                                            executorService.execute(syncRunnable);
                                        }
                                        String[] title = {"?????????", "????????????", "?????????", "??????", "??????", "??????", "??????"};
                                        String fileName = "" + data + ".xls";
                                        File dir1 = Environment.getExternalStorageDirectory();
                                        String path = dir1.getPath() + "/";
                                        File dirs1 = new File(path + "LocalAppLogs/log/Export/" + fileName + "");
                                        ExcelUtil.initExcel(fileName, "????????????", title);
                                        ExcelUtil.writeObjListToExcel(valueUtilList, dirs1.getPath(), MainActivity.this);
                                    } catch (Exception e) {
                                        MyErrorLog.e("Now Date", "File of found" + e);
                                        OpenAnimata();
                                        e.printStackTrace();
                                        AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                                                .setTitle("" + data + " ??????????????????????????????")//??????
                                                .setMessage(e.toString())//??????
                                                .setIcon(R.drawable.error_data)//??????
                                                .create();
                                        alertDialog1.show();
                                    } finally {
                                        if (fileInputStream != null) {
                                            try {
                                                fileInputStream.close();
                                                OpenAnimata();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                } else {
//                            Toast.makeText(MainActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                                    AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                                            .setTitle("??????????????????")//??????
                                            .setMessage("???????????????" + file.getPath())
                                            .setIcon(R.drawable.yes)//??????
                                            .create();
                                    alertDialog1.show();
                                }
                            } else {
                                AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("??????????????????????????????")//??????
                                        .setMessage(data + "?????????????????????????????????")
                                        .setIcon(R.drawable.notfile)//??????
                                        .create();
                                alertDialog1.show();
//                        Toast.makeText(MainActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                                MyErrorLog.e("File of found", "this path of found" + dirs.getPath());
                            }

                        }
                    }, year, month - 1, day).show();

                } catch (Exception exception) {
                    OpenAnimata();
                    Log.d(TAG, "Time Error: " + exception);
                }
                Looper.loop();
            }
        }).start();
    }

    private void SxView() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView TempValue = (TextView) findViewById(R.id.temps);
                TextView HumiValue = (TextView) findViewById(R.id.humis);
                TextView capacitance = (TextView) findViewById(R.id.capacitance);
                TextView NowElectricity = (TextView) findViewById(R.id.Electricity);
                ImageView NowImgSum = (ImageView) findViewById(R.id.ImgSum);
                if (MainBean.Instance.getTemperature() != null) {
                    try {
                        String NowTamps = MainBean.Instance.getTemperature();
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
                        String CutString = "?????????:" + MainBean.Instance.getId() + ":??????:" + MainBean.Instance.getCapacitance() + ":??????:" + Temp + ":??????:" + humidity_Show + ":????????????:" + MainBean.Instance.getTiem();
                        Pattern pattern1 = Pattern.compile("\t|\r|\n|\\s*");
                        Matcher matcher1 = pattern1.matcher(CutString);
                        String dest1 = matcher1.replaceAll("");
                        MyLog.e("", dest1.replace(" ", ""));
                        HumiValue.setText(humidity_Show);
                        TempValue.setText(Temp + "???");
                    } catch (Exception ex) {
                        MyErrorLog.e("Error info", ex.toString());
                    }
                }
                if (MainBean.Instance.getCapacitance() != null) {
                    try {
                        String capact = MainBean.Instance.getCapacitance();
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
                        String regEx = "[`~!@#$%^&*()+=|{}':;'\\[\\]<>/?~???@#???%??????&*??????|{}??????'????????????????????????]";
                        Pattern p = Pattern.compile(regEx);
                        Matcher m = p.matcher(NowValue_);
                        String toSpeechText = m.replaceAll("").trim();
                        if (toSpeechText.contains("ERR")) {
                            capacitance.setText("ERR");
                        } else if (toSpeechText.contains("?")) {
                            capacitance.setText("ERR");
                        } else if (toSpeechText.contains("m") && GetNOWVALUE.contains("V")) {
                            toSpeechText.replace("-m-V", "mV");
                            String NOwValue = toSpeechText;
                            capacitance.setText(NOwValue);
                        } else {
                            capacitance.setText(toSpeechText);
                        }
                        MyErrorLog.e("??????????????????", toSpeechText);
                        //if (GetNOWVALUE.contains("AC")) {
                        //                            NowImgSum.setImageResource(R.mipmap.ic_current);
                        //                            NowElectricity.setText("????????????");
                        //                        } else if (GetNOWVALUE.contains("DC")) {
                        //                            NowImgSum.setImageResource(R.mipmap.ic_voltage);
                        //                            NowElectricity.setText("????????????");
                        //                        } else
                        if (GetNOWVALUE.contains("Hz")) {
                            NowImgSum.setImageResource(R.drawable.hz);
                            NowElectricity.setText("??????");
                        } else if (GetNOWVALUE.contains("DC") && GetNOWVALUE.contains("V")) {
                            NowImgSum.setImageResource(R.mipmap.ic_current);
                            NowElectricity.setText("????????????");
                        } else if (GetNOWVALUE.contains("DC") && GetNOWVALUE.contains("A")) {
                            NowImgSum.setImageResource(R.mipmap.ic_voltage);
                            NowElectricity.setText("????????????");
                        } else if (GetNOWVALUE.contains("AC") && GetNOWVALUE.contains("V")) {
                            NowImgSum.setImageResource(R.mipmap.ic_current);
                            NowElectricity.setText("????????????");
                        } else if (GetNOWVALUE.contains("AC") && GetNOWVALUE.contains("V")) {
                            NowImgSum.setImageResource(R.mipmap.ic_voltage);
                            NowElectricity.setText("????????????");
                        } else if (GetNOWVALUE.contains("n") && GetNOWVALUE.contains("f")) {
                            NowElectricity.setText("??????");
                        } else if (GetNOWVALUE.contains("Diode") && GetNOWVALUE.contains("V")) {
                            NowElectricity.setText("?????????");
                        } else if (GetNOWVALUE.contains("k") && GetNOWVALUE.contains("??")) {
                            NowImgSum.setImageResource(R.mipmap.ic_resistance);
                            NowElectricity.setText("??????");
                        } else if (GetNOWVALUE.contains("??")) {
                            NowImgSum.setImageResource(R.mipmap.ic_resistance);
                            NowElectricity.setText("??????");
                        }
                    } catch (Exception exception) {
                        MyErrorLog.e("Now Get Capacitance Found ", "Error info " + exception);
                    }

                }

//                ScrollView scrollView=(ScrollView) findViewById(R.id.scrollView);
//                scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//
//                    public void onGlobalLayout() {
//                        scrollView.post(new Runnable() {
//                            public void run() {
//                                scrollView.fullScroll(View.FOCUS_DOWN);
//                            }
//                        });
//                    }
//                });
//                scrollView.post(new Runnable() {
//                    public void run() {
//                        scrollView.fullScroll(View.FOCUS_DOWN);
//                    }
//                });
            }
        });
    }

    private void OkHttpVersionRequst() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    int NwtWrok = NetUtil.getNetWorkStart(MainActivity.this);
                    if (NwtWrok > 0) {
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                        RequestBody body = RequestBody.create(mediaType, "name=??????????????????&department=????????????&plat=android");
                        Request request = new Request.Builder()
                                .url("http://version.cqset.com/api/sys/version/check_version?name=??????????????????&department=????????????&plat=android")
                                .method("POST", body)
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            String jsonData = response.body().string();
                            JSONObject jsonObject1 = null;
                            jsonObject1 = (JSONObject) JSON.parseObject(jsonData);
                            String Getdata = jsonObject1.getString("data");
                            jsonObject1 = JSON.parseObject(Getdata);
                            String VersionNow = jsonObject1.getString("version");
                            String DonwloadPath = jsonObject1.getString("path");
                            String description = jsonObject1.getString("description");
                            String AppName = jsonObject1.getString("app_name");
                            Remote_update_address = DonwloadPath;
                            RemoteAppVersion = VersionNow;
                            Remote_description = description;
                            Remote_AppName = AppName;
                            int Version = Integer.parseInt(VersionNow.replace(" ", ""));
                            int LocalVersion = getVersionCode(context);
                            MyErrorLog.e("????????????:", "" + LocalVersion);
                            if (Version > LocalVersion) {
                                MyErrorLog.e("?????????????????????????????????:", "" + LocalVersion);
                                UpdateTip();
                            }
                            MyErrorLog.e("API Response ", Getdata + "----" + VersionNow + "----" + DonwloadPath);
                        } catch (IOException e) {
                            MyErrorLog.e("API Resqus Error", "info:  " + e);
                            e.printStackTrace();
                        }
                    } else {
                        MyErrorLog.e("????????????????????????", "???????????????");
                    }
                } catch (Exception exception) {
                    MyErrorLog.e("Net Wrok", "Error:   " + exception);
                }
            }
        });
    }

    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    //??????PackageInfo???????????????????????????????????????
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pInfo = null;
        try {
            //??????PackageManager????????????PackageInfo
            PackageManager pManager = context.getPackageManager();
            pInfo = pManager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pInfo;
    }


    private void UpdateTip() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("????????????????????????????????????:" + Remote_description + "  ?????????" + RemoteAppVersion + "???????????????");
        File dir = Environment.getExternalStorageDirectory();
        File dirs = new File(dir.getPath() + "/1/app-debug.apk");
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dirs.exists()) {
                    dirs.delete();
                }
                OkHttpDonwloadTask(Remote_update_address);
            }
        });
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ??????????????????
                Toast.makeText(MainActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void RedTxtValue(String info) {
        try {
            String DataType = null;
            String DataUnit = null;
            String DataValues = null;
            String CutIndexOF_1 = info.replace(" ", "");
            String compare = CutIndexOF_1.substring(CutIndexOF_1.indexOf("?????????"), CutIndexOF_1.length());
            String CutDevice = compare.replace("?????????:", "?????????");
            // ?????????
            String CutIndexOF_2 = CutDevice.substring(CutDevice.indexOf("?????????") + 3, CutDevice.indexOf(":"));

            // ??????
            String CutIndexOF_3 = compare.replace("?????????:" + CutIndexOF_2 + ":??????:", "??????");
            String DataValue = CutIndexOF_3.substring(CutIndexOF_3.indexOf("??????") + 2, CutIndexOF_3.indexOf(":"));
            Pattern pattern = Pattern.compile("\t|\r|\n|\\s*");
            Matcher matcher = pattern.matcher(DataValue);
            String dest = matcher.replaceAll("");
            String CutAuio = dest.replace("AUTO", "");
            String Now_ = CutAuio.replace("+", "");
            String Now__ = Now_.replace(" ", "");
            String GetNOWVALUE = Now__.substring(0, Now_.length() - 1);
            GetNOWVALUE.replace("-", "");
            if (GetNOWVALUE.contains("DC")) {
                GetNOWVALUE.replace("DC", "");
                DataType = "DC";
                if (GetNOWVALUE.contains("ERR")) {
                    GetNOWVALUE.replace("ERR", "");
                    DataUnit = GetNOWVALUE;
                }
            } else if (GetNOWVALUE.contains("AC")) {
                GetNOWVALUE.replace("AC", "");
                DataType = "AC";
                if (GetNOWVALUE.contains("ERR")) {
                    GetNOWVALUE.replace("ERR", "");
                    DataUnit = "??";
                }
            } else if (GetNOWVALUE.contains("Hz")) {
                DataType = "??????";
            } else if (GetNOWVALUE.contains("ERR") || GetNOWVALUE.contains("-ERR") || GetNOWVALUE.contains("?") || GetNOWVALUE.contains(".") || GetNOWVALUE.contains(":")) {
                DataValues = "??";
                GetNOWVALUE.replace("ERR", "");
                GetNOWVALUE.replace("-ERR", "");
                GetNOWVALUE.replace("?", "");
                GetNOWVALUE.replace(".", "");
                GetNOWVALUE.replace(":", "");
            }

            if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("V")) {
                DataUnit = "mV";
            } else if (GetNOWVALUE.contains("M") && GetNOWVALUE.contains("Q")) {
                DataType = "M??";
                DataUnit = "M??";
            } else if (GetNOWVALUE.contains("Hz")) {
                DataUnit = "Hz";
            } else if (GetNOWVALUE.contains("u") && GetNOWVALUE.contains("A")) {
                DataUnit = "uA";
            } else if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("A")) {
                DataUnit = "mA";
            } else if (GetNOWVALUE.contains("A") && GetNOWVALUE.contains("V")) {
                DataUnit = "V";
            } else if (GetNOWVALUE.contains("A")) {
                DataUnit = "A";
            }

            if (GetNOWVALUE.contains("ERRDC") || GetNOWVALUE.contains("ERRVC")) {
                DataValues = "??";
            }

            if (GetNOWVALUE.contains("-ERR")) {
                GetNOWVALUE.replace("-ERR", "");
                DataUnit = GetNOWVALUE;
            }
            if (GetNOWVALUE.length() > 5) {
                String Cut_Str = GetNOWVALUE.replace("-", "");
                String CutString = Cut_Str.substring(0, 5);
                if (CutString.contains("ERRDC") || CutString.contains("ERRVC")) {
                    DataValues = "??";
                } else if (CutString.contains("?")) {
                    DataValues = "??";
                } else {
                    if (CutString.contains("ERRDC") || CutString.contains("ERRAC")) {
                        DataValues = "??";
                    } else if (CutString.contains("?")) {
                        DataValues = "??";
                    } else {
                        DataValues = CutString;
                    }
                }
            }
            String CutIndexOF_4 = compare.replace("?????????:" + CutIndexOF_2 + ":??????:" + DataValue + "", "");
            String CutIndexOF_5 = CutIndexOF_4.replace(":??????:", "??????");
            // ??????
            String DataTemp = CutIndexOF_5.substring(CutIndexOF_5.indexOf("??????") + 2, CutIndexOF_5.indexOf(":"));

            String CutIndexOF_6 = compare.replace("?????????:" + CutIndexOF_2 + ":??????:" + DataValue + ":??????:" + DataTemp + "", "");
            String CutIndexOF_7 = CutIndexOF_6.replace(":??????:", "??????");
            // ??????
            String DataHumi = CutIndexOF_7.substring(CutIndexOF_7.indexOf("??????") + 2, CutIndexOF_7.indexOf(":"));

            String CutIndexOF_8 = compare.replace("?????????:" + CutIndexOF_2 + ":??????:" + DataValue + ":??????:" + DataTemp + ":??????:" + DataHumi + "", "");
            String CutIndexOF_9 = CutIndexOF_8.replace(":????????????:", "????????????");
            // ????????????
            String DataTime = CutIndexOF_9.substring(CutIndexOF_9.indexOf("????????????") + 4, CutIndexOF_9.length());
            if (DataType == null) {
                DataType = "??";
            }
            if (DataValues == null) {
                DataValues = "??";
            }
            if (DataUnit == null) {
                DataUnit = "??";
            }
            ValueUtil valueUtil = new ValueUtil(CutIndexOF_2, DataType, DataValues, DataUnit, DataTemp, DataHumi, DataTime);
            valueUtilList.add(valueUtil);
        } catch (Exception exception) {
            MyErrorLog.e("??????????????????", "????????????:" + exception);
        }
    }

    private void OkHttpDonwloadTask(String Url) {
        // ?????????????????? ????????????
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyErrorLog.e("????????????", Url);
                //"http://download.talkdoo.com/iVRealFTPTest/OVMClass_Level2_U4_Android/game/Win64/bin/iVRealEdc-armv7.apk"
                final String url = Url;
                final long startTime = System.currentTimeMillis();
                MyErrorLog.i("DOWNLOAD", "startTime=" + startTime);
                Request request = new Request.Builder().url(url).build();
                new OkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        MyErrorLog.i("DOWNLOAD", "download failed info???" + e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Sink sink = null;
                        BufferedSink bufferedSink = null;
                        try {
                            String mSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                            // ????????? ????????? ?????? url.substring(url.lastIndexOf("/") + 1)
                            File dest = new File(mSDCardPath, "/1/app-debug.apk");
                            sink = Okio.sink(dest);
                            bufferedSink = Okio.buffer(sink);
                            bufferedSink.writeAll(response.body().source());
                            bufferedSink.close();
                            MyErrorLog.d("DOWNLOAD", "Total download event =" + (System.currentTimeMillis() - startTime) / 1000 + "??????");
//                            AviHide();
                            _ApplicationsForStart();
                        } catch (Exception e) {
                            e.printStackTrace();
                            MyErrorLog.i("DOWNLOAD", "download failed");
                        } finally {
                            if (bufferedSink != null) {
                                bufferedSink.close();
                            }
                        }
                    }
                });
            }
        }).start();
    }

    public boolean isUIProcess() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        MyErrorLog.e("Process", "?????? " + mainProcessName + "------PID" + myPid);
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    TimerTask DeviceTimerTask = new TimerTask() {
        @Override
        public void run() {
            try {
                if (MainBean.Instance.id != null && MainBean.Instance.id != "") {
                    TimerTask_GetNewDeviceName("");
                }
            } catch (Exception ex) {
                MyErrorLog.e("DeviceTimerTask Error ", ex);
            }
        }
    };

    public String TimerTask_GetNewDeviceName(String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    NewDeviceName = null;
                    File dir = Environment.getExternalStorageDirectory();
                    File dirs = new File(dir.getPath() + "/LocalAppLogs/log/Device/Device.log");
                    if (dirs.exists()) {
                        FileInputStream fileInputStream = null;
                        try {
                            // 1.????????????
                            fileInputStream = new FileInputStream(dirs);
                            // 2.????????? ????????????byte 10001??? -----> ?????????????????????ASCLL ?????????
                            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                            String line = null;
                            StringBuilder builder = new StringBuilder();
                            while ((line = reader.readLine()) != null) {
                                if (line.contains(id)) {
                                    String Cut_IndexOF = line.substring(line.indexOf(":"), line.length());
                                    String Cutins = Cut_IndexOF.replace(":", "");
                                    DeviceName.Instance.setDeviceName(Cutins);
                                    NewDeviceName = Cutins;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            MyErrorLog.e("???????????????:Error", "info:" + e);
                        } finally {
                            if (fileInputStream != null) {
                                try {
                                    fileInputStream.close();
                                } catch (IOException e) {
                                    MyErrorLog.e("???????????????:Error", "info:" + e);
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        MyErrorLog.e("Device Local File Not Found:", "?????????????????????????????????");
                    }
                } catch (Exception exception) {
                    MyErrorLog.e("Red Device Logs Error", "info : " + exception);
                }
            }
        }).start();
        return NewDeviceName == null ? null : NewDeviceName;
    }


    public void NewTimerTask_GetNewDeviceName(String str, String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MyErrorLog.e("???????????????", str + "------" + id);
                    int sum = 0;
                    EditText editText = (EditText) findViewById(R.id.DeviceInfo);
                    List<DeviceSumStr> deviceSumStrs = new ArrayList<>();
                    File dir = Environment.getExternalStorageDirectory();
                    File dirs = new File(dir.getPath() + "/LocalAppLogs/log/Device/Device.log");
                    if (dirs.exists()) {
                        FileInputStream fileInputStream = null;
                        try {
                            // 1.????????????
                            fileInputStream = new FileInputStream(dirs);
                            // 2.????????? ????????????byte 10001??? -----> ?????????????????????ASCLL ?????????
                            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                            String line = null;
                            StringBuilder builder = new StringBuilder();
                            while ((line = reader.readLine()) != null) {
                                if (line.contains(id)) {
                                    sum = sum + 1;
                                    DeviceSumStr deviceSumStr = new DeviceSumStr(line);
                                    deviceSumStrs.remove(deviceSumStr);
                                } else {
                                    DeviceSumStr deviceSumStr = new DeviceSumStr(line);
                                    deviceSumStrs.add(deviceSumStr);
                                }

                            }

                            if (sum == 1) {
                                if (str != null && str != "") {
                                    LocalDeviceName.DeleteFileNow();
                                    DeviceSumStr deviceSumStr = new DeviceSumStr(id + ":" + str);
                                    deviceSumStrs.add(deviceSumStr);
                                    builder.append(line);
                                }
                            } else if (sum < 1) {
                                LocalDeviceName.DeleteFileNow();
                                DeviceSumStr deviceSumStr = new DeviceSumStr(id + ":" + str);
                                deviceSumStrs.add(deviceSumStr);
//                                String Cut_IndexOF = line.substring(line.indexOf(":"), line.length());
//                                String Cutins = Cut_IndexOF.replace(":", "");
//                                DeviceName.Instance.setDeviceName(Cutins);
                            }
                            if (deviceSumStrs.size() >= 1 && sum >= 1) {
                                if (DeviceName.Instance.DeviceName_ != null || !dirs.exists()) {
                                    for (int i = 0; i < deviceSumStrs.size(); i++) {
                                        LocalDeviceName.e("", deviceSumStrs.get(i).DeviceStr);
                                    }
                                }
                            }
                            if (deviceSumStrs.size() >= 1) {
                                LocalDeviceName.DeleteFileNow();
                                if (DeviceName.Instance.DeviceName_ != null && DeviceName.Instance.DeviceName_ != "" || !dirs.exists()) {
                                    for (int i = 0; i < deviceSumStrs.size(); i++) {
                                        LocalDeviceName.e("", deviceSumStrs.get(i).DeviceStr);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            MyErrorLog.e("????????????????????????:", "info:" + e);
                        } finally {
                            if (fileInputStream != null) {
                                try {
                                    fileInputStream.close();
                                } catch (IOException e) {
                                    MyErrorLog.e("????????????????????????:", "info:" + e);
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (Exception exception) {
                    MyErrorLog.e("Red Device Logs Error", "info : " + exception);
                }
            }
        }).start();
    }

    private void initTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 1, 1);//????????????
        Calendar endDate = Calendar.getInstance();
        endDate.set(2099, 12, 31);//????????????
        pvTime = new TimePickerBuilder(MainActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                MyErrorLog.e("", getTimes(date));
                File dir = Environment.getExternalStorageDirectory();
                File dirs = new File(dir.getPath() + "/LocalAppLogs/log/" + historical_data_info + "ClassExeLogs.log");
                if (dirs.exists()) {
//                    avi.show();
                    TextView dalog = (TextView) findViewById(R.id.dalog);
                    dalog.setVisibility(View.VISIBLE);
                    MyErrorLog.d("??????", "??????");
                    ReadText(historical_data_info, getTimes(date));
                    Intent Href = new Intent();
                    Href.setClass(MainActivity.this, new_historical_data.class);
                    startActivity(Href);
                } else {
                    MyErrorLog.d("?????????", "?????????");
                    AlertDialog alertDialog1 = new AlertDialog.Builder(context)
                            .setTitle("????????????")//??????
                            .setMessage("????????????????????? -> " + dirs.getName())//??????
                            .setIcon(R.drawable.notfile)//??????
                            .create();
                    alertDialog1.show();
                }
            }
        })
                .setType(new boolean[]{true, true, true, true, true, false})
                .setLabel(" ???", "???", "???", "???", "???", "")
                .isCenterLabel(true)
                .setDividerColor(Color.DKGRAY)
                .setContentTextSize(16)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDecorView(null)
                .build();
    }

    //???????????????
    private String getTimes(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-M-d");
        historical_data_info = format1.format(date);
        return format.format(date);
    }

    private void ReadText(String file_Name, String date) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File dir = Environment.getExternalStorageDirectory();
                File dirs = new File(dir.getPath() + "/LocalAppLogs/log/" + file_Name + "ClassExeLogs.log");
                if (dirs.exists()) {
                    FileInputStream fileInputStream = null;
                    try {
                        // 1.????????????
                        fileInputStream = new FileInputStream(dirs);
                        // 2.????????? ????????????byte 10001??? -----> ?????????????????????ASCLL ?????????
                        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                        String line = null;
                        StringBuilder builder = new StringBuilder();
//                        ExecutorService executorService = Executors.newFixedThreadPool(5);
                        while ((line = reader.readLine()) != null) {
                            String finalLine = line;
//                            Runnable syncRunnable = new Runnable() {
//                                @Override
//                                public void run() {
//                                    MyErrorLog.e("??????:", Thread.currentThread().getName());
                            builder.append(finalLine);
                            if (finalLine.contains(date)) {
                                try {
                                    String DataType = null;
                                    String DataUnit = null;
                                    String DataValues = null;
                                    String CutIndexOF_1 = finalLine.replace(" ", "");
                                    String compare = CutIndexOF_1.substring(CutIndexOF_1.indexOf("?????????"), CutIndexOF_1.length());
                                    String CutDevice = compare.replace("?????????:", "?????????");
                                    // ?????????
                                    String CutIndexOF_2 = CutDevice.substring(CutDevice.indexOf("?????????") + 3, CutDevice.indexOf(":"));

                                    // ??????
                                    String CutIndexOF_3 = compare.replace("?????????:" + CutIndexOF_2 + ":??????:", "??????");
                                    String DataValue = CutIndexOF_3.substring(CutIndexOF_3.indexOf("??????") + 2, CutIndexOF_3.indexOf(":"));
                                    Pattern pattern = Pattern.compile("\t|\r|\n|\\s*");
                                    Matcher matcher = pattern.matcher(DataValue);
                                    String dest = matcher.replaceAll("");
                                    String CutAuio = dest.replace("AUTO", "");
                                    String Now_ = CutAuio.replace("+", "");
                                    String Now__ = Now_.replace(" ", "");
                                    String GetNOWVALUE = Now__.substring(0, Now_.length() - 1);
                                    GetNOWVALUE.replace("-", "");
                                    if (GetNOWVALUE.contains("??")) {
                                        DataType = "??????";
                                    } else if (GetNOWVALUE.contains("Diode")) {
                                        DataType = "?????????";
                                    } else if (GetNOWVALUE.contains("DC")) {
                                        GetNOWVALUE.replace("DC", "");
                                        DataType = "DC";
                                        if (GetNOWVALUE.contains("ERR")) {
                                            GetNOWVALUE.replace("ERR", "");
                                            DataUnit = GetNOWVALUE;
                                        }
                                    } else if (GetNOWVALUE.contains("AC")) {
                                        GetNOWVALUE.replace("AC", "");
                                        DataType = "AC";
                                        if (GetNOWVALUE.contains("ERR")) {
                                            GetNOWVALUE.replace("ERR", "");
                                            DataUnit = "ERR";
                                        }
                                    } else if (GetNOWVALUE.contains("Hz")) {
                                        DataType = "??????";
                                    } else if (GetNOWVALUE.contains("ERR") || GetNOWVALUE.contains("-ERR") || GetNOWVALUE.contains("?") || GetNOWVALUE.contains(".") || GetNOWVALUE.contains(":")) {
                                        DataValues = "ERR";
                                        GetNOWVALUE = GetNOWVALUE.replace("ERR", "");
                                        GetNOWVALUE = GetNOWVALUE.replace("-ERR", "");
                                    }

                                    if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("V")) {
                                        DataUnit = "mV";
                                    } else if (GetNOWVALUE.contains("M") && GetNOWVALUE.contains("Q")) {
                                        DataType = "M??";
                                        DataUnit = "M??";
                                    } else if (GetNOWVALUE.contains("Hz")) {
                                        DataUnit = "Hz";
                                    } else if (GetNOWVALUE.contains("u") && GetNOWVALUE.contains("A")) {
                                        DataUnit = "uA";
                                    } else if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("A")) {
                                        DataUnit = "mA";
                                    } else if (GetNOWVALUE.contains("A") && GetNOWVALUE.contains("V")) {
                                        DataUnit = "V";
                                    } else if (GetNOWVALUE.contains("n") && GetNOWVALUE.contains("f")) {
                                        DataType = "??????";
                                        DataUnit = "nF";
                                    } else if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("f")) {
                                        DataType = "??????";
                                        DataUnit = "mF";
                                    } else if (GetNOWVALUE.contains("u") && GetNOWVALUE.contains("f")) {
                                        DataType = "??????";
                                        DataUnit = "uF";
                                    } else if (GetNOWVALUE.contains("f")) {
                                        DataType = "??????";
                                        DataUnit = "F";
                                    } else if (GetNOWVALUE.contains("k") && GetNOWVALUE.contains("??")) {
                                        DataType = "??????";
                                        DataUnit = "k??";
                                    } else if (GetNOWVALUE.contains("Diode") && GetNOWVALUE.contains("V")) {
                                        DataType = "?????????";
                                        DataUnit = "V";
                                    } else if (GetNOWVALUE.contains("M") && GetNOWVALUE.contains("??")) {
                                        DataType = "??????";
                                        DataUnit = "M??";
                                    } else if (GetNOWVALUE.contains("??")) {
                                        DataType = "??????";
                                        DataUnit = "??";
                                    } else if (GetNOWVALUE.contains("A")) {
                                        DataUnit = "A";
                                    }
                                    if (GetNOWVALUE.contains("ERRDC") || GetNOWVALUE.contains("ERRVC")) {
                                        DataValues = "ERR";
                                    }

                                    if (GetNOWVALUE.contains("-ERR")) {
                                        GetNOWVALUE.replace("-ERR", "");
                                        DataUnit = GetNOWVALUE;
                                    }
                                    if (GetNOWVALUE.length() >= 4) {
                                        String Cut_Str = GetNOWVALUE.replace("-", "");
                                        String CutString = Cut_Str.substring(0, 5);
                                        if (CutString.contains("ERRDC") || CutString.contains("ERRVC")) {
                                            DataValues = "ERR";
                                        } else if (CutString.contains("?")) {
                                            DataValues = "ERR";
                                        } else {
                                            if (CutString.contains("ERRDC") || CutString.contains("ERRAC")) {
                                                DataValues = "ERR";
                                            } else if (CutString.contains("?")) {
                                                DataValues = "ERR";
                                            } else {
                                                DataValues = CutString;
                                            }
                                        }
                                    }

                                    String CutIndexOF_4 = compare.replace("?????????:" + CutIndexOF_2 + ":??????:" + DataValue + "", "");
                                    String CutIndexOF_5 = CutIndexOF_4.replace(":??????:", "??????");
                                    // ??????
                                    String DataTemp = CutIndexOF_5.substring(CutIndexOF_5.indexOf("??????") + 2, CutIndexOF_5.indexOf(":"));

                                    String CutIndexOF_6 = compare.replace("?????????:" + CutIndexOF_2 + ":??????:" + DataValue + ":??????:" + DataTemp + "", "");
                                    String CutIndexOF_7 = CutIndexOF_6.replace(":??????:", "??????");
                                    // ??????
                                    String DataHumi = CutIndexOF_7.substring(CutIndexOF_7.indexOf("??????") + 2, CutIndexOF_7.indexOf(":"));

                                    String CutIndexOF_8 = compare.replace("?????????:" + CutIndexOF_2 + ":??????:" + DataValue + ":??????:" + DataTemp + ":??????:" + DataHumi + "", "");
                                    String CutIndexOF_9 = CutIndexOF_8.replace(":????????????:", "????????????");
                                    // ????????????
                                    String DataTime = CutIndexOF_9.substring(CutIndexOF_9.indexOf("????????????") + 4, CutIndexOF_9.length());

                                    if (DataType == null) {
                                        DataType = "ERR";
                                    }
                                    if (DataValues == null) {
                                        DataValues = "ERR";
                                    }
                                    if (DataUnit == null) {
                                        DataUnit = "ERR";
                                    }
                                    if (DataUnit.contains("-ERRDC-m-V") || DataUnit.contains("-ERRAC-m-V")) {
                                        DataUnit = "mV";
                                    }
                                    Historical_Data_Value historicalDataValue = new Historical_Data_Value(CutIndexOF_2, DataValues, DataType, DataUnit, DataTemp, DataHumi, DataTime);
                                    historical_data_values.add(historicalDataValue);
                                } catch (Exception exception) {
                                    MyErrorLog.e("????????????????????????", "????????????:" + exception);
                                }
                            }
                        }
//                            };
//                            executorService.execute(syncRunnable);
//                        }
                    } catch (Exception e) {
                        OpenAnimata();
                        MyErrorLog.e("Now Date", "File of found" + e);
                        e.printStackTrace();
                    } finally {
                        if (fileInputStream != null) {
                            try {
                                OpenAnimata();
                                fileInputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            OpenAnimata();
                        }
                    }
                }
            }
        }).start();
    }

    // ????????????????????????
    public String UpdateDeviceName() {
        EditText UpdateName = (EditText) findViewById(R.id.input_deviceName);
        String Pm = UpdateName.getText() == null ? null : UpdateName.getText().toString();
        return Pm == null ? null : Pm;
    }

    // ??????????????? ???
    public void DeelteDeviceNameValue() {
        EditText UpdateName = (EditText) findViewById(R.id.input_deviceName);
        UpdateName.setText(null);
    }

    // ?????????????????????????????????
    public Boolean UpdateDeviceNamePublic(String id) {
        Boolean jUSToK = false;
        try {
            EditText device_TextNames = (EditText) findViewById(R.id.DeviceInfo);
            dialog_edit.normalDialog(this, device_TextNames, id);
            jUSToK = true;
        } catch (Exception exception) {
            MyErrorLog.e("????????????????????????????????????", exception);
        }
        return jUSToK;
    }

    public Boolean ToskInfo(Boolean Success) {
        if (Success) {
            AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("??????????????????")//??????
                    .setMessage("????????????")
                    .setIcon(R.drawable.success)//??????
                    .create();
            alertDialog1.show();
//            Toast.makeText(MainActivity.this, "????????????", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(MainActivity.this, "????????????!", Toast.LENGTH_SHORT).show();
            AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("??????????????????")//??????
                    .setMessage("????????????")
                    .setIcon(R.drawable.notfound)//??????
                    .create();
            alertDialog1.show();
        }
        return Success;
    }


    public void SumView(List<Multidevice> data) {
        date_compute.normalDialog(MainActivity.this, data);
    }

    private void InitNew() {
        MainBean mainBean = new MainBean();
        AppVersion appVersion = new AppVersion();
        NetUtil netWork = new NetUtil();
        DeviceName deviceName = new DeviceName();
        Device_Info device_info = new Device_Info();
    }

    public void Success_OK(String str) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    OpenAnimata();
                    AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("??????Excel??????")//??????
                            .setMessage("????????????::" + str)
                            .setIcon(R.drawable.success)//??????
                            .create();
                    alertDialog1.show();
                    Looper.loop();
                }
            }).start();
        } catch (Exception exception) {
            MyErrorLog.e("AlertDialog Error", "" + exception);
        }


    }

    TimerTask Adapter_item = new TimerTask() {
        @Override
        public void run() {
            ReloadView();
        }
    };

    private void ReloadView() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myRecy = (RecyclerView) findViewById(R.id.updateDeviceName);
                myRecy.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                myRecy.setAdapter(new device_Three_Item(MainActivity.this, multidevices));
            }
        });
//        if (multidevices.size() > 0) {
//            Handler handler = new Handler();
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    // TODO Auto-generated method stub
//                    //???????????????
//                    myRecy = (RecyclerView) findViewById(R.id.updateDeviceName);
//                    myRecy.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//                    myRecy.setAdapter(new device_Three_Item(MainActivity.this, multidevices));
//                    handler.postDelayed(this, 1000);
//                }
//            };
//            handler.postDelayed(runnable, 1000);
//        }
//    }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KEYCODE_BACK) {
            Toast.makeText(MainActivity.this, "?????????Home??????", Toast.LENGTH_SHORT).show();
            return true;
        }
        return true;
    }
}