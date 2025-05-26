package com.health.remind.test.json;

import com.alibaba.fastjson2.JSONObject;
import com.health.remind.entity.RemindTask;
import com.health.remind.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

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
        task.setEndTime(LocalDateTime.now().plusDays(1));
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
}
