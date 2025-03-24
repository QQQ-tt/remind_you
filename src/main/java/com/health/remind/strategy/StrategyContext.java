package com.health.remind.strategy;

import com.health.remind.strategy.impl.DoNothing;
import com.health.remind.util.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author QQQtx
 * @since 2025/1/27 16:28
 */
@Component
public class StrategyContext {

    private static final Logger log = LoggerFactory.getLogger(StrategyContext.class);

    private final Map<String, FrequencyStrategy> REGISTER_MAP = new HashMap<>();

    private static StrategyContext instance;

    private StrategyContext() {
        // Private constructor
    }

    public static StrategyContext getInstance() {
        if (instance == null) {
            instance = SpringUtils.getBean(StrategyContext.class);
        }
        return instance;
    }

    @Autowired
    public void initStrategies(List<FrequencyStrategy> strategies) {
        strategies.forEach(strategy ->
                REGISTER_MAP.put(
                        strategy.getClass()
                                .getSimpleName()
                                .replaceAll("\\$\\$.*", "")
                                .toLowerCase(),
                        strategy
                )
        );
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
