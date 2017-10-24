package com.example.demo.Service;


import com.example.demo.Model.ResultEnum;
import com.example.demo.Model.Wechat;
import com.example.demo.Model.WechatItem;
import com.example.demo.Repository.WechatItemRepostitory;
import com.example.demo.Repository.WechatRepository;
import com.example.demo.exception.GirlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by snsoft on 17/7/2017.
 */
@Service
public class WechatService {
    @Autowired
    private WechatItemRepostitory wechatItemRepostitory;
    @Autowired
    private WechatRepository wechatRepository;

    @Transactional
    public void CreateWechat(Wechat wechat) {
        wechatRepository.save(wechat);
//        for (int i = 0; i < wechat.getTenAmount(); i++) {
//            WechatItem wechatItem = new WechatItem();
//            wechatItem.setWechatName(wechat.getWehcahtName());
//            wechatItem.setAmount(10);
//            wechatItem.setNote(wechat.getWehcahtName() + "_10" + i);
//            wechatItemRepostitory.save(wechatItem);
//        }
//        set(wechat.getTenAmount(), 10, wechat.getWechatName());
//        set(wechat.getTwnAmount(), 20, wechat.getWechatName());
//        set(wechat.getFifAmount(), 50, wechat.getWechatName());
//        set(wechat.getHurAmount(), 100, wechat.getWechatName());
//        set(wechat.getTwnhurAmount(), 200, wechat.getWechatName());
//        set(wechat.getFivhurAmount(), 500, wechat.getWechatName());
//        set(wechat.getQianAmount(), 1000, wechat.getWechatName());
    }

    public void set(int size, int amount, String name) {
        for (int i = 0; i < size; i++) {
            WechatItem wechatItem = new WechatItem();
            wechatItem.setWechatName(name);
            wechatItem.setAmount(amount);
            wechatItem.setNote(name + "_" + amount + "" + i);
            wechatItemRepostitory.save(wechatItem);
        }

    }
    public void getAge(int age) throws  Exception{
        if(age<10){
            throw new GirlException("小孩子上小学",100);
        }else if(age>9 && age<17){
            throw new GirlException("小孩子上初中",102);
        }else{
            throw new GirlException(ResultEnum.SUCCESS);
        }
    }
}
