package com.health.remind.strategy;

import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.pojo.vo.FrequencyVO;

import java.util.List;

/**
 * @author QQQtx
 * @since 2025/1/27 15:55
 */
public interface FrequencyStrategy {

    /**
     * 策略内容
     *
     * @param task      任务
     * @param frequency 频次
     */
    void strategyTask(RemindTask task, FrequencyVO frequency);

    /**
     * 获取头10次执行时间
     *
     * @param task      任务
     * @param frequency 频率
     * @return 执行详情
     */
    List<RemindTaskInfo> strategyTaskNumTen(RemindTask task, FrequencyVO frequency);
}
