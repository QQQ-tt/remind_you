package com.health.remind.scheduler;

import com.baomidou.mybatisplus.extension.service.IService;
import com.health.remind.scheduler.entity.DelaySqlTask;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author qtx
 * @since 2025/5/30 13:37
 */
@Slf4j
@Component
public class DelaySqlScheduledExecutor extends ScheduledBase {

    // 调度线程池
    private final ScheduledExecutorService scheduler;

    private final TransactionTemplate transactionTemplate;

    public DelaySqlScheduledExecutor(ScheduledExecutorService scheduler, TransactionTemplate transactionTemplate) {
        this.scheduler = scheduler;
        this.transactionTemplate = transactionTemplate;
    }

    @PostConstruct
    public void start() {
        // 启动监听线程
        scheduler.scheduleAtFixedRate(this::processTasks, 0, 500, TimeUnit.MILLISECONDS);
    }

    public static <T> void putDelaySqlTask(
            IService<T> service, List<T> list, Class<T> classT) throws InterruptedException {
        DelaySqlTask<?> delaySqlTask = new DelaySqlTask<>(service, list, classT);
        delaySqlScheduledExecutorQueue.put(delaySqlTask);
    }

    @SuppressWarnings("unchecked")
    public void processTasks() {
        ArrayList<DelaySqlTask<?>> list = new ArrayList<>();
        delaySqlScheduledExecutorQueue.drainTo(list);
        if (list.isEmpty()) {
            return;
        }
        HashMap<Class<?>, Pair<IService<?>, List<Object>>> map = new HashMap<>();
        list.forEach(task -> {
            Pair<IService<?>, List<Object>> pair = map.computeIfAbsent(task.getClassT(),
                    k -> Pair.of(task.getService(), new ArrayList<>()));
            pair.getSecond()
                    .addAll(task.getList());
        });
        map.forEach((k, v) -> {
            try {
                transactionTemplate.execute(status -> {
                    IService<Object> first = (IService<Object>) v.getFirst();
                    first.saveOrUpdateBatch(v.getSecond(), 500);
                    log.info("批量保存成功：类型={}，数量={}", k.getSimpleName(), v.getSecond()
                            .size());
                    return null;
                });
            } catch (Exception e) {
                log.error("批量保存失败：{}", e.getMessage());
            }
        });
    }
}
