package com.example.demo.Repository;

import com.example.demo.Model.WechatItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * Created by snsoft on 17/7/2017.
 */
public interface WechatItemRepostitory extends JpaRepository<WechatItem, Integer>, JpaSpecificationExecutor<WechatItem> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update wechat_item p set p.state = ?2 where p.state = ?1 and over_time < now()", nativeQuery = true)
    int updateTask(String before, String after);
}
