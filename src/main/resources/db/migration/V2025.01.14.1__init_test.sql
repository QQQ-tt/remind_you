create table test
(
    id          bigint           not null
        primary key,
    name        varchar(50)      null,
    old_name    varchar(50)      null,
    num         int              null,
    all_num     int              null,
    tenant_id   bigint           null comment '租户id',
    create_id   bigint           null comment '创建人id',
    create_name varchar(100)     null comment '创建人名',
    create_time datetime         null comment '创建时间',
    update_id   bigint           null comment '更新人id',
    update_name varchar(100)     null comment '更新人名',
    update_time datetime         null comment '更新时间',
    delete_flag bit default b'0' null comment '逻辑删除'
)
    comment '测试表';