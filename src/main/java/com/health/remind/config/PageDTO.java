package com.health.remind.config;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.exception.DataException;
import lombok.Setter;

/**
 * @author QQQtx
 * @since 2023/8/31 11:36
 */
@Setter
public class PageDTO<T> {

    private Long pageNo;

    private Long pageSize;

    @JsonIgnore
    public Page<T> getPage() {
        if (pageNo == null || pageSize == null) {
            throw new DataException(DataEnums.DATA_IS_ABNORMAL);
        }
        return com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO.of(pageNo, pageSize);
    }
}
