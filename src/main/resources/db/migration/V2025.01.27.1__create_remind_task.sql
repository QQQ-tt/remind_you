create table remind_task
(
    id           bigint           not null
        primary key,
    name         varchar(50)      not null comment '任务名称',
    start_time   datetime         null comment '时间范围提醒:开始时间',
    end_time     datetime         null comment '时间范围提醒:结束时间',
    push_num     int default 0    null comment '推送次数',
    num          int              null comment '提醒次数(计算得出)',
    status       bit default b'0' null comment '任务状态',
    remark       text             null comment '备注',
    is_remind    bit default b'1' null comment '是否提醒',
    remind_type  varchar(20)      not null comment '提醒方式',
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
    id             bigint           not null
        primary key,
    remind_task_id bigint           not null,
    estimated_time datetime         not null comment '预计发送时间',
    actual_time    datetime         null comment '实际发送时间',
    time           datetime         not null comment '执行时间',
    remind_type    varchar(20)      null comment '提醒方式',
    status         bit default b'0' null comment '状态',
    is_remind      bit default b'0' null comment '是否提醒',
    is_send        bit default b'0' null,
    is_read        bit default b'0' null comment '是否已读',
    tenant_id      bigint           null comment '租户id',
    create_id      bigint           null comment '创建人id',
    create_name    varchar(100)     null comment '创建人名',
    create_time    datetime         null comment '创建时间',
    update_id      bigint           null comment '更新人id',
    update_name    varchar(100)     null comment '更新人名',
    update_time    datetime         null comment '更新时间',
    delete_flag    bit default b'0' null comment '逻辑删除'
)
    comment '任务执行详情数据';

create table remind_task_his
(
    id           bigint           not null
        primary key,
    name         varchar(50)      not null comment '任务名称',
    start_time   datetime         null comment '时间范围提醒:开始时间',
    end_time     datetime         null comment '时间范围提醒:结束时间',
    push_num     int default 0    null comment '推送次数',
    num          int              null comment '提醒次数(计算得出)',
    status       bit default b'0' null comment '任务状态',
    remark       text             null comment '备注',
    is_remind    bit default b'1' null comment '是否提醒',
    remind_type  varchar(20)      not null comment '提醒方式',
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

create table remind_task_info_his
(
    id             bigint           not null
        primary key,
    remind_task_id bigint           not null,
    estimated_time datetime         not null comment '预计发送时间',
    actual_time    datetime         null comment '实际发送时间',
    time           datetime         not null comment '执行时间',
    remind_type    varchar(20)      null comment '提醒方式',
    status         bit default b'0' null comment '状态',
    is_remind      bit default b'0' null comment '是否提醒',
    is_send        bit default b'0' null,
    is_read        bit default b'0' null comment '是否已读',
    tenant_id      bigint           null comment '租户id',
    create_id      bigint           null comment '创建人id',
    create_name    varchar(100)     null comment '创建人名',
    create_time    datetime         null comment '创建时间',
    update_id      bigint           null comment '更新人id',
    update_name    varchar(100)     null comment '更新人名',
    update_time    datetime         null comment '更新时间',
    delete_flag    bit default b'0' null comment '逻辑删除'
)
    comment '任务执行详情数据';



