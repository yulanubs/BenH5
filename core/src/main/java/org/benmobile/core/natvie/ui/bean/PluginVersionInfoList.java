package org.benmobile.core.natvie.ui.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jekshow on 2016/12/9.
 */

public class PluginVersionInfoList  extends  ResultViewModle implements Serializable {
    public ArrayList<PluginVersionInfo> mPluginVersionInfoList = new ArrayList<PluginVersionInfo>();

    public PluginVersionInfoList(JSONObject jsn) throws JSONException {
        super(jsn);

        if (jsn.has("data")) {
            JSONArray stationOrdersInfos = jsn.optJSONArray("data");
            for (int i = 0; i < stationOrdersInfos.length(); i++) {
                try {
                    mPluginVersionInfoList.add(new PluginVersionInfo(
                            stationOrdersInfos.optJSONObject(i)));
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "PluginVersionInfoList{" +
                "mPluginVersionInfoList=" + mPluginVersionInfoList +
                '}';
    }
}
