package com.health.remind.scheduler.entity;

import com.baomidou.mybatisplus.extension.service.IService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author qtx
 * @since 2025/5/30 13:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DelaySqlTask<T> {

    private IService<T> service;

    private List<T> list;

    private Class<T> classT;
}
