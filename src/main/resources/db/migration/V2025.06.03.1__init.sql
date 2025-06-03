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

create table frequency
(
    id               bigint                        not null
        primary key,
    name             varchar(50)                   null comment '频次名称',
    frequency_code   varchar(50)                   null comment '频次编码',
    frequency_desc   varchar(200)                  null comment '频次描述',
    frequency_number int                           null comment '频次数目',
    frequency_cycle  int                           null comment '频次周期',
    cycle_unit       varchar(20)                   null comment '周期单位',
    type             varchar(20)                   null comment '开始方式',
    status           bit          default b'0'     null comment '是否启用',
    source           varchar(100) default 'system' null comment '来源',
    level            int          default 1        null comment '等级',
    tenant_id        bigint                        null comment '租户id',
    create_id        bigint                        null comment '创建人id',
    create_name      varchar(100)                  null comment '创建人名',
    create_time      datetime                      null comment '创建时间',
    update_id        bigint                        null comment '更新人id',
    update_name      varchar(100)                  null comment '更新人名',
    update_time      datetime                      null comment '更新时间',
    delete_flag      bit          default b'0'     null comment '逻辑删除'
)
    comment '频率';

create table frequency_detail
(
    id                bigint           not null
        primary key,
    frequency_id      bigint           not null,
    frequency_weekday int              null,
    frequency_time    varchar(20)      null comment '提醒时间(HH:mm)',
    before_rule_time  varchar(20)      null comment '首次提醒时间设置(前)(HH:mm)',
    after_rule_time   varchar(20)      null comment '首次提醒时间设置(后)(HH:mm)',
    tenant_id         bigint           null comment '租户id',
    create_id         bigint           null comment '创建人id',
    create_name       varchar(100)     null comment '创建人名',
    create_time       datetime         null comment '创建时间',
    update_id         bigint           null comment '更新人id',
    update_name       varchar(100)     null comment '更新人名',
    update_time       datetime         null comment '更新时间',
    delete_flag       bit default b'0' null comment '逻辑删除'
)
    comment '频次详情表(时间明细表)';

create table remind_task
(
    id           bigint           not null
        primary key,
    name         varchar(50)      not null comment '任务名称',
    start_time   datetime         null comment '时间范围提醒:开始时间',
    end_time     datetime         null comment '时间范围提醒:结束时间',
    push_num     int default 0    null comment '推送次数',
    num          int default 0    null comment '提醒次数(计算得出)',
    status       bit default b'0' null comment '任务状态',
    is_finish    bit default b'0' null comment '完成状态',
    remark       text             null comment '备注',
    is_remind    bit default b'1' null comment '是否提醒',
    remind_type  varchar(20)      not null comment '提醒方式',
    email        varchar(100)     null,
    telephone    varchar(13)      null comment '手机号',
    advance_num  int              null comment '提前时间的数量',
    cycle_unit   varchar(20)      null comment '单位:分钟,小时,天,周',
    frequency_id bigint           not null,
    tenant_id    bigint           null comment '租户id',
    create_id    bigint           null comment '创建人id',
    create_name  varchar(100)     null comment '创建人名',
    create_time  datetime         null comment '创建时间',
    update_id    bigint           null comment '更新人id',
    update_name  varchar(100)     null comment '更新人名',
    update_time  datetime         null comment '更新时间',
    delete_flag  bit default b'0' null comment '逻辑删除'
)
    comment '提醒任务';

create table remind_task_info
(
    id               bigint                    not null
        primary key,
    remind_task_id   bigint                    not null,
    remind_task_name varchar(64)               null,
    estimated_time   datetime                  not null comment '预计发送时间',
    actual_time      datetime                  null comment '实际发送时间',
    time             datetime                  not null comment '执行时间',
    remind_type      varchar(20)               null comment '提醒方式',
    email            varchar(100) default ''   null,
    status           bit          default b'0' null comment '状态',
    is_remind        bit          default b'0' null comment '是否提醒',
    is_send          bit          default b'0' null,
    is_read          bit          default b'0' null comment '是否已读',
    tenant_id        bigint                    null comment '租户id',
    create_id        bigint                    null comment '创建人id',
    create_name      varchar(100)              null comment '创建人名',
    create_time      datetime                  null comment '创建时间',
    update_id        bigint                    null comment '更新人id',
    update_name      varchar(100)              null comment '更新人名',
    update_time      datetime                  null comment '更新时间',
    delete_flag      bit          default b'0' null comment '逻辑删除'
)
    comment '任务执行详情数据';

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

create table rule_template
(
    id                   bigint           not null
        primary key,
    code                 varchar(50)      null comment '规则编码',
    name                 varchar(100)     null comment '规则名称',
    status               bit default b'0' null comment '是否启用',
    value                int default 10   null comment '默认值',
    rule_type            varchar(50)      not null comment '权益类型',
    interests_level      varchar(20)      not null comment '权益等级：vip_0,vip_1,ad_boost ',
    expired_period_value int              null comment '过期数值',
    expired_period_unit  varchar(20)      null comment '过期单位',
    expired_period_type  int              null comment '过期时间类型：1累计时间，2指定时间',
    priority             int              null comment '优先级，越小越先',
    description          varchar(100)     null comment '描述',
    tenant_id            bigint           null comment '租户id',
    create_id            bigint           null comment '创建人id',
    create_name          varchar(100)     null comment '创建人名',
    create_time          datetime         null comment '创建时间',
    update_id            bigint           null comment '更新人id',
    update_name          varchar(100)     null comment '更新人名',
    update_time          datetime         null comment '更新时间',
    delete_flag          bit default b'0' null comment '逻辑删除'
)
    comment '规则模板';

create table rule_user
(
    id               bigint           not null
        primary key,
    rule_template_id bigint           null,
    sys_user_id      bigint           null,
    name             varchar(100)     null comment '规则名称',
    use_value        int              null comment '已使用数量',
    value            int default 10   null comment '默认值',
    rule_type        varchar(20)      null comment '权益类型',
    started_at       datetime         null comment '开始时间',
    expired_at       datetime         null comment '过期时间',
    tenant_id        bigint           null comment '租户id',
    priority         int              null comment '优先级，越小越先',
    create_id        bigint           null comment '创建人id',
    create_name      varchar(100)     null comment '创建人名',
    create_time      datetime         null comment '创建时间',
    update_id        bigint           null comment '更新人id',
    update_name      varchar(100)     null comment '更新人名',
    update_time      datetime         null comment '更新时间',
    delete_flag      bit default b'0' null comment '逻辑删除'
);

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
    id              bigint           not null
        primary key,
    sys_role_id     bigint           null,
    sys_resource_id bigint           null,
    tenant_id       bigint           null comment '租户id',
    create_id       bigint           null comment '创建人id',
    create_name     varchar(100)     null comment '创建人名',
    create_time     datetime         null comment '创建时间',
    update_id       bigint           null comment '更新人id',
    update_name     varchar(100)     null comment '更新人名',
    update_time     datetime         null comment '更新时间',
    delete_flag     bit default b'0' null comment '逻辑删除'
)
    comment '角色资源关联表';

create table sys_user
(
    id                  bigint           not null
        primary key,
    name                varchar(50)      null comment '用户名称',
    avatar              text             null comment '头像',
    account             bigint           not null comment '账户',
    password            varchar(200)     not null comment '密码',
    telephone           varchar(13)      null comment '电话',
    encrypted_telephone varchar(20)      null comment '加密后的电话',
    status              bit default b'0' null comment '是否启用',
    sys_role_id         bigint           null comment '用户角色id',
    user_type           varchar(20)      not null comment '用户类型',
    interests_level     varchar(20)      null comment '权益等级：vip_0,vip_1 ',
    open_id             varchar(50)      null comment '微信用户唯一标识',
    msg_num             int default 0    null comment '消息订阅次数',
    user_info           json             null,
    shared_user_id      bigint           null comment '分享用户id',
    authorized          int default 1    null comment '是否同意授权',
    login_time          datetime         null comment '登录时间',
    tenant_id           bigint           null comment '租户id',
    create_id           bigint           null comment '创建人id',
    create_name         varchar(100)     null comment '创建人名',
    create_time         datetime         null comment '创建时间',
    update_id           bigint           null comment '更新人id',
    update_name         varchar(100)     null comment '更新人名',
    update_time         datetime         null comment '更新时间',
    delete_flag         bit default b'0' null comment '逻辑删除',
    constraint sys_user_pk
        unique (account)
)
    comment '系统用户';

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

create table user_feedback
(
    id          bigint           not null
        primary key,
    title       varchar(100)     null comment '标题',
    type        int default 1    not null comment '1.bug；2.意见',
    content     varchar(1000)    null comment '具体内容',
    adopted     int default 0    not null comment '是否采纳',
    reply       varchar(1000)    null comment '回复',
    tenant_id   bigint           null comment '租户id',
    create_id   bigint           null comment '创建人id',
    create_name varchar(100)     null comment '创建人名',
    create_time datetime         null comment '创建时间',
    update_id   bigint           null comment '更新人id',
    update_name varchar(100)     null comment '更新人名',
    update_time datetime         null comment '更新时间',
    delete_flag bit default b'0' null comment '逻辑删除'
)
    comment '意见反馈表';