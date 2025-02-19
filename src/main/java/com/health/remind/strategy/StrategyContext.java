package com.health.remind.strategy;

import com.health.remind.strategy.impl.DoNothing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author QQQtx
 * @since 2025/1/27 16:28
 */
public class StrategyContext {

    private static final Logger log = LoggerFactory.getLogger(StrategyContext.class);

    private final Map<String, FrequencyStrategy> REGISTER_MAP = new HashMap<>();

    private static StrategyContext instance;

    private StrategyContext() {
        // Private constructor
    }

    public static StrategyContext getInstance() {
        if (instance == null) {
            instance = new StrategyContext();
        }
        return instance;
    }

    public static void registerStrategy(String type, FrequencyStrategy frequency) {
        getInstance().insertIntoRegisterMap(type, frequency);
    }

    private void insertIntoRegisterMap(String type, FrequencyStrategy frequency) {
        REGISTER_MAP.putIfAbsent(type, frequency);
    }

    public static FrequencyStrategy getStrategy(String type) {
        return getInstance().getFromRegisterMap(type);
    }

    private FrequencyStrategy getFromRegisterMap(String type) {
        FrequencyStrategy strategy = REGISTER_MAP.getOrDefault(type, new DoNothing());
        log.debug("frequency type:{}", strategy.getClass().getName());
        return strategy;
    }
}
