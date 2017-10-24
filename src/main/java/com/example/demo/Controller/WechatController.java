package com.example.demo.Controller;

import com.example.demo.Model.Deposit;
import com.example.demo.Model.Proposal;
import com.example.demo.Model.Result;
import com.example.demo.Model.Wechat;
import com.example.demo.Repository.Depositrepository;
import com.example.demo.Repository.ProposalRepository;
import com.example.demo.Repository.WechatRepository;
import com.example.demo.Service.BankcardService;
import com.example.demo.Utils.Config;
import com.example.demo.Utils.ResultUtil;
import com.example.demo.exception.GirlException;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by snsoft on 26/9/2017.
 */
@RestController
public class WechatController {
    @Autowired
    WechatRepository wechatRepostitory;
    @Autowired
    BankcardService bankcardService;
    @Autowired
    ProposalRepository proposalRepository;
    @Autowired
    Depositrepository depositRepository;

    //    @Autowired
//    @Resource(name = "DefaultStringRedisTemplate")
//    private RedisTemplate defaultStringRedis;
    @Autowired
    @Resource(name = "DefaultRedisTemplate")
    private RedisTemplate defaultRedis;

    @PostMapping(value = "/wechatitem")
    public Result addWechat(Wechat wechat) {
        try {
            return bankcardService.creatWechat(wechat);
        } catch (Exception e) {
            throw new GirlException(e.getMessage(), -1);
        }
    }

    @GetMapping(value = "/wechat")
    public Result getWechat(@RequestParam("name") String name, @RequestParam("type") String type, @RequestParam("state") String state, @RequestParam("page") int page, @RequestParam("size") int size) {

        try {
            return ResultUtil.success(bankcardService.getAll(page, size, name, state, "", "id").getContent());
        } catch (Exception e) {
            throw new GirlException(e.getMessage(), -1);
        }
//        List<Wechat> wechatlist = bankcardService.getAll(page, size, name, state, "", "", "");
    }

    @GetMapping(value = "/wechatitem")
    public Result get(@RequestParam("name") String name, @RequestParam("type") String type, @RequestParam("state") String state, @RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("note") int note) {
        try {
            return ResultUtil.success(bankcardService.getAll(page, size, name, state, 0, type, ""));
        } catch (Exception e) {
            throw new GirlException(e.getMessage(), -1);
        }
    }

    @PutMapping(value = "/wechatitem")
    public Result updateWechat(Wechat wechat) {
//        try {
        List<Wechat> list = new ArrayList();
        Result result = new Result();
        Wechat item = wechatRepostitory.save(wechat);
        if (item != null) {
            result.setMsg("修改成功");
            list.add(item);
            result.setCode(200);
        } else
            result.setMsg("添加失败");
        result.setData(list);
        return result;
//        } catch (Exception e) {
//            throw new GirlException(e.getMessage(), -1);
//        }
    }

    @DeleteMapping(value = "/wechatitem")
    public Result deleWechat(@RequestParam("id") Integer id) {
        try {
            wechatRepostitory.delete(id);
            return ResultUtil.success();
        } catch (Exception e) {
            throw new GirlException(e.getMessage(), -1);
        }
    }

    @PutMapping("/proposal")
    public Result updateProposal(Proposal proposal) {
        Result result = new Result();
        List<Proposal> list = new ArrayList<>();
        list.add(proposalRepository.save(proposal));
        result.setData(list);
        result.setCode(200);
        return result;
    }

    @PostMapping("/proposal")
    public Result addProposal(Proposal proposal) {
        try {
            Result result = new Result();
            List<Proposal> list = new ArrayList<>();
            list.add(proposalRepository.save(proposal));
            result.setData(list);
            result.setCode(200);
//            bankcardService.addproposallist(proposal, Config.Proposallist);
            return result;
        } catch (Exception e) {
            throw new GirlException(e.getMessage(), -1);
        }
    }

    @GetMapping("/proposal")
    public Result getProposal(@RequestParam("depositNumber") String depositNumber, @RequestParam("state") String state, @RequestParam("beginTime") Long beginTime, @RequestParam("endTime") Long endTime, @RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("username") String username, @RequestParam("billNo") String billNo) {
//        List<Proposal> list = (List<Proposal>) defaultRedis.opsForValue().get(Config.Proposallist);
//        bankcardService.getproposallist(Config.Proposallist);
//        list = getPropsal(1, 500, "", Config.Pending, 0L, 0L, "").getContent();
        try {
            return ResultUtil.success(bankcardService.getPropsal(page, size, depositNumber, state, beginTime, endTime, username, billNo).getContent());
        } catch (Exception e) {
            throw new GirlException(e.getMessage(), -1);
        }
    }

    @DeleteMapping("/proposal")
    public Result deletPropsal(@RequestParam("id") Integer id) {
        try {
            List<Proposal> list = new ArrayList<Proposal>();
            proposalRepository.delete(id);
            return ResultUtil.success(list);
        } catch (Exception e) {
            throw new GirlException(e.getMessage(), -1);
        }
    }

    @GetMapping("/setProposal")
    public Result setPorposalitem(@RequestParam("name") String name, @RequestParam("depositNumber") String depositNumber, @RequestParam("amount") String amounts, @RequestParam("callback") String callback, @RequestParam("wechatName") String wechatName) {
//        Config.callback = callback;
        int amount = Double.valueOf(amounts).intValue();
        List<Wechat> wechalist = bankcardService.getAll(1, 1, wechatName, Config.Normal, "", "id").getContent();
        if (wechalist.size() == 0) {
            throw new GirlException("微信号不存在", -1);
        }
        try {
            if (amount != 10 && amount != 20 && amount != 50 && amount != 100 && amount != 200)
                throw new GirlException("金额不支持", -1);
            List<Proposal> list = bankcardService.getPropsal(1, 1, depositNumber, "", 0L, 0L, "", "").getContent();
            if (list.size() > 0)
                return ResultUtil.success(list.get(0));
            return bankcardService.setPropsal(name, (int) amount, depositNumber, wechatName, callback);
        } catch (Exception e) {
            if (e.getMessage().indexOf("UK_h3wfobabkuemsyihbtdwrrehy") != -1) {
                throw new GirlException("订单号已存在", -1);
            } else
                throw new GirlException(e.getMessage(), -1);
        }
    }

    @PostMapping("/deposit")
    public Result addDeposit(Deposit deposit) {
        try {
            List<Deposit> list = new ArrayList<Deposit>();
            list.add(depositRepository.save(deposit));
            return ResultUtil.success(list);
        } catch (Exception e) {
            throw new GirlException(e.getMessage(), -1);
        }
    }

    @GetMapping("/deposit")
    public Result getDeposit(@RequestParam("page") int pageNumber, @RequestParam("size") int pageSize, @RequestParam("state") String state, @RequestParam("beginTime") Long beginTime, @RequestParam("endTime") Long endTime, @RequestParam("beginTranstime") Long beginTranstime, @RequestParam("endTranstime") Long endTranstime, @RequestParam("beginExcuteTime") Long beginExcuteTime, @RequestParam("endExcuteTime") Long endExcuteTime, @RequestParam("wechatName") String wechatName, @RequestParam("depositNumber") String depositNumber, @RequestParam("billNo") String billNo) {
        try {
            return ResultUtil.success(bankcardService.getAll(pageNumber, pageSize, state, beginTime, endTime, beginTranstime, endTranstime, beginExcuteTime, endExcuteTime, wechatName, depositNumber, billNo).getContent());
        } catch (Exception e) {
            throw new GirlException(e.getMessage(), -1);
        }
    }

    @PutMapping("/deposit")
    public Result updateDeposit(Deposit deposit) {
        try {
            List<Deposit> list = new ArrayList<Deposit>();
            list.add(depositRepository.save(deposit));
            return ResultUtil.success(list);
        } catch (Exception e) {
            throw new GirlException(e.getMessage(), -1);
        }
    }

    @DeleteMapping("/deposit")
    public Result deleteDeposit(@RequestParam("id") Integer id) {
        try {
            List<Deposit> list = new ArrayList<Deposit>();
            depositRepository.delete(id);
            return ResultUtil.success(list);
        } catch (Exception e) {
            throw new GirlException(e.getMessage(), -1);
        }
    }
}
