package com.health.remind.test.json;

import com.alibaba.fastjson2.JSONObject;
import com.health.remind.entity.RemindTask;
import com.health.remind.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author qtx
 * @since 2025/5/26 16:48
 */
@Slf4j
public class JsonTest {

    @Test
    public void test() {
        RemindTask task = new RemindTask();
        task.setName("测试");
        task.setStartTime(LocalDateTime.now());
        task.setEndTime(LocalDateTime.now()
                .plusDays(1));
        task.setPushNum(1);
        task.setCreateTime(LocalDateTime.now());
        String jsonString = JSONObject.toJSONString(task);
        log.info("时间对象：{}", jsonString);

        SysUser sysUser = new SysUser();
        sysUser.setName("测试");
        sysUser.setCreateTime(LocalDateTime.now());
        String jsonString1 = JSONObject.toJSONString(sysUser);
        log.info("用户对象：{}", jsonString1);
    }

    @Test
    public void test1() {
        String json = "{\"template_id\":\"ahi62RYx-WStwOclzC26ZEBaSgZNaVWjs_eyuFefWzM\"," +
                "\"touser\":\"oebiw7fmRwIsVdXLLNdcX6-pRT4g\",\"miniprogram_state\":\"formal\",\"lang\":\"zh_CN\",\"data\":{\"date6\":{\"value\":\"2025-05-27\"},\"thing7\":{\"value\":\"sd\"},\"thing9\":{},\"time11\":{\"value\":\"2025-05-27 13:11\"}}}";
        int length = json.getBytes(StandardCharsets.UTF_8).length;
        log.info("发送微信消息: {}", length);

        String json1 = "{\"template_id\":\"ahi62RYx-WStwOclzC26ZEBaSgZNaVWjs_eyuFefWzM\"," +
                "\"touser\":\"oebiw7fmRwIsVdXLLNdcX6-pRT4g\",\"miniprogram_state\":\"formal\",\"lang\":\"zh_CN\",\"data\":{\"date6\":{\"value\":\"2025-05-27\"},\"thing7\":{\"value\":\"sd\"},\"time11\":{\"value\":\"2025-05-27 13:11\"}}}";
        int length1 = json1.getBytes(StandardCharsets.UTF_8).length;
        log.info("发送微信消息1: {}", length1);

        // 2025-05-27 13:39:43 [main] INFO  com.health.remind.test.json.JsonTest - 发送微信消息: 259
        // 2025-05-27 13:39:43 [main] INFO  com.health.remind.test.json.JsonTest - 发送微信消息1: 247
    }
}
