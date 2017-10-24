package com.example.demo.Repository;

import com.example.demo.Model.Wechat;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by snsoft on 17/7/2017.
 */
public interface WechatRepository extends JpaRepository<Wechat,Integer>,JpaSpecificationExecutor<Wechat> {

//    public Wechat save(
//            super.
////            return super.save();
//    )
}
