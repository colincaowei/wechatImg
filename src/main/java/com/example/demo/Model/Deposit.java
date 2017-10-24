package com.example.demo.Model;

import com.example.demo.Utils.Config;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.type.DoubleType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by snsoft on 26/9/2017.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Deposit {
    @Id
    @GeneratedValue
    private int id;
    private String depositNumber;
    private Date tranTime;
    private Date creatTime;
    private Date excuteTime;
    private String note;
    private Double amount;
    private Double tranfee;
    private String wechatName;
    private String nickName;
    private String state;
    private String billNo;

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepositNumber() {
        return depositNumber;
    }

    public void setDepositNumber(String depositNumer) {
        this.depositNumber = depositNumer;
    }

    public Date getTranTime() {
        return tranTime;
    }

    public void setTranTime(Date tranTime) {
        this.tranTime = tranTime;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public Date getExcuteTime() {
        return excuteTime;
    }

    public void setExcuteTime(Date excuteTime) {
        this.excuteTime = excuteTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amuount) {
        this.amount = amuount;
    }

    public Double getTranfee() {
        return tranfee;
    }

    public void setTranfee(Double tranfee) {
        this.tranfee = tranfee;
    }

    public String getWechatName() {
        return wechatName;
    }

    public void setWechatName(String wechatName) {
        this.wechatName = wechatName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Deposit() {
        this.creatTime = new Date();
        this.tranTime = new Date();
        this.excuteTime = new Date();
        this.state = Config.Pending;
        this.billNo = (new Date()).getTime() + "";
    }
}
