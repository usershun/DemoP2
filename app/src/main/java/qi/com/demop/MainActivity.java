package qi.com.demop;

import androidx.appcompat.app.AppCompatActivity;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.mbms.MbmsErrors;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_ping;
    EditText et_ip, et_count, et_size, et_time;
    String ip, count, size, time;
    String ip2,hip2,hip1,qip2,qip1,ship3,ship2,ship1,sip3,sip2,sip1;
    private String query;
    private EditText edit_ip2;
    private Button btn_ping2;
    private String countCmd;
    private String sizeCmd;
    private String timeCmd;
    private String ip_adress;
    private String ping;
    private Intent intent;
    private Bundle bundle;
    private Button sbtn_ping1;
    private Button sbtn_ping2;
    private Button sbtn_ping3;
    private Button qbtn_ping1;
    private Button qbtn_ping2;
    private Button shbtn_ping1;
    private Button shbtn_ping2;
    private Button shbtn_ping3;
    private Button hbtn_ping1;
    private Button hbtn_ping2;
    private EditText sedit_ip1;
    private EditText sedit_ip2;
    private EditText sedit_ip3;
    private EditText shedit_ip1;
    private EditText shedit_ip2;
    private EditText shedit_ip3;
    private EditText qedit_ip1;
    private EditText qedit_ip2;
    private EditText hedit_ip1;
    private EditText hedit_ip2;
    private String city;
    private String regionName;
    private String org;
    private int Time=60;
    private Handler handler=new Handler(){
        public void handleMessage(android.os.Message msg) {
            if (msg.what==0) {
                if (Time>0) {
                    //时间--
                 Time--; //给时间赋值

                 handler.sendEmptyMessageDelayed(0, 1000);
                }else {

                    Intent intent=new Intent(MainActivity.this,PingResult1.class);
                    startActivity(intent); finish();
                }
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        btn_ping.setOnClickListener(this);
        btn_ping2.setOnClickListener(this);
        sbtn_ping1.setOnClickListener(this);
        sbtn_ping2.setOnClickListener(this);
        sbtn_ping3.setOnClickListener(this);
        shbtn_ping1.setOnClickListener(this);
        shbtn_ping2.setOnClickListener(this);
        shbtn_ping3.setOnClickListener(this);
        qbtn_ping1.setOnClickListener(this);
        qbtn_ping2.setOnClickListener(this);
        hbtn_ping1.setOnClickListener(this);
        hbtn_ping2.setOnClickListener(this);

        try {
            RetrofitUtils.getInstance().getMyServer().ping()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<PingBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(PingBean pingBean) {
                            query = pingBean.getQuery();
                            city = pingBean.getCity();
                            regionName = pingBean.getRegionName();
                            org = pingBean.getOrg();

                            // et_ip.setText(query);


                            Log.i("cc", query +"");
                        }
                    });
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }



    }

    private void init() {
        btn_ping = (Button) findViewById(R.id.btn_ping);
        et_ip = (EditText) findViewById(R.id.edit_ip);
        et_count = (EditText) findViewById(R.id.edit_count);
        et_size = (EditText) findViewById(R.id.edit_size);
        et_time = (EditText) findViewById(R.id.edit_time);
        edit_ip2 = findViewById(R.id.edit_ip2);
        btn_ping2 = findViewById(R.id.btn_ping2);
        sbtn_ping1 = findViewById(R.id.sbtn_ping1);
        sbtn_ping2 = findViewById(R.id.sbtn_ping2);
        sbtn_ping3 = findViewById(R.id.sbtn_ping3);
        qbtn_ping1 = findViewById(R.id.qbtn_ping1);
        qbtn_ping2 = findViewById(R.id.qbtn_ping2);
        shbtn_ping1 = findViewById(R.id.shbtn_ping1);
        shbtn_ping2 = findViewById(R.id.shbtn_ping2);
        shbtn_ping3 = findViewById(R.id.shbtn_ping3);
        hbtn_ping1 = findViewById(R.id.hbtn_ping1);
        hbtn_ping2 = findViewById(R.id.hbtn_ping2);
        sedit_ip1 = findViewById(R.id.sedit_ip1);
        sedit_ip2 = findViewById(R.id.sedit_ip2);
        sedit_ip3 = findViewById(R.id.sedit_ip3);
        shedit_ip1 = findViewById(R.id.shedit_ip1);
        shedit_ip2 = findViewById(R.id.shedit_ip2);
        shedit_ip3 = findViewById(R.id.shedit_ip3);
        qedit_ip1 = findViewById(R.id.qedit_ip1);
        qedit_ip2 = findViewById(R.id.qedit_ip2);
        hedit_ip1 = findViewById(R.id.hedit_ip1);
        hedit_ip2 = findViewById(R.id.hedit_ip2);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ping:
                ip = et_ip.getText().toString();
                count = et_count.getText().toString();
                size = et_size.getText().toString();
                time = et_time.getText().toString();

                String countCmd = " -c " + count + " ";
                String sizeCmd = " -s " + size + " ";
                String timeCmd = " -i " + time + " ";
                String ip_adress = ip;
                String ping = "ping" + countCmd + timeCmd + sizeCmd + ip_adress;

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, PingResult1.class);
                // new一个Bundle对象，并将要传递的数据传入
                Bundle bundle = new Bundle();
                bundle.putString("ping", ping);
                bundle.putString("ip", ip);
                bundle.putString("city",city);
                bundle.putString("regionName",regionName);
                bundle.putString("org",org);
                bundle.putString("count", count);
                bundle.putString("size", size);
                bundle.putString("time", time);
                bundle.putString("query",query);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btn_ping2:
                ip2 = edit_ip2.getText().toString();
                count = et_count.getText().toString();
                size = et_size.getText().toString();
                time = et_time.getText().toString();

                countCmd = " -c " + count + " ";
                sizeCmd = " -s " + size + " ";
                timeCmd = " -i " + time + " ";
                ip_adress = ip2;
                ping = "ping" + countCmd + timeCmd + sizeCmd + ip_adress;

                intent = new Intent();
                intent.setClass(MainActivity.this, PingResult1.class);
                // new一个Bundle对象，并将要传递的数据传入
                bundle = new Bundle();
                bundle.putString("ping", ping);
                bundle.putString("ip", ip2);
                bundle.putString("count", count);
                bundle.putString("size", size);
                bundle.putString("city",city);
                bundle.putString("regionName",regionName);
                bundle.putString("org",org);
                bundle.putString("query",query);
                bundle.putString("time", time);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.sbtn_ping1:
                sip1 = sedit_ip1.getText().toString();
                count = et_count.getText().toString();
                size = et_size.getText().toString();
                time = et_time.getText().toString();

                countCmd = " -c " + count + " ";
                sizeCmd = " -s " + size + " ";
                timeCmd = " -i " + time + " ";
                ip_adress = sip1;
                ping = "ping" + countCmd + timeCmd + sizeCmd + ip_adress;

                intent = new Intent();
                intent.setClass(MainActivity.this, PingResult1.class);
                // new一个Bundle对象，并将要传递的数据传入
                bundle = new Bundle();
                bundle.putString("ping", ping);
                bundle.putString("ip", sip1);
                bundle.putString("count", count);
                bundle.putString("size", size);
                bundle.putString("time", time);
                bundle.putString("query",query);
                bundle.putString("city",city);
                bundle.putString("regionName",regionName);
                bundle.putString("org",org);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.sbtn_ping2:
                sip2 = sedit_ip2.getText().toString();
                count = et_count.getText().toString();
                size = et_size.getText().toString();
                time = et_time.getText().toString();

                countCmd = " -c " + count + " ";
                sizeCmd = " -s " + size + " ";
                timeCmd = " -i " + time + " ";
                ip_adress = sip2;
                ping = "ping" + countCmd + timeCmd + sizeCmd + ip_adress;

                intent = new Intent();
                intent.setClass(MainActivity.this, PingResult1.class);
                // new一个Bundle对象，并将要传递的数据传入
                bundle = new Bundle();
                bundle.putString("ping", ping);
                bundle.putString("ip", sip2);
                bundle.putString("count", count);
                bundle.putString("size", size);
                bundle.putString("city",city);
                bundle.putString("regionName",regionName);
                bundle.putString("org",org);
                bundle.putString("time", time);
                bundle.putString("query",query);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.sbtn_ping3:
                sip3 = sedit_ip3.getText().toString();
                count = et_count.getText().toString();
                size = et_size.getText().toString();
                time = et_time.getText().toString();

                countCmd = " -c " + count + " ";
                sizeCmd = " -s " + size + " ";
                timeCmd = " -i " + time + " ";
                ip_adress = sip3;
                ping = "ping" + countCmd + timeCmd + sizeCmd + ip_adress;

                intent = new Intent();
                intent.setClass(MainActivity.this, PingResult1.class);
                // new一个Bundle对象，并将要传递的数据传入
                bundle = new Bundle();
                bundle.putString("ping", ping);
                bundle.putString("ip", sip3);
                bundle.putString("count", count);
                bundle.putString("size", size);
                bundle.putString("query",query);
                bundle.putString("city",city);
                bundle.putString("regionName",regionName);
                bundle.putString("org",org);
                bundle.putString("time", time);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.shbtn_ping1:
                ship1 = shedit_ip1.getText().toString();
                count = et_count.getText().toString();
                size = et_size.getText().toString();
                time = et_time.getText().toString();

                countCmd = " -c " + count + " ";
                sizeCmd = " -s " + size + " ";
                timeCmd = " -i " + time + " ";
                ip_adress = ship1;
                ping = "ping" + countCmd + timeCmd + sizeCmd + ip_adress;

                intent = new Intent();
                intent.setClass(MainActivity.this, PingResult1.class);
                // new一个Bundle对象，并将要传递的数据传入
                bundle = new Bundle();
                bundle.putString("ping", ping);
                bundle.putString("ip", ship1);
                bundle.putString("count", count);
                bundle.putString("size", size);
                bundle.putString("time", time);
                bundle.putString("query",query);
                bundle.putString("city",city);
                bundle.putString("regionName",regionName);
                bundle.putString("org",org);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.shbtn_ping2:
                ship2 = shedit_ip2.getText().toString();
                count = et_count.getText().toString();
                size = et_size.getText().toString();
                time = et_time.getText().toString();

                countCmd = " -c " + count + " ";
                sizeCmd = " -s " + size + " ";
                timeCmd = " -i " + time + " ";
                ip_adress = ship2;
                ping = "ping" + countCmd + timeCmd + sizeCmd + ip_adress;

                intent = new Intent();
                intent.setClass(MainActivity.this, PingResult1.class);
                // new一个Bundle对象，并将要传递的数据传入
                bundle = new Bundle();
                bundle.putString("ping", ping);
                bundle.putString("ip", ship2);
                bundle.putString("count", count);
                bundle.putString("size", size);
                bundle.putString("time", time);
                bundle.putString("query",query);
                bundle.putString("city",city);
                bundle.putString("regionName",regionName);
                bundle.putString("org",org);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.shbtn_ping3:
                ship3 = shedit_ip3.getText().toString();
                count = et_count.getText().toString();
                size = et_size.getText().toString();
                time = et_time.getText().toString();

                countCmd = " -c " + count + " ";
                sizeCmd = " -s " + size + " ";
                timeCmd = " -i " + time + " ";
                ip_adress = ship3;
                ping = "ping" + countCmd + timeCmd + sizeCmd + ip_adress;

                intent = new Intent();
                intent.setClass(MainActivity.this, PingResult1.class);
                // new一个Bundle对象，并将要传递的数据传入
                bundle = new Bundle();
                bundle.putString("ping", ping);
                bundle.putString("ip", ship3);
                bundle.putString("count", count);
                bundle.putString("size", size);
                bundle.putString("city",city);
                bundle.putString("regionName",regionName);
                bundle.putString("org",org);
                bundle.putString("time", time);
                bundle.putString("query",query);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.qbtn_ping1:
                qip1 = qedit_ip1.getText().toString();
                count = et_count.getText().toString();
                size = et_size.getText().toString();
                time = et_time.getText().toString();

                countCmd = " -c " + count + " ";
                sizeCmd = " -s " + size + " ";
                timeCmd = " -i " + time + " ";
                ip_adress = qip1;
                ping = "ping" + countCmd + timeCmd + sizeCmd + ip_adress;

                intent = new Intent();
                intent.setClass(MainActivity.this, PingResult1.class);
                // new一个Bundle对象，并将要传递的数据传入
                bundle = new Bundle();
                bundle.putString("ping", ping);
                bundle.putString("ip", qip1);
                bundle.putString("count", count);
                bundle.putString("size", size);
                bundle.putString("time", time);
                bundle.putString("query",query);
                bundle.putString("city",city);
                bundle.putString("regionName",regionName);
                bundle.putString("org",org);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.qbtn_ping2:
                qip2 = qedit_ip2.getText().toString();
                count = et_count.getText().toString();
                size = et_size.getText().toString();
                time = et_time.getText().toString();

                countCmd = " -c " + count + " ";
                sizeCmd = " -s " + size + " ";
                timeCmd = " -i " + time + " ";
                ip_adress = qip2;
                ping = "ping" + countCmd + timeCmd + sizeCmd + ip_adress;

                intent = new Intent();
                intent.setClass(MainActivity.this, PingResult1.class);
                // new一个Bundle对象，并将要传递的数据传入
                bundle = new Bundle();
                bundle.putString("ping", ping);
                bundle.putString("ip", qip2);
                bundle.putString("count", count);
                bundle.putString("size", size);
                bundle.putString("time", time);
                bundle.putString("query",query);
                bundle.putString("city",city);
                bundle.putString("regionName",regionName);
                bundle.putString("org",org);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.hbtn_ping1:
                hip1 = hedit_ip1.getText().toString();
                count = et_count.getText().toString();
                size = et_size.getText().toString();
                time = et_time.getText().toString();

                countCmd = " -c " + count + " ";
                sizeCmd = " -s " + size + " ";
                timeCmd = " -i " + time + " ";
                ip_adress = hip1;
                ping = "ping" + countCmd + timeCmd + sizeCmd + ip_adress;

                intent = new Intent();
                intent.setClass(MainActivity.this, PingResult1.class);
                // new一个Bundle对象，并将要传递的数据传入
                bundle = new Bundle();
                bundle.putString("ping", ping);
                bundle.putString("ip", hip1);
                bundle.putString("count", count);
                bundle.putString("size", size);
                bundle.putString("time", time);
                bundle.putString("query",query);
                bundle.putString("city",city);
                bundle.putString("regionName",regionName);
                bundle.putString("org",org);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.hbtn_ping2:
                hip2 = hedit_ip2.getText().toString();
                count = et_count.getText().toString();
                size = et_size.getText().toString();
                time = et_time.getText().toString();

                countCmd = " -c " + count + " ";
                sizeCmd = " -s " + size + " ";
                timeCmd = " -i " + time + " ";
                ip_adress = hip2;
                ping = "ping" + countCmd + timeCmd + sizeCmd + ip_adress;

                intent = new Intent();
                intent.setClass(MainActivity.this, PingResult1.class);
                // new一个Bundle对象，并将要传递的数据传入
                bundle = new Bundle();
                bundle.putString("ping", ping);
                bundle.putString("ip", hip2);
                bundle.putString("count", count);
                bundle.putString("size", size);
                bundle.putString("time", time);
                bundle.putString("query",query);
                bundle.putString("city",city);
                bundle.putString("regionName",regionName);
                bundle.putString("org",org);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

        }
    }
}
