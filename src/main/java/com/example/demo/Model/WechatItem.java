package com.example.demo.Model;

import com.example.demo.Utils.Config;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by snsoft on 17/7/2017.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class WechatItem {
    @Id
    @GeneratedValue
    private Integer id;
    private String wechatName;
    private Integer amount;
    private String url;
    private Date lastUsetime;
    private Date OverTime;
    private Date creatTime;
    private String state;
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public WechatItem() {
        this.lastUsetime = new Date();
        this.creatTime = new Date();
        this.OverTime = new Date();
        this.state = Config.Normal;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWechatName() {
        return wechatName;
    }

    public void setWechatName(String wechatName) {
        this.wechatName = wechatName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getLastUsetime() {
        return lastUsetime;
    }

    public void setLastUsetime(Date lastUsetime) {
        this.lastUsetime = lastUsetime;
    }

    public Date getOverTime() {
        return OverTime;
    }

    public void setOverTime(Date overTime) {
        OverTime = overTime;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    @Override
    public String toString() {
        return "WechatItem{" +
                "id=" + id +
                ", wechatName='" + wechatName + '\'' +
                ", amount=" + amount +
                ", url='" + url + '\'' +
                ", lastUsetime=" + lastUsetime +
                ", creatTime=" + creatTime +
                ", note='" + note + '\'' +
                '}';
    }
}
