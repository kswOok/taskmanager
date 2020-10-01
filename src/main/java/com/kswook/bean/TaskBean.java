package com.kswook.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskBean {


    @JsonProperty(value = "开不开浏览器")
    public Boolean brower;

    public Integer progress;


    @JsonProperty(value = "目标数量")
    public Integer quantity;

    public Integer clickCount;

    @JsonProperty(value = "链接地址")
    public String url;
    public ArrayList<String> urls;

    @JSONField(name = "ua")
    @JsonProperty(value = "浏览器标志")
    public String uaKey;
    public String aid;


    @JsonProperty(value = "定向城市")
    public String city;
    public String taskKey;
    public String channel;
    public String version;


    @JsonProperty(value = "移动端")
    public Boolean isMobile = null;

    @JsonProperty(value = "PC端")
    public Boolean isPC = null;
    public String referer;
    public String tianmaoDeal;
    public String tianmaoAndroidpid;
    public String tianmaoIOSpid;
    public Boolean unionDevice = null;

    @JsonProperty(value = "开始时间")
    public String start = "";

    public Integer doubleRate =null;

    public String doubleTag = null;

    public String doubleXpath = null;

    public Integer thirdRate =null;

    public String thirdTag = null;

    public String thirdXpath = null;


    @JsonProperty(value = "结束时间")
    public String end = "";

    public TaskBean() {
    }


    public boolean isEffect() {
        if (StringUtils.isEmpty(start)) {
            return false;
        }
        if (StringUtils.isEmpty(end)) {
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date currentTime = new Date();
        Date eddTime = null;
        Date expTime = null;
        try {
            eddTime = dateFormat.parse(start);
            expTime = dateFormat.parse(end);
            return currentTime.after(eddTime) && currentTime.before(expTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isNextDayEffect() {
        if (StringUtils.isEmpty(start)) {
            return false;
        }
        if (StringUtils.isEmpty(end)) {
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,1);
        Date currentTime = calendar.getTime();
        Date eddTime = null;
        Date expTime = null;
        try {
            eddTime = dateFormat.parse(start);
            expTime = dateFormat.parse(end);
            return currentTime.after(eddTime) && currentTime.before(expTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
