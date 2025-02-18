create table sys_resource
(
    id          bigint           not null
        primary key,
    name        varchar(50)      null comment '资源名称',
    url         varchar(500)     not null comment '接口地址/路由地址',
    type        varchar(20)      null comment '资源类型：菜单、接口等',
    method      varchar(20)      null comment '方法类型',
    description varchar(200)     null comment '详细描述',
    status      bit default b'0' not null comment '是否启用',
    parent_id   bigint           null comment '父id',
    tenant_id   bigint           null comment '租户id',
    create_id   bigint           null comment '创建人id',
    create_name varchar(100)     null comment '创建人名',
    create_time datetime         null comment '创建时间',
    update_id   bigint           null comment '更新人id',
    update_name varchar(100)     null comment '更新人名',
    update_time datetime         null comment '更新时间',
    delete_flag bit default b'0' null comment '逻辑删除'
)
    comment '系统资源';

create table sys_role
(
    id          bigint           not null
        primary key,
    name        varchar(50)      not null comment '角色名称',
    remark      varchar(100)     null,
    tenant_id   bigint           null comment '租户id',
    create_id   bigint           null comment '创建人id',
    create_name varchar(100)     null comment '创建人名',
    create_time datetime         null comment '创建时间',
    update_id   bigint           null comment '更新人id',
    update_name varchar(100)     null comment '更新人名',
    update_time datetime         null comment '更新时间',
    delete_flag bit default b'0' null comment '逻辑删除'
)
    comment '系统角色';

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
    user_type   varchar(20)      not null comment '用户类型',
    tenant_id   bigint           null comment '租户id',
    create_id   bigint           null comment '创建人id',
    create_name varchar(100)     null comment '创建人名',
    create_time datetime         null comment '创建时间',
    update_id   bigint           null comment '更新人id',
    update_name varchar(100)     null comment '更新人名',
    update_time datetime         null comment '更新时间',
    delete_flag bit default b'0' null comment '逻辑删除',
    constraint sys_user_pk
        unique (account)
)
    comment '系统用户';

