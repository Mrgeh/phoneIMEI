package com.example.phoneimei;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String TODO = null;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;

    Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext = getApplicationContext();
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);

        showInformation();
    }

    /**
     * 获取到信息并展示
     */
    public void showInformation() {
        //获取Mac地址
        String newMac = getNewMac();
        tv1.setText("MAC:    " + newMac);
        //获取IMEI地址
        String imei = getIMEI(mcontext);
        tv2.setText("IMEI:    " + imei);
        //获取MSISDN
        String msisdn = getMSISDN(mcontext);

            tv3.setText("手机号:    " + msisdn);

        //获取IMSI
        String imsi = getIMSI(mcontext);
        tv4.setText("IMSI:    " + imsi);
        //获取ICCID
        String iccid = getICCID(mcontext);
        tv5.setText("ICCID:    " + iccid);

    }

    /**
     * 通过网络接口取
     * 获取wifiMac地址
     *
     * @return
     */
    private static String getNewMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 获取手机IMEI号
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return TODO;
        }
        String imei = telephonyManager.getDeviceId();

        return imei;
    }

    /**
     * 获取手机号
     */
    public static String getMSISDN(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return TODO;
        }
        String msisdn = telephonyManager.getLine1Number();
        return msisdn;
    }

    /**
     * 获取手机MSISDN号
     */
    public static String getIMSI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return TODO;
        }
        String imsi = telephonyManager.getSubscriberId();
        return imsi;
    }

    /**
     * 获取手机ICCID号
     */
    public static String getICCID(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return TODO;
        }
        String iccid = telephonyManager.getSimSerialNumber();
        return iccid;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //8.0动态权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int checkPermission = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1); //后面的1为请求码

                Log.d(TAG, "onpause(),未授权,去授权");
                //展示信息
                showInformation();
                return;

            }
            //展示信息
            showInformation();
            Log.d(TAG, "onpause()已授权...");

        } else {
            //展示信息
            showInformation();
            Log.d(TAG, "onpause()版本<=6.0");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //8.0动态权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int checkPermission = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1); //后面的1为请求码

                Log.d(TAG, "ondestroy(),未授权,去授权");
                //展示信息
                showInformation();
                return;

            }
            //展示信息
            showInformation();
            Log.d(TAG, "ondestroy(),已授权...");

        } else {
            //展示信息
            showInformation();
            Log.d(TAG, "ondestroy(),版本<=6.0");
        }

    }
}

