package com.example.demo.Utils;

import com.example.demo.Controller.UpdateService;
import com.example.demo.Model.Deposit;
import com.example.demo.Model.Proposal;
import com.example.demo.Model.Wechat;
import com.example.demo.Model.WechatItem;
import com.example.demo.Repository.Depositrepository;
import com.example.demo.Repository.ProposalRepository;
import com.example.demo.Repository.WechatItemRepostitory;
import com.example.demo.Service.BankcardService;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by snsoft on 27/9/2017.
 */
@Component
public class ScheduledTask {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);
    @Autowired
    UpdateService updateService;
    @Autowired
    BankcardService bankcardService;
    @Autowired
    ProposalRepository proposalRepository;
    @Autowired
    Depositrepository depositrepository;
    @Autowired
    WechatItemRepostitory wechatItemRepostitory;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void executeFileDownLoadTask() {
        // 间隔1分钟,执行工单上传任务
        Thread current = Thread.currentThread();
//        System.out.println("定时任务1:" + current.getId());
        logger.info("匹配单号任务:" + current.getId() + ",name:" + current.getName());
        List<Proposal> listProposal = bankcardService.getPropsal(1, 500, "", Config.Pending, 0L, 0L, "","").getContent();
        List<Deposit> listDeposit = bankcardService.getAll(1, 500, Config.Pending, 0L,0L,0L,0L,0L, 0L, "", "", "").getContent();
        for (int i = 0; i < listProposal.size(); i++) {
            logger.info("匹配单号任务:有" + listProposal.size() + "提案进来啦" + listDeposit.size() + "流水开始啦");
            if (listProposal.get(i).getOverTime() == null) {
                if (listProposal.get(i).getCreatTime().getTime() + 2 * 3600 * 1000 < (new Date()).getTime()) {
                    listProposal.get(i).setState(Config.OVERTIME);
                    listProposal.get(i).setUpdateTime(new Date());
                    proposalRepository.save(listProposal.get(i));
                }
            } else if (listProposal.get(i).getOverTime().getTime() < (new Date()).getTime()) {
                listProposal.get(i).setUpdateTime(new Date());
                listProposal.get(i).setState(Config.OVERTIME);
                proposalRepository.save(listProposal.get(i));
            } else {
                for (int j = 0; j < listDeposit.size(); j++) {
                    if (listDeposit.get(j).getWechatName().equals(listProposal.get(i).getWechatName())
                            && listProposal.get(i).getAmount() == listDeposit.get(j).getAmount()
                            && listDeposit.get(j).getNote().equals(listProposal.get(i).getNotes())
                            && (listDeposit.get(j).getCreatTime().getTime() < listProposal.get(i).getCreatTime().getTime() + 1800 * 1000)
                            && (listDeposit.get(j).getCreatTime().getTime() > listProposal.get(i).getCreatTime().getTime())) {
                        listProposal.get(i).setState(Config.EXECUTED);
                        listProposal.get(i).setState(Config.EXECUTED);
                        listProposal.get(i).setBillNo(listDeposit.get(j).getBillNo());
                        listDeposit.get(j).setState(Config.EXECUTED);
                        listDeposit.get(j).setTranTime(new Date());
                        listDeposit.get(j).setDepositNumber(listProposal.get(i).getDepositNumber());
                        WechatItem wechatlist = bankcardService.getAll(1, 1, listProposal.get(j).getWechatName(), "", 0, "id", listProposal.get(j).getNotes()).getContent().get(0);
                        wechatlist.setState(Config.Normal);
                        wechatItemRepostitory.save(wechatlist);
//                        if(wechatlist.get(0))
//                        wechatItemRepostitory.
                        listProposal.get(j).getNotes();
                        proposalRepository.save(listProposal.get(i));
                        depositrepository.save(listDeposit.get(j));
                        Task task = new Task(listProposal.get(i));
                        task.run();
                        break;
                    }
                }
            }
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void callbackTask() {
        Thread current = Thread.currentThread();
        logger.info("第三方回调:" + current.getId() + ",name:" + current.getName());
        List<Proposal> listProposal = bankcardService.getPropsal(1, 500, "", Config.EXECUTED, 0L, 0L, "","").getContent();
        for (int i = 0; i < listProposal.size(); i++) {
            Task task = new Task(listProposal.get(i));
            task.run();
        }
        wechatItemRepostitory.updateTask(Config.EXECUTED, Config.Normal);
    }

//    @Scheduled(cron = "0 0/5 * * * ?")
//    public void setImage() {
//        Thread current = Thread.currentThread();
//        logger.info("图片设置:" + current.getId() + ",name:" + current.getName());
////        List<Wechat> list = bankcardService.getAll(1, 1, "", Config.Normal, "", "id").getContent();
////        List<Proposal> listProposal = bankcardService.getPropsal(1, 500, "", Config.EXECUTED, 0L, 0L, "").getContent();
////        for (int i = 0; i < listProposal.size(); i++) {
////            Task task = new Task(listProposal.get(i));
////            task.run();
////        }
//    }

    class Task implements Runnable {
        public Proposal proposal;

        public Task(Proposal proposal) {
            this.proposal = proposal;
        }

        @Override
        public void run() {
            try {
                HttpClient httpClient = new HttpClient();
                //step2： 创建GET方法的实例，类似于在浏览器地址栏输入url    GetMethod getMethod = new GetMethod("http://192.168.12.106:8080/http/pss/initWechatQRData?wechatNumber=" + wechatPicture.getWechatName());
                // http://192.168.12.106:8080/http/pss/initWechatQRData?wechatNumber=test
                GetMethod getMethod = new GetMethod(proposal.getCallback() + "?depositNumber=" + proposal.getDepositNumber() + "&orderNumber=" + proposal.getOrderNumber() + "&amount=" + proposal.getAmount() + "&wechatName=" + proposal.getWechatName());
//                GetMethod getMethod = new GetMethod("\n" +
//                        "http://www.gzqingyuanfei.com/pay/personalwechatqrpay/iResult.jsp?depositNumber=917092800000047&orderNumber=1011506591577201&amount=10&wechatName=riley965");// 使用系统提供的默认的恢复策略
                logger.error(proposal.getCallback() + "?depositNumber=" + proposal.getDepositNumber() + "&orderNumber=" + proposal.getOrderNumber() + "&amount=" + proposal.getAmount() + "&wechatName=" + proposal.getWechatName());
                getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                        new DefaultHttpMethodRetryHandler());
                try {
                    //step3: 执行getMethod 类似于点击enter，让浏览器发出请求
                    int statusCode = httpClient.executeMethod(getMethod);
                    byte[] responseBody = getMethod.getResponseBody();
                    logger.error("statusCode:" + statusCode + "回调返回:" + " success:" + (new String(responseBody).toLowerCase().indexOf("success") != -1));
                    if (statusCode == HttpStatus.SC_OK && (new String(responseBody).toLowerCase().indexOf("success") != -1)) {
//                        logger.error("回调成功了");
                        proposal.setState(Config.SUCCESS);
                        proposal.setUpdateTime(new Date());
                        proposalRepository.save(proposal);
                    } else {
                    }
                    //step4: 读取内容,浏览器返回结果//处理内容

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
            } catch (Exception e) {
                logger.error("第三方报错:" + e.getMessage());
            }

        }
    }
}




