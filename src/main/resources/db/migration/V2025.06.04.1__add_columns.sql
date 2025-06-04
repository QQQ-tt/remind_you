alter table sys_user
    add email varchar(100) null comment '邮箱' after encrypted_telephone;

alter table user_feedback
    add contact varchar(100) null comment '联系方式' after id;

alter table user_feedback
    drop column title;

alter table user_feedback
    modify type varchar(20) default 1 not null comment '1.suggestion 建议；2.issue 问题';

alter table user_feedback
    change content problem varchar(1000) null comment '具体内容';