package qi.com.demop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.schedulers.SchedulerWhen;
import rx.schedulers.Schedulers;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PingResult1 extends AppCompatActivity{
    private int CHOOSE = 0;
    TextView tv_show;
    String lost = "";// 丢包
    String delay = "";// 延迟
    String ip_adress = "";// ip地址
    String countCmd = "";// ping -c
    String sizeCmd = "", timeCmd = "";// ping -s ;ping -i
    String result = "";
    private static final String tag = "TAG";// Log标志
    int status = -1;// 状态
    String ping, ip, count, size, time;
    long delaytime = 0;
    // Myhandler handler=null;
    Handler handler1 = null;
    Thread a = null;
    private Button btn_send;
    private String t;
    private String query;
    private String min;
    private String max;
    private String city;
    private String regionName;
    private String org;
    private String mins;
    private String maxs;
    private String minss;
    private String maxss;
    private SimpleDateFormat formatter;
    private Date curDate;
    private String str;
    private int Time=10;
    private Handler handler=new Handler(){
        public void handleMessage(android.os.Message msg) {
            if (msg.what==0) {
                if (Time>0) {
                    //时间--
                    Time--; //给时间赋值


                    handler.sendEmptyMessageDelayed(0, 1000);
                }else {
                    finish();
                }
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping_result1);
        tv_show = (TextView) findViewById(R.id.tv_show);


        Intent intent2 = this.getIntent();
        Bundle bundle2 = intent2.getExtras();
        ping = bundle2.getString("ping");
        ip = bundle2.getString("ip");
        count = bundle2.getString("count");
        time = bundle2.getString("time");
        size = bundle2.getString("size");
        t = bundle2.getString("t");
        city = bundle2.getString("city");
        regionName = bundle2.getString("regionName");
        org = bundle2.getString("org");
        query = bundle2.getString("query");

        delaytime = (long) Double.parseDouble(time);
        //Log.i("ssss",t+"");
        // Log.i("ssss",query+"");
        // Log.i(tag, "====MainThread====:" + Thread.currentThread().getId());
        tv_show.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        PingResult1.this);
                builder.setTitle("请选择操作");
                String[] items = {"复制", "保存到SD卡"};
                builder.setSingleChoiceItems(items, 0,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                CHOOSE = which;
                            }
                        });
                builder.setNegativeButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                // Toast.makeText(PingResult1.this,
                                // "which="+which+"\nChoose="+CHOOSE,
                                // Toast.LENGTH_SHORT).show();
                                switch (CHOOSE) {
                                    case 0:
                                        ClipboardManager cm = (ClipboardManager) PingResult1.this
                                                .getSystemService(Context.CLIPBOARD_SERVICE);
                                        cm.setText(tv_show.getText());
                                        Toast.makeText(PingResult1.this, "复制成功！",
                                                Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1:
                                        Date date = new Date();
                                        SimpleDateFormat sfd = new SimpleDateFormat(
                                                "yyyy-MM-dd HH.mm.ss");
                                        String time = sfd.format(date) + ".txt";
                                        String text = tv_show.getText().toString();
                                        FileUtils fileUtils = new FileUtils();
                                        fileUtils.writeToSDFromStr(fileUtils.SDPATH
                                                + "/PING/", time, text);
                                        Toast.makeText(PingResult1.this,
                                                "保存成功！" + fileUtils.SDPATH,
                                                Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        break;
                                }
                                CHOOSE = 0;
                            }
                        });
                builder.setPositiveButton("取消", null);
                builder.show();

            }
        });

        handler1 = new Handler() {// 创建一个handler对象 ，用于监听子线程发送的消息
            public void handleMessage(Message msg)// 接收消息的方法
            {
                // String str = (String) msg.obj;// 类型转化
                // tv_show.setText(str);// 执行
                switch (msg.what) {
                    case 10:
                        String resultmsg = (String) msg.obj;
                        tv_show.append(resultmsg);
                        // Log.i(tag, "====handlerThread====:"+ Thread.currentThread().getId());
                        //Log.i("uu", "====resultmsg====:" + msg.what);
                        Log.i("oo", resultmsg + "");
                        break;
                    default:
                        break;
                }
            }
        };

        a = new Thread()// 创建子线程
        {
            public void run() {
                // for (int i = 0; i < 100; i++) {
                // try {
                // sleep(500);
                // } catch (InterruptedException e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
                // Message msg = new Message();// 创建消息类
                // msg.obj = "线程进度 ：" + i;// 消息类对象中存入消息
                // handler1.sendMessage(msg);// 通过handler对象发送消息
                // }
                delay = "";
                lost = "";

                Process process = null;
                BufferedReader successReader = null;
                BufferedReader errorReader = null;

                DataOutputStream dos = null;
                try {
                    // 闃诲澶勭悊
                    process = Runtime.getRuntime().exec(ping);
                    // dos = new DataOutputStream(process.getOutputStream());
                    //Log.i(tag, "====receive====:");

                    String command = "ping" + countCmd + timeCmd + sizeCmd
                            + ip_adress;


                    // dos.write(command.getBytes());
                    // dos.writeBytes("\n");
                    // dos.flush();
                    // dos.writeBytes("exit\n");
                    // dos.flush();

                    // status = process.waitFor();
                    InputStream in = process.getInputStream();

                    OutputStream out = process.getOutputStream();
                    // success

                    successReader = new BufferedReader(
                            new InputStreamReader(in));

                    // error
                    errorReader = new BufferedReader(new InputStreamReader(
                            process.getErrorStream()));

                    String lineStr;

                    while ((lineStr = successReader.readLine()) != null) {

                        //  Log.i("qq", lineStr+"");

                        Message msg = handler1.obtainMessage();
                        msg.obj = lineStr + "\r\n";
                        msg.what = 10;
                        msg.sendToTarget();
                        result = result + lineStr + "\n";

                        if (lineStr.contains("packet loss")) {
                            //Log.i(tag, "=====Message=====" + lineStr.toString());
                            // Log.i("qq",lineStr+"");
                            int i = lineStr.indexOf("received");
                            int j = lineStr.indexOf("%");



                            //Log.i("aa","====丢包率====:" + lineStr.substring(i + 10, j + 1));
                            lost = lineStr.substring(i + 10, j + 1);


                            /*String ddddd=lost;

                            losts=ddddd.substring(str.indexOf("%"));*/


                        }
                        if (lineStr.contains("avg")) {
                            int i = lineStr.indexOf("/", 20);
                            int j = lineStr.indexOf(".", i);

                            String substring = lineStr.substring(i + 1, j);

                            delay = lineStr.substring(i + 1, j + 4);
                            min = lineStr.substring(i - 10, j - 3);
                            mins = String.valueOf(min);
                            String dddd=mins;

                            minss=dddd.substring(4);



                            max = lineStr.substring(i + 8, j + 11);
                            maxs = String.valueOf(max);

                            //Log.i("bbbbb",cc+"");
                            Log.i("cccc", delay + "");


                            delay = delay + "ms";
                            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            curDate = new Date(System.currentTimeMillis());
                            str = formatter.format(curDate);
                            new Thread()
                            {
                                @Override
                                public void run() {

                                    // TODO Auto-generated method stub
                                    Looper.prepare();
                                    final String urlPath="http://58.220.51.10:8080/GameData/SaveSql";
                                    URL url;
                                    try {
                                        url = new URL(urlPath);
                                        /*封装子对象*/
                                        JSONObject ClientKey = new JSONObject();
                                        ClientKey.put("regionName", regionName);
                                        ClientKey.put("city", city);
                                        ClientKey.put("query", query);
                                        ClientKey.put("org", org);
                                        ClientKey.put("dst", ip);
                                        ClientKey.put("min",minss);
                                        ClientKey.put("max", maxs);
                                        ClientKey.put("loss", "0");
                                        ClientKey.put("date", str);
                                        ClientKey.put("terminalType","MA");
                                        /*封装Person数组*/
                    /*JSONObject params = new JSONObject();
                    params.put("Person", ClientKey);*/
                                        /*把JSON数据转换成String类型使用输出流向服务器写*/
                                        String content = String.valueOf(ClientKey);
                                        Log.i("mmmm",ClientKey+"");
                                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                        conn.setConnectTimeout(5000);
                                        conn.setDoOutput(true);//设置允许输出
                                        conn.setRequestMethod("POST");
                                        conn.setRequestProperty("User-Agent", "Fiddler");
                                        conn.setRequestProperty("Content-Type", "application/json");

                                        OutputStream os = conn.getOutputStream();
                                        os.write(content.getBytes());
                                        os.close();
                                        /*服务器返回的响应码*/
                                        int code = conn.getResponseCode();
                                        if(code == 200)
                                        {
                                            Toast.makeText(PingResult1.this,"数据成功发送",Toast.LENGTH_LONG).show();
                                            handler.sendEmptyMessage(0);

                                        }

                                        else
                                        {
                                            Toast.makeText(getApplicationContext(), "数据提交失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        // TODO: handle exception
                                        throw new RuntimeException(e);
                                    }
                                    Looper.loop();

                                }

                            }.start();
                        }
                        // tv_show.setText("丢包率:" + lost.toString() + "\n" +
                        // "平均时延:"
                        // + delay.toString() + "\n" + "IP地址:");// +
                        // getNetIpAddress()
                        // + getLocalIPAdress() + "\n" + "MAC地址:" +
                        // getLocalMacAddress() + getGateWay());
                        sleep(delaytime * 1000);
                    }
                    // tv_show.setText(result);

                    while ((lineStr = errorReader.readLine()) != null) {
                        //Log.i(tag, "==error======" + lineStr);
                        //Log.i(tag, "==error======" + lineStr);
                        // tv_show.setText(lineStr);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (dos != null) {
                            dos.close();
                        }
                        if (successReader != null) {
                            successReader.close();
                        }
                        if (errorReader != null) {
                            errorReader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (process != null) {
                        process.destroy();
                    }
                }
            }
        };
        a.start();

    }





}



