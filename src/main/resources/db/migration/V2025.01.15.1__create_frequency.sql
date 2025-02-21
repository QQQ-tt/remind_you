create table frequency
(
    id               bigint                        not null
        primary key,
    frequency_name   varchar(50)                   null comment '频次名称',
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