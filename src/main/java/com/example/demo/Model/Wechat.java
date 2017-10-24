package com.example.demo.Model;

import com.example.demo.Utils.Config;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by snsoft on 17/7/2017.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wechat {
    @Id
    @GeneratedValue
    private Integer id;
    @NotEmpty(message = "微信账号为为空")
    @Column(name = "wechat_name", unique = true)
    private String wechatName;
    private String nickName;
    private String wechatId;
    private String ip;
    private String teleNumber;
    private String state;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String remark;
    private Integer amount;
    //    @Value("now")
    private Date creatimte;
    private String creatUser;
    private Date lastUsetime;

    public Date getLastUsetime() {
        return lastUsetime;
    }

    public void setLastUsetime(Date lastUsetime) {
        this.lastUsetime = lastUsetime;
    }

    public Wechat() {
        this.creatimte = new Date();
        this.creatUser = "root";
        this.type = Config.wehcat_coltype;
        this.amount = 0;
        this.state = Config.Normal;
        this.ip = "127.0.0.1";
        this.wechatId = "0";
        this.lastUsetime = new Date();
        //        this.type =
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTeleNumber() {
        return teleNumber;
    }

    public void setTeleNumber(String teleNumber) {
        this.teleNumber = teleNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Date getCreatimte() {
        return creatimte;
    }

    public void setCreatimte(Date creatimte) {
        this.creatimte = creatimte;
    }

    public String getCreatUser() {
        return creatUser;
    }

    public void setCreatUser(String creatUser) {
        this.creatUser = creatUser;
    }

    @Override
    public String toString() {
        return "Wechat{" +
                "id=" + id +
                ", wechatName='" + wechatName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", wechatId='" + wechatId + '\'' +
                ", ip='" + ip + '\'' +
                ", teleNumber='" + teleNumber + '\'' +
                ", state='" + state + '\'' +
                ", type='" + type + '\'' +
                ", remark='" + remark + '\'' +
                ", amount=" + amount +
                ", creatimte=" + creatimte +
                ", creatUser='" + creatUser + '\'' +
                ", lastUsetime=" + lastUsetime +
                '}';
    }
}
