package adbotg;

import static adbotg.Message.INSTALLING_PROGRESS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cgutman.adblib.AdbBase64;
import com.cgutman.adblib.AdbConnection;
import com.cgutman.adblib.AdbCrypto;
import com.cgutman.adblib.AdbStream;
import com.cgutman.adblib.UsbChannel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.huosoft.wisdomclass.linspirerdemo.R;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class adbMainActivity extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnKeyListener {
    private Handler handler;
    private UsbDevice mDevice;
    private TextView tvStatus,logs;
    private ImageView usb_icon;
    private AdbCrypto adbCrypto;
    private AdbConnection adbConnection;
    private UsbManager mManager;
    private RelativeLayout terminalView;
    private LinearLayout checkContainer;
    private EditText edCommand;
    private Button btnRun;
    private ScrollView scrollView;
    private String user = null;
    private AdbStream stream;
    private SpinnerDialog waitingDialog;
    public static final int DEVICE_NOT_FOUND = 0;
    public static final int CONNECTING = 1;
    public static final int DEVICE_FOUND = 2;
    public static final int FLASHING = 3;
    final String[] settings = new String[]{"激活Linspirerdemo(请选择渠道)"};
    final String[] lspdemo2 = new String[]{"搜狗输入法定制版(com.sohu.inputmethod.sogou.oem)","bronya(cn.lspdemo_bronya)","透明版(cn.lspdemo_transparent)","WPS版(cn.wps.moffice_eng)","mana(cn.lspdemo_mana)","无限宝(vizpower.imeeting)"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String  action=getIntent().getAction();
        if(action!=null&&action.equals("android.hardware.usb.action.USB_DEVICE_ATTACHED")){
            finish();
        }
        setContentView(R.layout.activity_adb);
        tvStatus = findViewById(R.id.tv_status);
        usb_icon = findViewById(R.id.usb_icon);
        logs = findViewById(R.id.logs);
        terminalView = findViewById(R.id.terminalView);
        checkContainer = findViewById(R.id.checkContainer);
        edCommand = findViewById(R.id.edCommand);
        btnRun = findViewById(R.id.btnRun);
        scrollView = findViewById(R.id.scrollView);
        mManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case DEVICE_FOUND:
                        closeWaiting();
                        tvStatus.setText(getString(R.string.adb_device_connected));
                        usb_icon.setColorFilter(Color.parseColor("#4CAF50"));
                        checkContainer.setVisibility(View.GONE);
                        terminalView.setVisibility(View.VISIBLE);
                        initCommand();
                        showKeyboard();
                        break;
                    case CONNECTING:
                        waitingDialog();
                        closeKeyboard();
                        tvStatus.setText(getString(R.string.waiting_device));
                        usb_icon.setColorFilter(Color.BLUE);
                        checkContainer.setVisibility(View.VISIBLE);
                        terminalView.setVisibility(View.GONE);
                        break;
                    case DEVICE_NOT_FOUND:
                        closeWaiting();
                        closeKeyboard();
                        tvStatus.setText(getString(R.string.adb_device_not_connected));
                        usb_icon.setColorFilter(Color.RED);
                        checkContainer.setVisibility(View.VISIBLE);
                        terminalView.setVisibility(View.GONE);
                        break;

                    case FLASHING:
                        Toast.makeText(adbMainActivity.this, "Flashing", Toast.LENGTH_SHORT).show();
                        break;

                    case INSTALLING_PROGRESS:
                        Toast.makeText(adbMainActivity.this, "Progress", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        };
        AdbBase64 base64 = new MyAdbBase64();
        try {
            adbCrypto = AdbCrypto.loadAdbKeyPair(base64, new File(getFilesDir(), "private_key"), new File(getFilesDir(), "public_key"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (adbCrypto == null) {
            try {
                adbCrypto = AdbCrypto.generateAdbKeyPair(base64);
                adbCrypto.saveAdbKeyPair(new File(getFilesDir(), "private_key"), new File(getFilesDir(), "public_key"));
            } catch (Exception e) {
            }
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction("htetznaing.usb.permission");
        registerReceiver(mUsbReceiver, filter);
        UsbDevice device = getIntent().getParcelableExtra(UsbManager.EXTRA_DEVICE);
        if (device!=null) {
            asyncRefreshAdbConnection(device);
        }else {
            try{
                for (String k : mManager.getDeviceList().keySet()) {
                    UsbDevice usbDevice = mManager.getDeviceList().get(k);
                    handler.sendEmptyMessage(CONNECTING);
                    if (mManager.hasPermission(usbDevice)) { ;
                        asyncRefreshAdbConnection(usbDevice);
                    } else {
                        mManager.requestPermission(usbDevice, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("htetznaing.usb.permission"), 0));
                    }
                }
            }catch (Exception ignore){

            }

        }
        edCommand.setImeActionLabel("Run", EditorInfo.IME_ACTION_DONE);
        edCommand.setOnEditorActionListener(this);
        edCommand.setOnKeyListener(this);
    }

    private void closeWaiting(){
        if (waitingDialog!=null)
            waitingDialog.dismiss();
    }

    private void waitingDialog(){
        closeWaiting();
        waitingDialog = SpinnerDialog.displayDialog(this, "注意", "如果您是第一次从该设备连接到目标设备，则可能需要接受该设备上的提示。\n如果短时间您看见此提示多次，建议更换一个otg头", false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.adbmain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.settings){
            MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(this);
            builder.setTitle("更多选项")
                    .setIcon(R.drawable.settings)
                    .setItems(settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i){
                                case 0:
                                    MaterialAlertDialogBuilder builder1=new MaterialAlertDialogBuilder(adbMainActivity.this);
                                    builder1.setItems(lspdemo2, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            String finalcmd="";
                                            switch (i){
                                                case 0:
                                                    finalcmd="dpm set-device-owner  set-device-owner com.sohu.inputmethod.sogou.oem/com.huosoft.wisdomclass.linspirerdemo.AR";
                                                    break;
                                                case 1:
                                                    finalcmd="dpm set-device-owner cn.lspdemo_bronya/com.huosoft.wisdomclass.linspirerdemo.AR";
                                                    break;
                                                case 2:
                                                    finalcmd="dpm set-device-owner cn.lspdemo_transparent/com.huosoft.wisdomclass.linspirerdemo.AR";
                                                    break;
                                                case 3:
                                                    finalcmd="dpm set-device-owner cn.wps.moffice_eng/com.huosoft.wisdomclass.linspirerdemo.AR";
                                                    break;
                                                case 4:
                                                    finalcmd="dpm set-device-owner cn.lspdemo_mana/com.huosoft.wisdomclass.linspirerdemo.AR";
                                                    break;
                                                case 5:
                                                    finalcmd="dpm set-device-owner vizpower.imeeting/com.huosoft.wisdomclass.linspirerdemo.AR";
                                                    break;
                                            }
                                            if(adbConnection!=null){
                                                try{
                                                    stream.write((finalcmd+"\n").getBytes("UTF-8"));
                                                }catch (UnsupportedEncodingException e){

                                                }catch (InterruptedException e){

                                                }catch (IOException e){

                                                }
                                            }
                                        }
                                    });
                                    builder1.create().show();
                                    break;
                            }
                        }
                    }).create().show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        asyncRefreshAdbConnection((UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE));
    }

    public void asyncRefreshAdbConnection(final UsbDevice device) {
        if (device != null) {
            new Thread() {
                @Override
                public void run() {
                    final UsbInterface intf = findAdbInterface(device);
                    try {
                        setAdbInterface(device, intf);
                    } catch (Exception e) {
                    }
                }
            }.start();
        }
    }

    BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                String deviceName = device.getDeviceName();
                if (mDevice != null && mDevice.getDeviceName().equals(deviceName)) {
                    try {
                        setAdbInterface(null, null);
                    } catch (Exception e) {
                    }
                }
            }else if ("htetznaing.usb.permission".equals(action)){
                UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                handler.sendEmptyMessage(CONNECTING);
                if (mManager.hasPermission(usbDevice))
                    asyncRefreshAdbConnection(usbDevice);
                else
                    mManager.requestPermission(usbDevice,PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("htetznaing.usb.permission"), 0));
            }
        }
    };
    private UsbInterface findAdbInterface(UsbDevice device) {
        int count = device.getInterfaceCount();
        for (int i = 0; i < count; i++) {
            UsbInterface intf = device.getInterface(i);
            if (intf.getInterfaceClass() == 255 && intf.getInterfaceSubclass() == 66 &&
                    intf.getInterfaceProtocol() == 1) {
                return intf;
            }
        }
        return null;
    }
    private synchronized boolean setAdbInterface(UsbDevice device, UsbInterface intf) throws IOException, InterruptedException {
        if (adbConnection != null) {
            adbConnection.close();
            adbConnection = null;
            mDevice = null;
        }
        if (device != null && intf != null) {
            UsbDeviceConnection connection = mManager.openDevice(device);
            if (connection != null) {
                if (connection.claimInterface(intf, false)) {
                    handler.sendEmptyMessage(CONNECTING);
                    adbConnection = AdbConnection.create(new UsbChannel(connection, intf), adbCrypto);
                    adbConnection.connect();
                    //TODO: DO NOT DELETE IT, I CAN'T EXPLAIN WHY
                    adbConnection.open("shell:exec date");
                    mDevice = device;
                    handler.sendEmptyMessage(DEVICE_FOUND);
                    return true;
                } else {
                    connection.close();
                }
            }
        }

        handler.sendEmptyMessage(DEVICE_NOT_FOUND);
        mDevice = null;
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mUsbReceiver);
        try {
            if (adbConnection != null) {
                adbConnection.close();
                adbConnection = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initCommand(){
        // Open the shell stream of ADB
        logs.setText("");
        try {
            stream = adbConnection.open("shell:");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }catch (NullPointerException e){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!stream.isClosed()) {
                    try {
                        // Print each thing we read from the shell stream
                        final String[] output = {new String(stream.read(), "US-ASCII")};
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (user == null) {
                                    user = output[0].substring(0,output[0].lastIndexOf("/")+1);
                                }else if (output[0].contains(user)){
                                }
                                logs.append(output[0]);
                                scrollView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                        edCommand.requestFocus();
                                    }
                                });
                            }
                        });
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }).start();

        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putCommand();
            }
        });
    }

    private void putCommand() {
        if (!edCommand.getText().toString().isEmpty()){
            try {
                String cmd = edCommand.getText().toString();
                if (cmd.equalsIgnoreCase("clear")) {
                    String log = logs.getText().toString();
                    String[] logSplit = log.split("\n");
                    logs.setText(logSplit[logSplit.length-1]);
                }else if (cmd.equalsIgnoreCase("exit")) {
                    onDestroy();
                    finish();
                }else {
                    stream.write((cmd+"\n").getBytes("UTF-8"));
                }
                edCommand.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else Toast.makeText(adbMainActivity.this, "No command", Toast.LENGTH_SHORT).show();
    }
    public void showKeyboard(){
        edCommand.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
    public void closeKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (adbConnection != null && actionId == EditorInfo.IME_ACTION_DONE) {
            putCommand();
        }
        return true;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            return onEditorAction((TextView)v, EditorInfo.IME_ACTION_DONE, event);
        } else {
            return false;
        }
    }
    private void test(){
        File local = new File(Environment.getExternalStorageDirectory()+"/adm.apk");
        String remotePath = "/sdcard/" + local.getName();
        try {
            new Push(adbConnection, local, remotePath).execute(handler);
            new Install(adbConnection, remotePath, local.length() / 1024).execute(handler);
        } catch (Exception e) {
        }
    }
}

class SpinnerDialog implements Runnable, DialogInterface.OnCancelListener {
    private String title, message;
    private Activity activity;
    private ProgressDialog progress;
    private boolean finish;
    public SpinnerDialog(Activity activity, String title, String message, boolean finish) {
        this.activity = activity;
        this.title = title;
        this.message = message;
        this.progress = null;
        this.finish = finish;
    }

    public static SpinnerDialog displayDialog(Activity activity, String title, String message, boolean finish) {
        SpinnerDialog spinner = new SpinnerDialog(activity, title, message, finish);
        activity.runOnUiThread(spinner);
        return spinner;
    }
    public void dismiss() {
        activity.runOnUiThread(this);
    }

    @Override
    public void run() {
        if (progress == null) {
            if (activity.isFinishing())
                return;
            progress = new ProgressDialog(activity);
            progress.setTitle(title);
            progress.setMessage(message);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setOnCancelListener(this);
            if (finish) {
                progress.setCancelable(true);
                progress.setCanceledOnTouchOutside(false);
            }
            else {
                progress.setCancelable(false);
            }
            progress.show();
        }
        else {
            progress.dismiss();
        }
    }
    @Override
    public void onCancel(DialogInterface dialog) {
        activity.finish();
    }
}
