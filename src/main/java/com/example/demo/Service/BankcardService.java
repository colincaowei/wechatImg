package com.example.demo.Service;

import com.example.demo.Model.*;
import com.example.demo.Repository.*;
import com.example.demo.Utils.*;
import com.example.demo.exception.GirlException;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by snsoft on 23/8/2017.
 */
@Service
public class BankcardService {
    @Autowired
    private BankCardRepository bankcard;
    @Autowired
    private WechatPictureRepository wechatPicture;
    @Autowired
    private WechatRepository wechatRepository;
    @Autowired
    private WechatItemRepostitory wechatitemRepository;
    @Autowired
    ProposalRepository proposalRepository;
    @Autowired
    Depositrepository depositRepository;
    @Autowired
    @Resource(name = "DefaultRedisTemplate")
    private RedisTemplate defaultRedis;
    @Autowired
    private ListMapFtp listMapFtp;

    public Page<BankCard> getSourceCode(int pageNumber, int pageSize) {
        PageRequest request = this.buildPageRequest(pageNumber, pageSize);
        Page<BankCard> sourceCodes = this.bankcard.findAll(request);
        return sourceCodes;
    }

    public Page<WechatPicture> getWechatSourceCode(int pageNumber, int pageSize) {
        PageRequest request = this.buildPageRequest(pageNumber, pageSize);
        Page<WechatPicture> sourceCodes = this.wechatPicture.findAll(request);
//        sourceCodes.
        return sourceCodes;
    }

    private PageRequest buildPageRequest(int pageNumber, int pagzSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return new PageRequest(pageNumber - 1, pagzSize, sort);
    }

    private PageRequest buildPageRequest(int pageNumber, int pagzSize, String properties) {
        Sort sort = new Sort(Sort.Direction.ASC, properties);
        return new PageRequest(pageNumber - 1, pagzSize, sort);
    }


    public Page<WechatPicture> getAll(int pageNumber, int pageSize, final String wechatName, final String state) {
        PageRequest request = this.buildPageRequest(pageNumber, pageSize);
        Page<WechatPicture> result = wechatPicture.findAll(new Specification<WechatPicture>() {

            public Predicate toPredicate(Root<WechatPicture> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
//                Path<String> namePath = root.get("name");
                Path<String> nicknamePath = root.get("wechatName");
                Path<String> statePath = root.get("state");
                Predicate predicate = cb.conjunction();
                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                if (!"".equals(wechatName))
                    predicate.getExpressions().add(cb.like(nicknamePath, "%" + wechatName + "%")); //这里可以设置任意条查询条件
                if (!"".equals(state))
                    predicate.getExpressions().add(cb.equal(statePath, state));
                return predicate;
            }

        }, request);
        return result;
    }

    public Page<Wechat> getAll(int pageNumber, int pageSize, final String wechatName, final String state, final String type, String properties) {
        PageRequest request = this.buildPageRequest(pageNumber, pageSize, properties);
        Page<Wechat> result = wechatRepository.findAll(new Specification<Wechat>() {

            public Predicate toPredicate(Root<Wechat> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
//                Path<String> namePath = root.get("name");
                Path<String> nicknamePath = root.get("wechatName");
                Path<String> statePath = root.get("state");
                Path<String> typePath = root.get("type");
                Predicate predicate = cb.conjunction();
                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                if (!"".equals(wechatName))
                    predicate.getExpressions().add(cb.like(nicknamePath, "%" + wechatName + "%")); //这里可以设置任意条查询条件
                if (!"".equals(state))
                    predicate.getExpressions().add(cb.equal(statePath, state));
                if (!"".equals(type))
                    predicate.getExpressions().add(cb.equal(typePath, type));
                return predicate;
            }

        }, request);
        return result;
    }

    public Page<WechatItem> getAll(int pageNumber, int pageSize, final String wechatName, final String state, final int amount, String properties, final String note) {
        PageRequest request = this.buildPageRequest(pageNumber, pageSize, properties);
        Page<WechatItem> result = wechatitemRepository.findAll(new Specification<WechatItem>() {

            public Predicate toPredicate(Root<WechatItem> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
//                Path<String> namePath = root.get("name");
                Path<String> nicknamePath = root.get("wechatName");
                Path<String> statePath = root.get("state");
                Path<String> amountPath = root.get("amount");
                Path<String> notedPath = root.get("note");
                Predicate predicate = cb.conjunction();
                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                if (!"".equals(wechatName))
                    predicate.getExpressions().add(cb.like(nicknamePath, "%" + wechatName + "%")); //这里可以设置任意条查询条件
                if (!"".equals(state))
                    predicate.getExpressions().add(cb.equal(statePath, state));
                if (amount != 0)
                    predicate.getExpressions().add(cb.equal(amountPath, amount));
                if (!"".equals(note))
                    predicate.getExpressions().add(cb.equal(notedPath, note));
                return predicate;
            }

        }, request);
        return result;
    }

    public Page<Proposal> getPropsal(int pageNumber, int pageSize, final String depositNumber, final String state, final Long beginTime, final Long endTime, final String username, final String billNo) {
        PageRequest request = this.buildPageRequest(pageNumber, pageSize);
        Page<Proposal> result = proposalRepository.findAll(new Specification<Proposal>() {

            public Predicate toPredicate(Root<Proposal> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
//                Path<String> namePath = root.get("name");
                Path<String> depositNumberPath = root.get("depositNumber");
                Path<String> statePath = root.get("state");
                Path<Date> creatTimePath = root.<Date>get("creatTime");
//                Path<String> updateTimePath =<> root.get("updateTime");
                Path<String> usernamePath = root.get("username");
                Path<String> billNoPath = root.get("billNo");
//                Path<String> typePath = root.get("type");
                Predicate predicate = cb.conjunction();
                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                if (!"".equals(depositNumber))
                    predicate.getExpressions().add(cb.equal(depositNumberPath, depositNumber)); //这里可以设置任意条查询条件
                if (!"".equals(state))
                    predicate.getExpressions().add(cb.equal(statePath, state));
                if (beginTime != 0L && endTime != 0L)
//                    predicate.getExpressions().add(cb.));
                    predicate.getExpressions().add(cb.between(creatTimePath, new Date(beginTime), new Date(endTime)));
                if (!"".equals(username))
                    predicate.getExpressions().add(cb.equal(usernamePath, username));
                if (!"".equals(billNo))
                    predicate.getExpressions().add(cb.equal(usernamePath, username));
                return predicate;
            }

        }, request);
        return result;
    }

    @Transactional
    public Result creatWechat(Wechat wechat) throws Exception {
        Result result = new Result();
        result.setCode(200);
        List<Wechat> wechatlist = new ArrayList<Wechat>();
        wechatlist.add(wechatRepository.save(wechat));
        result.setData(wechatlist);
        List<List<DtsFtpFile>> list = listMapFtp.showList(Config.hostname, Config.port,
                Config.username, Config.password, "/images/" + wechat.getWechatName());// 获得ftp对应路径下的所有目录和文件信息
        List<DtsFtpFile> listDirectory = list.get(0);// 获得ftp该路径下的所有目录信息
        List<DtsFtpFile> listFile = list.get(1);// 获得ftp该路径下所有的文件信息
        for (int i = 0; i < listFile.size(); i++) {
            WechatItem wechatItem = new WechatItem();
            wechatItem.setAmount(Integer.parseInt(listFile.get(i).getName().substring(2, listFile.get(i).getName().indexOf("_"))));
            wechatItem.setNote(listFile.get(i).getName().substring(0, listFile.get(i).getName().indexOf(".")));
            wechatItem.setUrl(Config.url + "/" + wechat.getWechatName() + "/" + listFile.get(i).getName());
            wechatItem.setWechatName(wechat.getWechatName());
            wechatitemRepository.save(wechatItem);
        }
        return result;
    }

    @Transactional
    public Result setPropsal(String name, int amount, String depositNumber, String wechatName, String callback) {
//        Wechat wechatlist = getAll(1, 1, "", Config.Normal, "", "lastUsetime").getContent().get(0);
        WechatItem wechatItemlist = null;
        try {
            wechatItemlist = getAll(1, 1, wechatName, Config.Normal, amount, "lastUsetime", "").getContent().get(0);
            wechatItemlist.setLastUsetime(new Date());
            wechatItemlist.setState(Config.EXECUTED);
            wechatItemlist.setOverTime(new Date(new Date().getTime() + 2 * 3600 * 1000));
//        wechatRepository.save(wechatlist);
            wechatitemRepository.save(wechatItemlist);
            Proposal proposal = new Proposal();
            proposal.setAmount(amount);
            proposal.setUsername(name);
            proposal.setOrderNumber(wechatItemlist.getId() + "" + (new Date()).getTime());
            proposal.setUrl(wechatItemlist.getUrl());
            proposal.setNotes(wechatItemlist.getNote());
            proposal.setDepositNumber(depositNumber);
            proposal.setWechatName(wechatName);
            proposal.setCallback(callback);
            proposalRepository.save(proposal);
            return ResultUtil.success(proposal);
        } catch (Exception e) {
            throw new GirlException("无可用的二维码", -1);
        }
//         wechatItemlist = getAll(1, 1, wechatName, Config.Normal, amount, "lastUsetime","").getContent().get(0);
//        wechatlist.setLastUsetime(new Date());
    }

    @Transactional
    public void setBankcard(WechatPicture wechatPicture, WechatPictureRepository wechat, Result result) {

        if (wechatPicture.getState().equals(WechatEnume.wechat_surecrete)) {
            HttpClient httpClient = new HttpClient();
            //step2： 创建GET方法的实例，类似于在浏览器地址栏输入url    GetMethod getMethod = new GetMethod("http://192.168.12.106:8080/http/pss/initWechatQRData?wechatNumber=" + wechatPicture.getWechatName());
            // http://192.168.12.106:8080/http/pss/initWechatQRData?wechatNumber=test
            GetMethod getMethod = new GetMethod("http://papi-pacnet.pms8.me/http/pss/initWechatQRData?wechatNumber=" + wechatPicture.getWechatName());
//            GetMethod getMethod = new GetMethod("http://192.168.12.106:8080/http/pss/initWechatQRData?wechatNumber=" + wechatPicture.getWechatName());
            // 使用系统提供的默认的恢复策略
            getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                    new DefaultHttpMethodRetryHandler());
            try {
                //step3: 执行getMethod 类似于点击enter，让浏览器发出请求
                int statusCode = httpClient.executeMethod(getMethod);
                if (statusCode != HttpStatus.SC_OK) {
                    System.err.println("Method failed: "
                            + getMethod.getStatusLine());
                } else {
                    if (wechat.save(wechatPicture) != null) {
                        result.setData(wechat.save(wechatPicture));
                        result.setCode(200);
                        result.setMsg("成功");
                    }
                }
                //step4: 读取内容,浏览器返回结果
                byte[] responseBody = getMethod.getResponseBody();
                //处理内容
                System.out.println(new String(responseBody));
            } catch (HttpException e) {
                //发生致命的异常，可能是协议不对或者返回的内容有问题
                System.out.println("Please check your provided http address!");
                e.printStackTrace();
            } catch (IOException e) {
                //发生网络异常
                e.printStackTrace();
            } finally {
                //释放连接 （一定要记住）
                getMethod.releaseConnection();
            }
        } else {
//            result.setData(wechat.save(wechatPicture));
            if (wechat.save(wechatPicture) != null) {
                result.setData(wechat.save(wechatPicture));
                result.setCode(200);
                result.setMsg("成功");
            }
        }
    }

    public Page<Deposit> getAll(int pageNumber, int pageSize, final String state, final Long beginTime, final Long endTime, final Long beginTranstime, final Long endTranstime, final Long beginExcuteTime, final Long endExcuteTime, final String wechatName, final String depositNumber, final String billNo) {
        PageRequest request = this.buildPageRequest(pageNumber, pageSize);
        Page<Deposit> result = depositRepository.findAll(new Specification<Deposit>() {

            public Predicate toPredicate(Root<Deposit> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
//                Path<String> namePath = root.get("name");
                Path<String> depositNumberPath = root.get("depositNumber");
                Path<String> statePath = root.get("state");
                Path<Date> creatTimePath = root.<Date>get("creatTime");
                Path<Date> tranTimePath = root.<Date>get("tranTime");
                Path<Date> excuteTimePath = root.<Date>get("excuteTime");
//                Path<String> updateTimePath =<> root.get("updateTime");
                Path<String> wechatNamePath = root.get("wechatName");
                Path<String> billNoPath = root.get("billNo");
//                Path<String> typePath = root.get("type");
                Predicate predicate = cb.conjunction();
                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                if (!"".equals(depositNumber))
                    predicate.getExpressions().add(cb.equal(depositNumberPath, "%" + depositNumber + "%")); //这里可以设置任意条查询条件
                if (!"".equals(state))
                    predicate.getExpressions().add(cb.equal(statePath, state));
                if (beginTime != 0L && endTime != 0L)
                    predicate.getExpressions().add(cb.between(creatTimePath, new Date(beginTime), new Date(endTime)));
                if (beginTranstime != 0L && endTranstime != 0L)
                    predicate.getExpressions().add(cb.between(tranTimePath, new Date(beginTranstime), new Date(endTranstime)));
                if (beginExcuteTime != 0L && endExcuteTime != 0L)
                    predicate.getExpressions().add(cb.between(excuteTimePath, new Date(beginExcuteTime), new Date(endExcuteTime)));
                if (!"".equals(wechatName))
                    predicate.getExpressions().add(cb.like(wechatNamePath, wechatName));
                if (!"".equals(billNo))
                    predicate.getExpressions().add(cb.like(billNoPath, billNo));
                return predicate;
            }
        }, request);
        return result;
    }
}
