# 数据库初始化

-- 创建库
create database if not exists gpt_db;

-- 切换库
use gpt_db;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除'
) comment '用户' collate = utf8mb4_unicode_ci;

-- 对话表
create table if not exists conversation
(
    id           bigint auto_increment comment 'id' primary key,
    userId       bigint                                 not null comment '用户id',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    title        varchar(256) default ''            not null comment '标题',
    hasTalked    tinyint      default 0                 not null comment '是否已经对话',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    foreign key (userId) references user (id),
    index (userId)
) comment '对话' collate = utf8mb4_unicode_ci;

-- 消息表，用户对llm的消息
create table if not exists message
(
    id           bigint auto_increment comment 'id' primary key,
    conversationId bigint                                 not null comment '对话id',
    content        varchar(1024)                          not null comment '内容',
    createTime     datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    foreign key (conversationId) references conversation (id)
) comment '消息' collate = utf8mb4_unicode_ci;

-- 消息表，llm对用户的消息
create table if not exists response
(
    id           bigint auto_increment comment 'id' primary key,
    messageId bigint                                 not null comment '询问id',
    content        varchar(1024)                          not null comment '内容',
    createTime     datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    foreign key (messageId) references message (id),
    index (messageId)
) comment '消息' collate = utf8mb4_unicode_ci;