package org.benmobile.protocol.bean;

import java.io.Serializable;

/**
 * Created by Jekshow on 2016/12/15.
 */

public class AddressBean implements Serializable {
    private String provinceCode = "";
    private String  cityCode = "";
    private String areaCode = "";
    private String provinceName;
    private String  cityName;
    private String areaName;

    public String getProvinceCode() {
        return provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public AddressBean(String provinceCode, String cityCode, String areaCode, String provinceName, String cityName, String areaName) {
        this.provinceCode = provinceCode;
        this.cityCode = cityCode;
        this.areaCode = areaCode;
        this.provinceName = provinceName;
        this.cityName = cityName;
        this.areaName = areaName;
    }


}
