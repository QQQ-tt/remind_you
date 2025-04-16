package com.health.remind.scheduler;

import com.health.remind.common.keys.RedisKeys;
import com.health.remind.service.SysRoleResourceService;
import com.health.remind.util.RedisUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @author QQQtx
 * @since 2025/2/19
 */
@Component
public class ScheduledTasks {

    private final SysRoleResourceService sysRoleResourceService;

    public ScheduledTasks(SysRoleResourceService sysRoleResourceService) {
        this.sysRoleResourceService = sysRoleResourceService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void removeExpiredData() {
        // 定时删除过期数据
    }

    @Scheduled(fixedDelay = 1000 * 60 * 20)
    public void initRoleResource() {
        // 定时初始化角色资源
        RedisUtils.delete(RedisUtils.keys(RedisKeys.getRoleResourceKey(null)));
        sysRoleResourceService.listRoleResourceByRoleId(new ArrayList<>());
    }
}
