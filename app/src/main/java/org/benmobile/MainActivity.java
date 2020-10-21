package org.benmobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;



import org.benmobile.activity.BaseActivity;
import org.benmobile.analysis.SyknetMobileLog;
import org.benmobile.config.ConstData;
import org.benmobile.coolhttp.http.CoolHttp;
import org.benmobile.coolhttp.http.HttpListener;
import org.benmobile.coolhttp.http.Method;
import org.benmobile.coolhttp.http.Response;
import org.benmobile.coolhttp.http.StringRequest;
import org.benmobile.core.natvie.app.PluginClient;
import org.benmobile.core.natvie.utils.ValueUtils;
import org.benmobile.log.Logger;
import org.benmobile.protocol.ComponentRegister;
import org.benmobile.protocol.Protocol;
import org.benmobile.protocol.bean.AddressBean;
import org.benmobile.protocol.bean.ProtocolMsgBean;
import org.benmobile.protocol.config.PluginConstData;
import org.benmobile.protocol.event.GetMsgInfoListener;
import org.benmobile.protocol.event.OnAppNetQuestListener;
import org.benmobile.protocol.serviceimpl.AppNetQuestService;
import org.benmobile.utils.BatchPingIpThread;
import org.benmobile.utils.Ping;
import org.benmobile.utils.PingTest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class MainActivity extends BaseActivity implements View.OnClickListener {
        private String pluginid = "cn.benh5.ardx.common.cpst";
//    private String pluginid = "com.syknet.addressplugin";
    private RelativeLayout rl_appdemo, rl_ui_wgt, rl_windows,rl_oder_sdk,rl_extent,rl_net,rlui_native,rl_sys;
    private ImageView iv_meum,iv_msg;
    private String tag = PluginConstData.TAG_HOMEPROJECT;
    public final int requestcode = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_meum= (ImageView) this.findViewById(R.id.iv_meum);
        iv_msg= (ImageView) this.findViewById(R.id.iv_msg);
        rl_appdemo = (RelativeLayout) this.findViewById(R.id.rl_appdemo);
        rl_ui_wgt = (RelativeLayout) this.findViewById(R.id.rl_ui_wgt);
        rl_oder_sdk = (RelativeLayout) this.findViewById(R.id.rl_oder_sdk);
        rl_sys = (RelativeLayout) this.findViewById(R.id.rl_sys);
        rlui_native = (RelativeLayout) this.findViewById(R.id.rlui_native);
        rl_net = (RelativeLayout) this.findViewById(R.id.rl_net);
        rl_extent = (RelativeLayout) this.findViewById(R.id.rl_extent);
        rl_windows = (RelativeLayout) this.findViewById(R.id.rl_windows);
        setViewEvent();
    }

    private void setViewEvent() {
        iv_meum.setOnClickListener(this);
        iv_msg.setOnClickListener(this);
        rl_net.setOnClickListener(this);
        rl_oder_sdk.setOnClickListener(this);
        rl_appdemo.setOnClickListener(this);
        rl_windows.setOnClickListener(this);
        rl_ui_wgt.setOnClickListener(this);
        rl_sys.setOnClickListener(this);
        rlui_native.setOnClickListener(this);
        rl_extent.setOnClickListener(this);
    }

    private void request() {
        final Ping ping=new Ping();
         final String ipAddress = "42.51.33.155";
//        PingTest.test0();
//        PingTest.test1();

       new Thread(new Runnable() {
            @Override
            public void run() {
                {
                    Queue<String> allIp = new LinkedList<String>();


                    allIp.offer("123.184.41.48");
                    allIp.offer("59.46.81.105");

                    allIp.offer("42.51.33.155");
                    BatchPingIpThread batchPingIpThread = new BatchPingIpThread(allIp, 3);
                    batchPingIpThread.setIpsOK("");
                    batchPingIpThread.setIpsNO("");
                    batchPingIpThread.startPing();
                    System.out.println("ipsOK:" + batchPingIpThread.getIpsOK());
//                    System.out.print("ipsNO:" + batchPingIpThread.getIpsNO());
//                    PingTest.test1();
//                    try {
//
//                        ping. ping02(ipAddress);
//                        System.out.println("网络是否可用:"+ping.ping(ipAddress));
////                   System.out.println(ping.ping(ipAddress, 5, 5000));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }).start();



        // 测试接口见：http://api.nohttp.net

        String url = "http://qimg.ewharain.cn/";
//        String url = "http://www.ule.com/ulewap/recommond/ylxd/index.html";
        StringRequest request = new StringRequest(url, Method.POST);
        request.addParams("name", "yanzhenjie");
        request.addParams("pwd", 123);
        Logger.e("---开始:"+System.currentTimeMillis()+"");
        CoolHttp.asyncRequest(request, new HttpListener<String>() {
            @Override
            public void onSucceed(Response<String> response) {
                Logger.e("---结束："+System.currentTimeMillis()+"");
                String result = response.getResult();
                Log.e("CoolHttp", result);

                Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailed(Response<String> response) {
                Log.e("CoolHttp", "", response.getException());
                Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void testNet() {
        SyknetMobileLog.DEBUG=true;
        final int mRequestCode = 1100;
        String eventAction = "0x10001";
        Map<String, String> data = new HashMap<String, String>();
        data.put("appid", mApp.appid);
        data.put("appkey", mApp.appkey);
//        data.put("flageType", "2");
//        data.put("pluginId", "3");
//        data.put("appProject", "com.ule.demo.zhuxunkang");
//        mpush.setMsgType(ConstServiceType.INFRASTRUCTURE_SERVICE);
//        mpush.setServiceType(ConstServiceType.Infrastructure.BENH5_APP_NET_FRAME);
//        mpush.setEventAction(eventAction);
//        mpush.setParam(data);
//        mpush.setPluginId(tag);
//        new MagController().pushPluginAction(mpush, mRequestCode, new GetMsgInfoListener() {
//            @Override
//            public void MsgInfo(String pluignId, int requestCode, int resultCode, Protocol msgBean) {
//                if (mRequestCode==requestCode&&ValueUtils.isNotEmpty(msgBean)) {
//                    ProtocolMsgBean msgBean1= (ProtocolMsgBean) msgBean.call("");
//                    Toast.makeText(MainActivity.this, msgBean1.toString(), Toast.LENGTH_LONG).show();
//                }
//            }
//        });

        AppNetQuestService service = new AppNetQuestService(mApp.configs.SERVER_BENH5_VPS,ConstData.IndexFunction,eventAction, data, tag, mRequestCode, new OnAppNetQuestListener() {
            @Override
            public void onMsgSucceed(String pluignId, int requestCode, int resultCode, Protocol msgBean) {
                {
                    if (mRequestCode == requestCode && ValueUtils.isNotEmpty(msgBean)) {
                        ProtocolMsgBean msgBean1 = (ProtocolMsgBean) msgBean.call("");
                        Toast.makeText(MainActivity.this, msgBean1.getData(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onMsgFailed(String pluignId, int requestCode, int resultCode, Protocol msgBean) {

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        Protocol protocol = ComponentRegister.getInstance().getComponent(ConstData.DELIVERYADDRESS_REQUEST + "");
        if (ValueUtils.isNotEmpty(protocol)) {
            AddressBean mAddress = (AddressBean) protocol.call("");
            if (ValueUtils.isNotEmpty(mAddress)) {
                String add = mAddress.getProvinceName() + mAddress.getCityName() + mAddress.getAreaName();
                Toast.makeText(MainActivity.this, add, Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        Protocol protocol = ComponentRegister.getInstance().getComponent(ConstData.DELIVERYADDRESS_REQUEST + "");
        if (ValueUtils.isNotEmpty(protocol)) {
            AddressBean mAddress = (AddressBean) protocol.call("");
            if (ValueUtils.isNotEmpty(mAddress)) {
                String add = mAddress.getProvinceName() + mAddress.getCityName() + mAddress.getAreaName();
                Toast.makeText(MainActivity.this, add, Toast.LENGTH_LONG).show();

            }
        }

//            if (requestCode == ConstData.DELIVERYADDRESS_REQUEST) {
//           String add=      data.getStringExtra(ConstData.PNAME)+ data.getStringExtra(ConstData.CNAME)+
//                 data.getStringExtra(ConstData.ANAME)+
//                data.getStringExtra(ConstData.PCODE)+
//                data.getStringExtra(ConstData.CCODE)+
//                data.getStringExtra(ConstData.ACODE);
//                Toast.makeText(MainActivity.this,add,Toast.LENGTH_LONG).show();
//
//            }
//        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_meum:
                SyknetMobileLog.onClick(getBaseContext(),"","","菜单","");
                Toast.makeText(MainActivity.this, "菜单", Toast.LENGTH_LONG).show();
            break;
            case R.id.iv_msg:
                SyknetMobileLog.onClick(getBaseContext(),"","","消息","");
                Toast.makeText(MainActivity.this, "消息", Toast.LENGTH_LONG).show();
            break;
            case R.id.rl_oder_sdk:
                SyknetMobileLog.onClick(getBaseContext(),"","","第三方SDK","");
                Toast.makeText(MainActivity.this, "第三方SDK", Toast.LENGTH_LONG).show();
            break;
            case R.id.rl_extent:
                SyknetMobileLog.onClick(getBaseContext(),"","","扩展功能","");
                Toast.makeText(MainActivity.this, "扩展功能", Toast.LENGTH_LONG).show();
            break;
            case R.id.rlui_native:
                SyknetMobileLog.onClick(getBaseContext(),"","","原生UI","");
                Toast.makeText(MainActivity.this, "原生UI", Toast.LENGTH_LONG).show();
            break;
            case R.id.rl_net:
                SyknetMobileLog.onClick(getBaseContext(),"","","网络通讯","");
                Toast.makeText(MainActivity.this, "网络通讯", Toast.LENGTH_LONG).show();
            break;
            case R.id.rl_sys:
                SyknetMobileLog.onClick(getBaseContext(),"","","系统调用","");
                Toast.makeText(MainActivity.this, "系统调用", Toast.LENGTH_LONG).show();
            break;
            case R.id.rl_windows:
                testNet();
            break;
            case R.id.rl_appdemo:
               launchPlugin(pluginid,false);
            break;
            case R.id.rl_ui_wgt:
                request();
            break;
        }
    }
}
