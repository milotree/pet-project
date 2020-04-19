package com.lijin.quartz;

import com.alibaba.fastjson.JSONObject;
import com.lijin.entity.OrderClear;
import com.lijin.entity.Pet;
import com.lijin.entity.SUPN;
import com.lijin.entity.Saler;
import com.lijin.service.OrderClearService;
import com.lijin.service.PetService;
import com.lijin.service.SalerService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class DelJob extends QuartzJobBean {
    @Autowired
    private OrderClearService orderClearService;
    @Autowired
    private PetService petService;
    @Autowired
    private SalerService salerService;
    @Autowired
    private RedisTemplate redisTemplate;

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setOrderClearService(OrderClearService orderClearService) {
        this.orderClearService = orderClearService;
    }

    public void setPetService(PetService petService) {
        this.petService = petService;
    }

    public void setSalerService(SalerService salerService) {
        this.salerService = salerService;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //打印当前的执行时间
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("现在的时间是：" + sf.format(date));


    //具体的业务逻辑 此处为将未支付的订单取消
        System.out.println("删除所有未付款的订单");
        List<OrderClear> orderClears = orderClearService.CheckAll();
        for (OrderClear orderClear : orderClears) {
            if (orderClear.getOstatus() == 0) {
                List<SUPN> list = JSONObject.parseArray(orderClear.getNameandnum(), SUPN.class);//将存入的json字符串转为对象数组
                for (SUPN supn : list) {
                    String pname = supn.getPname();//得到名字
                    String sname = supn.getSname();//得到卖家姓名
                    Saler saler = salerService.findBySname(sname);//得到卖家姓名
                    Pet pet = petService.findByPname(pname, saler);//查询出宠物信息
                    pet.setPnum(pet.getPnum()+supn.getPnum());
                    petService.petSave(pet);
                }
                orderClearService.DelAll(orderClear.getOid());
            }

            if (orderClear.getOstatus() == 1){
                if ((new Date().getTime()-Integer.valueOf(orderClear.getOtime()))>=1296000){
                    System.out.println("开始自动收货");
                    orderClear.setOstatus(-1);
                    orderClearService.saveOrderClear(orderClear);
                    System.out.println("收货完成");
                }
            }

            Set<String> keys = redisTemplate.keys("*");
            if (keys != null) {
                redisTemplate.delete(keys);
            }

        }


    }
}
