package com.health.remind.strategy;

/**
 * @author qtx
 * @since 2025/1/27 16:27
 */
public abstract class AbstractStrategy implements FrequencyStrategy {

    /**
     * 类注册方法
     */
    public void register() {
        StrategyContext.registerStrategy(getClass().getSimpleName()
                .toLowerCase(), this);
    }
}
