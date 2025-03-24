create table exception_log
(
    id             bigint           not null
        primary key,
    exception_name varchar(100)     null comment '异常名称',
    url            varchar(200)     null comment '请求地址',
    parameter      text             null comment '请求参数',
    level          int default 1    null comment '异常等级',
    message        varchar(200)     null comment '异常信息',
    stack_trace    text             null comment '堆栈信息',
    tenant_id      bigint           null comment '租户id',
    create_id      bigint           null comment '创建人id',
    create_name    varchar(100)     null comment '创建人名',
    create_time    datetime         null comment '创建时间',
    update_id      bigint           null comment '更新人id',
    update_name    varchar(100)     null comment '更新人名',
    update_time    datetime         null comment '更新时间',
    delete_flag    bit default b'0' null comment '逻辑删除'
)
    comment '异常日志log';

create table request_log
(
    id              bigint           not null
        primary key,
    ip              varchar(20)      null comment '请求ip',
    url             varchar(100)     null comment '请求地址',
    method          varchar(20)      null comment '请求类型',
    time            datetime         null comment '请求时间',
    size            int              null comment '请求时长（毫秒数）',
    status_code     varchar(20)      null comment 'HTTP 状态码',
    response_length int              null comment '响应体大小（字节）',
    user_agent      text             null comment '客户端浏览器和操作系统信息',
    params          text             null comment '请求参数',
    headers         text             null comment '自定义请求头',
    tenant_id       bigint           null comment '租户id',
    create_id       bigint           null comment '创建人id',
    create_name     varchar(100)     null comment '创建人名',
    create_time     datetime         null comment '创建时间',
    update_id       bigint           null comment '更新人id',
    update_name     varchar(100)     null comment '更新人名',
    update_time     datetime         null comment '更新时间',
    delete_flag     bit default b'0' null comment '逻辑删除'
)
    comment '请求日志';

create table sys_resource
(
    id          bigint           not null
        primary key,
    name        varchar(50)      null comment '资源名称',
    url         varchar(500)     not null comment '接口地址/路由地址',
    type        varchar(20)      null comment '资源类型：路由、接口',
    icon        varchar(50)      null comment '图标',
    method      varchar(20)      null comment '方法类型',
    description varchar(200)     null comment '详细描述',
    status      bit default b'0' not null comment '资源状态',
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
    status      bit default b'0' null comment '角色状态',
    remark      varchar(100)     null comment '备注',
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

create table sys_role_resource
(
    id           bigint           not null
        primary key,
    sys_role_id  bigint           null,
    sys_resource bigint           null,
    tenant_id    bigint           null comment '租户id',
    create_id    bigint           null comment '创建人id',
    create_name  varchar(100)     null comment '创建人名',
    create_time  datetime         null comment '创建时间',
    update_id    bigint           null comment '更新人id',
    update_name  varchar(100)     null comment '更新人名',
    update_time  datetime         null comment '更新时间',
    delete_flag  bit default b'0' null comment '逻辑删除'
)
    comment '角色资源关联表';

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