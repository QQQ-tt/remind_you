create table sys_user
(
    id          bigint           not null
        primary key,
    name        varchar(50)      null comment '用户名称',
    account     bigint           not null comment '账户',
    password    varchar(200)     not null comment '密码',
    telephone   varchar(13)      null comment '电话',
    status      bit default b'0' null comment '是否启用',
    sys_role_id bigint           null comment '用户角色id',
    tenant_id   bigint           null comment '租户id',
    create_id   bigint           null comment '创建人id',
    create_name varchar(100)     null comment '创建人名',
    create_time datetime         null comment '创建时间',
    update_id   bigint           null comment '更新人id',
    update_name varchar(100)     null comment '更新人名',
    update_time datetime         null comment '更新时间',
    delete_flag bit default b'0' null comment '逻辑删除'
);

