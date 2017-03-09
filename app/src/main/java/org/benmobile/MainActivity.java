package org.benmobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.benmobile.activity.BaseActivity;
import org.benmobile.config.ConstData;
import org.benmobile.coolhttp.http.CoolHttp;
import org.benmobile.coolhttp.http.HttpListener;
import org.benmobile.coolhttp.http.Method;
import org.benmobile.coolhttp.http.Response;
import org.benmobile.coolhttp.http.StringRequest;
import org.benmobile.core.natvie.app.PluginClient;
import org.benmobile.core.natvie.utils.ValueUtils;
import org.benmobile.protocol.ComponentRegister;
import org.benmobile.protocol.Protocol;
import org.benmobile.protocol.bean.AddressBean;
import org.benmobile.protocol.bean.ProtocolMsgBean;
import org.benmobile.protocol.config.PluginConstData;
import org.benmobile.protocol.event.GetMsgInfoListener;
import org.benmobile.protocol.event.OnAppNetQuestListener;
import org.benmobile.protocol.serviceimpl.AppNetQuestService;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private String pluginid = "com.syknet.addressplugin";
    private RelativeLayout rl_appdemo, rl_ui_wgt, rl_windows;

    private String tag = PluginConstData.TAG_HOMEPROJECT;
    public final int requestcode = 100;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rl_appdemo = (RelativeLayout) this.findViewById(R.id.rl_appdemo);
        rl_ui_wgt = (RelativeLayout) this.findViewById(R.id.rl_ui_wgt);
        rl_windows = (RelativeLayout) this.findViewById(R.id.rl_windows);
        rl_appdemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PluginClient.getInstance().launch(pluginid, MainActivity.this, false);
            }
        });
        rl_ui_wgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                request();
            }
        });
        rl_windows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testNet();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void request() {
        // 测试接口见：http://api.nohttp.net

        String url = "http://api.nohttp.net/upload";
        StringRequest request = new StringRequest(url, Method.POST);
        request.addParams("name", "yanzhenjie");
        request.addParams("pwd", 123);

        CoolHttp.asyncRequest(request, new HttpListener<String>() {
            @Override
            public void onSucceed(Response<String> response) {
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
        final int mRequestCode = 1100;
        String eventAction = "0x10001";
        Map<String, String> data = new HashMap<String, String>();
        data.put("name", "15773273445");
        data.put("password", "123456");
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

        AppNetQuestService service = new AppNetQuestService(mApp.configs.SERVER_BENH5_VPS,ConstData.LOGIN,eventAction, data, tag, mRequestCode, new OnAppNetQuestListener() {
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
