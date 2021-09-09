package com.example.wirelesscapacitor;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;
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
    ImageView mainDate;
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


    private TimePickerView mPickerOptions;//时间选择器
    private long startingTime;//开始时间
    private long endTime;//结束时间
    private MainAdapter adapter;
    private List<MainBean> mData = new ArrayList<>();

    private List<ValueUtil> valueUtilList = new ArrayList<>();
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
    public static int CurrentIndex = 1;

    private String NowTemp = null;
    private String NowHumi = null;
    private RecyclerView recyclerView;
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-M-d");
    private String LogsOutName = "ClassExeLogs.log";


    private String Remote_update_address = null;
    private String RemoteAppVersion = null;
    private String Remote_description = null;
    private String Remote_AppName = null;
    private Context context;
    private AVLoadingIndicatorView avi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏标题栏以及状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE); /**标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题**/
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
        adapter = new MainAdapter(mData);
        historicalPicturesRv.setLayoutManager(new LinearLayoutManager(this));
        historicalPicturesRv.setAdapter(adapter);
        try {
            OkHttpVersionRequst();
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, "检查版本更新时错误", Toast.LENGTH_SHORT).show();
        }

        // Start Timer Just Get Tamp Hum Data
        Timer NowTimer = new Timer();
        NowTimer.schedule(ViewTask, 1000, 1000);
        Timer Scroll =new Timer();
        Scroll.schedule(ScroolView,200,200);
        MainBean mainBean = new MainBean();
        AppVersion appVersion=new AppVersion();
        NetUtil netWork=new NetUtil();
        TextView temBut = (TextView) findViewById(R.id.TextTemBut);
        temBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainBean.Instance.getTemperature() != null) {
                    AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("当前温度")//标题
                            .setMessage(MainBean.Instance.getTemperature() + "单位C°")//内容
                            .setIcon(R.drawable.tem)//图标
                            .create();
                    alertDialog1.show();
                }
            }
        });
        ImageView imgdate = (ImageView) findViewById(R.id.main_date);
        imgdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        handler.postDelayed(runnable, TIME); // 开启定时器更新UI
    }
    private void OpenAnimata() {
        setContentView(R.layout.activity_main);
        String indicator = getIntent().getStringExtra("game");
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.setIndicator(indicator);
        avi.hide();
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
                // 返回选中日期时间
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


    // Now TimerTask Get Data
    TimerTask ViewTask = new TimerTask() {
        @Override
        public void run() {
            SxView();
        }
    };
    TimerTask ScroolView =new TimerTask() {
        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RecyclerView recyclerView=(RecyclerView) findViewById(R.id.historical_pictures_rv);
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
            });

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        startActivityForResult(intent, 10086);

    }
    private  void AviHide(){
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
                    // 申请权限。
                    startInstallPermissionSettingActivity();
                    MyErrorLog.d("privilege", "Applying for System Permissions");
                }
            }
        } catch (Exception exception) {
            MyErrorLog.d("privilege Error", "System Errore: " + exception);
        }
    }

    // pathName 文件路径
    // authority  供应商权限
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

    // 动态权限
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

    private void OpenExcel() {
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
                    if (month == 0) {
                        month = 1;
                    }
                    List<ValueUtil> valueUtilList = new ArrayList<>();
                    String data = String.format("%d-%d-%d", year, month + 1, dayOfMonth);//格式化日期
                    String OutFileName = data + "ClassExeLogs.log";
                    OutFileName.replace(" ", "");
                    MyErrorLog.d("File Name:", OutFileName);
                    File dir = Environment.getExternalStorageDirectory();
                    File dirs = new File(dir.getPath() + "/LocalAppLogs/log/" + OutFileName + "");
                    MyErrorLog.d("File Path", dirs.getPath());
                    if (dirs.exists()) {
                        MyErrorLog.e("存在", "文件存在");
                        File file = new File(Environment.getExternalStorageDirectory(),
                                "LocalAppLogs/log/Export/" + data + ".xls");
                        if (!file.exists()) {
                            FileInputStream fileInputStream = null;
                            try {
                                // 1.打开文件
                                fileInputStream = new FileInputStream(dirs);
                                // 2.读操作 字节流（byte 10001） -----> 字符流（编码）ASCLL 流操作
                                BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                                String line = null;
                                StringBuilder builder = new StringBuilder();
                                while ((line = reader.readLine()) != null) {
                                    builder.append(line);
                                    try {
                                        String DataType = null;
                                        String DataUnit = null;
                                        String DataValues = null;
                                        String CutIndexOF_1 = line.replace(" ", "");
                                        String compare = CutIndexOF_1.substring(CutIndexOF_1.indexOf("设备号"), CutIndexOF_1.length());
                                        String CutDevice = compare.replace("设备号:", "设备号");
                                        // 设备号
                                        String CutIndexOF_2 = CutDevice.substring(CutDevice.indexOf("设备号") + 3, CutDevice.indexOf(":"));

                                        // 数据
                                        String CutIndexOF_3 = compare.replace("设备号:" + CutIndexOF_2 + ":数据:", "数据");
                                        String DataValue = CutIndexOF_3.substring(CutIndexOF_3.indexOf("数据") + 2, CutIndexOF_3.indexOf(":"));
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
                                                DataUnit = "ERR";
                                            }
                                        } else if (GetNOWVALUE.contains("Hz")) {
                                            DataType = "频率";
                                        } else if (GetNOWVALUE.contains("ERR") || GetNOWVALUE.contains("-ERR") || GetNOWVALUE.contains("?") || GetNOWVALUE.contains(".") || GetNOWVALUE.contains(":")) {
                                            DataValues = "ERR";
                                            GetNOWVALUE.replace("ERR", "");
                                            GetNOWVALUE.replace("-ERR", "");
                                            GetNOWVALUE.replace("?", "");
                                            GetNOWVALUE.replace(".", "");
                                            GetNOWVALUE.replace(":", "");
                                        }

                                        if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("V")) {
                                            DataUnit = "mV";
                                        } else if (GetNOWVALUE.contains("M") && GetNOWVALUE.contains("Q")) {
                                            DataType = "MΩ";
                                            DataUnit = "MΩ";
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
                                            DataValues = "ERR";
                                        }

                                        if (GetNOWVALUE.contains("-ERR")) {
                                            GetNOWVALUE.replace("-ERR", "");
                                            DataUnit = GetNOWVALUE;
                                        }
                                        if (GetNOWVALUE.length() > 5) {
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
                                        String CutIndexOF_4 = compare.replace("设备号:" + CutIndexOF_2 + ":数据:" + DataValue + "", "");
                                        String CutIndexOF_5 = CutIndexOF_4.replace(":温度:", "温度");
                                        // 温度
                                        String DataTemp = CutIndexOF_5.substring(CutIndexOF_5.indexOf("温度") + 2, CutIndexOF_5.indexOf(":"));

                                        String CutIndexOF_6 = compare.replace("设备号:" + CutIndexOF_2 + ":数据:" + DataValue + ":温度:" + DataTemp + "", "");
                                        String CutIndexOF_7 = CutIndexOF_6.replace(":湿度:", "湿度");
                                        // 湿度
                                        String DataHumi = CutIndexOF_7.substring(CutIndexOF_7.indexOf("湿度") + 2, CutIndexOF_7.indexOf(":"));

                                        String CutIndexOF_8 = compare.replace("设备号:" + CutIndexOF_2 + ":数据:" + DataValue + ":温度:" + DataTemp + ":湿度:" + DataHumi + "", "");
                                        String CutIndexOF_9 = CutIndexOF_8.replace(":记录时间:", "记录时间");
                                        // 记录时间
                                        String DataTime = CutIndexOF_9.substring(CutIndexOF_9.indexOf("记录时间") + 4, CutIndexOF_9.length());

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
                                        MyErrorLog.e("写入列表错误", "错误信息:" + exception);
                                    }
                                }
                                String[] title = {"设备号", "数据类型", "数据值", "单位", "温度", "湿度", "时间"};
                                String fileName = "" + data + ".xls";
                                File dir1 = Environment.getExternalStorageDirectory();
                                String path = dir1.getPath() + "/";
                                File dirs1 = new File(path + "LocalAppLogs/log/Export/" + fileName + "");
                                ExcelUtil.initExcel(fileName, "数据记录", title);
                                ExcelUtil.writeObjListToExcel(valueUtilList, dirs1.getPath(), MainActivity.this);
                            } catch (Exception e) {
                                MyErrorLog.e("Now Date", "File of found" + e);
                                e.printStackTrace();
                                AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("" + data + " 相关数据信息获取错误")//标题
                                        .setMessage(e.toString())//内容
                                        .setIcon(R.drawable.tem)//图标
                                        .create();
                                alertDialog1.show();
                            } finally {
                                if (fileInputStream != null) {
                                    try {
                                        fileInputStream.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "该文件已存在", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "该文件不存在", Toast.LENGTH_SHORT).show();
                        MyErrorLog.e("File of found", "this path of found" + dirs.getPath());
                    }

                }
            }, year, month - 1, day).show();
        } catch (Exception exception) {
            Log.d(TAG, "Time Error: " + exception);
        }
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
                        String humidity_Show = humidity + " RH";
                        humidity_Show.replace(" ", "");
                        String CutString = "设备号:" + MainBean.Instance.getId() + ":数据:" + MainBean.Instance.getCapacitance() + ":温度:" + Temp + ":湿度:" + humidity_Show + ":记录时间:" + MainBean.Instance.getTiem();
                        Pattern pattern1 = Pattern.compile("\t|\r|\n|\\s*");
                        Matcher matcher1 = pattern1.matcher(CutString);
                        String dest1 = matcher1.replaceAll("");
                        MyLog.e("", dest1.replace(" ", ""));
//                        NowTamp.setText("current temperature " + Temp + " C°");
//                        NowHum.setText("current humidity " + humidity_Show);
                        HumiValue.setText(Temp + "℃");
                        TempValue.setText(humidity_Show + "℃");
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
                        String Now_ = CutAuio.replace("+", "");
                        String Now__ = Now_.replace(" ", "");
                        String GetNOWVALUE = Now__.substring(0, Now_.length() - 1);
                        if (GetNOWVALUE.contains("ERR")) {
                            capacitance.setText("ERR");
                        } else if (GetNOWVALUE.contains("?")) {
                            capacitance.setText("ERR");
                        }else if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("V")){
                            GetNOWVALUE.replace("m-V","mV");
                            String NOwValue = GetNOWVALUE;
                            capacitance.setText(NOwValue);
                        } else {
                            capacitance.setText(GetNOWVALUE);
                        }
                        //if (GetNOWVALUE.contains("AC")) {
                        //                            NowImgSum.setImageResource(R.mipmap.ic_current);
                        //                            NowElectricity.setText("交流电流");
                        //                        } else if (GetNOWVALUE.contains("DC")) {
                        //                            NowImgSum.setImageResource(R.mipmap.ic_voltage);
                        //                            NowElectricity.setText("直流电流");
                        //                        } else
                        if (GetNOWVALUE.contains("Hz")) {
                            NowImgSum.setImageResource(R.drawable.hz);
                            NowElectricity.setText("频率");
                        } else if (GetNOWVALUE.contains("DC") && GetNOWVALUE.contains("V")) {
                            NowImgSum.setImageResource(R.mipmap.ic_current);
                            NowElectricity.setText("直流电压");
                        } else if (GetNOWVALUE.contains("DC") && GetNOWVALUE.contains("A")) {
                            NowImgSum.setImageResource(R.mipmap.ic_voltage);
                            NowElectricity.setText("直流电流");
                        } else if (GetNOWVALUE.contains("AC") && GetNOWVALUE.contains("V")) {
                            NowImgSum.setImageResource(R.mipmap.ic_current);
                            NowElectricity.setText("交流电压");
                        } else if (GetNOWVALUE.contains("AC") && GetNOWVALUE.contains("V")) {
                            NowImgSum.setImageResource(R.mipmap.ic_voltage);
                            NowElectricity.setText("交流电流");
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
                    RequestBody body = RequestBody.create(mediaType, "name=在线监测安卓&department=南电科技&plat=android");
                    Request request = new Request.Builder()
                            .url("http://www.cqset.com:8081/api/v2/version/check_version?name=在线监测安卓&department=南电科技&plat=android")
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
                        MyErrorLog.e("本地版本:",""+LocalVersion);
                        if (Version > LocalVersion ) {
                            MyErrorLog.e("本地版本小于服务器版本:",""+LocalVersion);
                            UpdateTip();
                        }
                        MyErrorLog.e("API Response ", Getdata + "----" + VersionNow + "----" + DonwloadPath);
                    } catch (IOException e) {
                        MyErrorLog.e("API Resqus Error", "info:  " + e);
                        e.printStackTrace();
                    }
                    }else{
                        MyErrorLog.e("未检测到当前网络","无网络使用");
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

    //通过PackageInfo得到的想要启动的应用的包名
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pInfo = null;
        try {
            //通过PackageManager可以得到PackageInfo
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
        builder.setTitle("监测到程序可更新版本内容:" + Remote_description + "  版本号" + RemoteAppVersion + "，是否更新");
        File dir = Environment.getExternalStorageDirectory();
        File dirs = new File(dir.getPath() + "/1/app-debug.apk");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                avi.show();
                if (dirs.exists()) {
                    dirs.delete();
                }
                OkHttpDonwloadTask(Remote_update_address);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户取消更新
                Toast.makeText(MainActivity.this, "已取消取消更新", Toast.LENGTH_SHORT).show();
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
            String compare = CutIndexOF_1.substring(CutIndexOF_1.indexOf("设备号"), CutIndexOF_1.length());
            String CutDevice = compare.replace("设备号:", "设备号");
            // 设备号
            String CutIndexOF_2 = CutDevice.substring(CutDevice.indexOf("设备号") + 3, CutDevice.indexOf(":"));

            // 数据
            String CutIndexOF_3 = compare.replace("设备号:" + CutIndexOF_2 + ":数据:", "数据");
            String DataValue = CutIndexOF_3.substring(CutIndexOF_3.indexOf("数据") + 2, CutIndexOF_3.indexOf(":"));
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
                    DataUnit = "Ω";
                }
            } else if (GetNOWVALUE.contains("Hz")) {
                DataType = "频率";
            } else if (GetNOWVALUE.contains("ERR") || GetNOWVALUE.contains("-ERR") || GetNOWVALUE.contains("?") || GetNOWVALUE.contains(".") || GetNOWVALUE.contains(":")) {
                DataValues = "Ω";
                GetNOWVALUE.replace("ERR", "");
                GetNOWVALUE.replace("-ERR", "");
                GetNOWVALUE.replace("?", "");
                GetNOWVALUE.replace(".", "");
                GetNOWVALUE.replace(":", "");
            }

            if (GetNOWVALUE.contains("m") && GetNOWVALUE.contains("V")) {
                DataUnit = "mV";
            } else if (GetNOWVALUE.contains("M") && GetNOWVALUE.contains("Q")) {
                DataType = "MΩ";
                DataUnit = "MΩ";
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
                DataValues = "Ω";
            }

            if (GetNOWVALUE.contains("-ERR")) {
                GetNOWVALUE.replace("-ERR", "");
                DataUnit = GetNOWVALUE;
            }
            if (GetNOWVALUE.length() > 5) {
                String Cut_Str = GetNOWVALUE.replace("-", "");
                String CutString = Cut_Str.substring(0, 5);
                if (CutString.contains("ERRDC") || CutString.contains("ERRVC")) {
                    DataValues = "Ω";
                } else if (CutString.contains("?")) {
                    DataValues = "Ω";
                } else {
                    if (CutString.contains("ERRDC") || CutString.contains("ERRAC")) {
                        DataValues = "Ω";
                    } else if (CutString.contains("?")) {
                        DataValues = "Ω";
                    } else {
                        DataValues = CutString;
                    }
                }
            }
            String CutIndexOF_4 = compare.replace("设备号:" + CutIndexOF_2 + ":数据:" + DataValue + "", "");
            String CutIndexOF_5 = CutIndexOF_4.replace(":温度:", "温度");
            // 温度
            String DataTemp = CutIndexOF_5.substring(CutIndexOF_5.indexOf("温度") + 2, CutIndexOF_5.indexOf(":"));

            String CutIndexOF_6 = compare.replace("设备号:" + CutIndexOF_2 + ":数据:" + DataValue + ":温度:" + DataTemp + "", "");
            String CutIndexOF_7 = CutIndexOF_6.replace(":湿度:", "湿度");
            // 湿度
            String DataHumi = CutIndexOF_7.substring(CutIndexOF_7.indexOf("湿度") + 2, CutIndexOF_7.indexOf(":"));

            String CutIndexOF_8 = compare.replace("设备号:" + CutIndexOF_2 + ":数据:" + DataValue + ":温度:" + DataTemp + ":湿度:" + DataHumi + "", "");
            String CutIndexOF_9 = CutIndexOF_8.replace(":记录时间:", "记录时间");
            // 记录时间
            String DataTime = CutIndexOF_9.substring(CutIndexOF_9.indexOf("记录时间") + 4, CutIndexOF_9.length());
            if (DataType == null) {
                DataType = "Ω";
            }
            if (DataValues == null) {
                DataValues = "Ω";
            }
            if (DataUnit == null) {
                DataUnit = "Ω";
            }
            ValueUtil valueUtil = new ValueUtil(CutIndexOF_2, DataType, DataValues, DataUnit, DataTemp, DataHumi, DataTime);
            valueUtilList.add(valueUtil);
        } catch (Exception exception) {
            MyErrorLog.e("写入列表错误", "错误信息:" + exception);
        }
    }

    private void OkHttpDonwloadTask(String Url) {
        // 异步循环执行 下载任务
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyErrorLog.e("下载地址", Url);
                //"http://download.talkdoo.com/iVRealFTPTest/OVMClass_Level2_U4_Android/game/Win64/bin/iVRealEdc-armv7.apk"
                final String url = Url;
                final long startTime = System.currentTimeMillis();
                MyLog.i("DOWNLOAD", "startTime=" + startTime);
                Request request = new Request.Builder().url(url).build();
                new OkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        MyErrorLog.i("DOWNLOAD", "download failed info：" + e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Sink sink = null;
                        BufferedSink bufferedSink = null;
                        try {
                            String mSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                            // 服务器 安装包 名称 url.substring(url.lastIndexOf("/") + 1)
                            File dest = new File(mSDCardPath, "/1/app-debug.apk");
                            sink = Okio.sink(dest);
                            bufferedSink = Okio.buffer(sink);
                            bufferedSink.writeAll(response.body().source());
                            bufferedSink.close();
                            MyErrorLog.d("DOWNLOAD", "Total download event =" + (System.currentTimeMillis() - startTime) / 1000 + "：秒");
                            AviHide();
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
    private void Screed(){

    }
}