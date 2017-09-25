
/*==============================================================*/
/* Table: t_mail                                                */
/*==============================================================*/
create table t_mail
(
  id                   bigint unsigned not null comment '主键',
  send_type            tinyint unsigned not null comment '发送类型:0异步实时发送、1同步实时发送、2定时发送',
  sender               varchar(200) not null comment '发件人',
  subject              varchar(200) not null comment '主题',
  receiver             varchar(500) not null comment '收件人,多个收件人以英文;分割组成的字符串',
  copy_to              varchar(500) comment '抄送,多个抄送人以英文;分割组成的字符串',
  blind_copy_to        varchar(500) comment '密送,多个密送人以英文;分割组成的字符串',
  template_name        varchar(100) comment '邮件模板名称',
  template_data        varchar(5000) comment '模板邮件中的数据',
  content              varchar(5000) comment '邮件内容',
  is_contains_attachment tinyint unsigned not null comment '是否有附件:0:不带附件 1:带附件的邮件',
  priority             tinyint unsigned comment '优先级 1~5',
  required_send_time   datetime not null comment '要求发送时间',
  send_status          tinyint unsigned not null comment '发送状态:0待发送，1发送中，2发送成功，3发送失败',
  send_time            datetime comment '实际发送时间',
  failure_reason       varchar(5000) comment '发送失败原因',
  failure_times        tinyint unsigned comment '发送失败次数',
  is_active            tinyint unsigned not null comment '是否有效，用于标识数据是否已被逻辑删除,1表示有效、未删除，0表示无效、已删除',
  create_time          datetime not null comment '创建时间',
  create_user_code     varchar(50) not null comment '创建人',
  modify_time          datetime comment '修改时间',
  modify_user_code     varchar(50) comment '修改人',
  primary key (id)
);

alter table t_mail comment '邮件发送表';

/*==============================================================*/
/* Index: idx_mail_required_send_time                           */
/*==============================================================*/
create index idx_mail_required_send_time on t_mail
(
  required_send_time
);

/*==============================================================*/
  /* Table: t_mail_attachment                                     */
  /*==============================================================*/
create table t_mail_attachment
(
  id                   bigint unsigned not null comment '主键',
  mail_id              bigint unsigned not null comment '对应邮件主表的主键id',
  attachment_name      varchar(200) comment '附件名称',
  attachment_order     tinyint unsigned not null comment '附件添加到邮件中的顺序',
  attachment_url       varchar(500) not null comment '附件所在的ftp服务器地址',
  is_active            tinyint unsigned not null comment '是否有效，用于标识数据是否已被逻辑删除,1表示有效、未删除，0表示无效、已删除',
  create_time          datetime not null comment '创建时间',
  create_user_code     varchar(50) not null comment '创建人',
  modify_time          datetime comment '修改时间',
  modify_user_code     varchar(50) comment '修改人',
primary key (id)
);

alter table t_mail_attachment comment '邮件附件明细';

/*==============================================================*/
/* Index: idx_mail_attachment_mail_id                           */
/*==============================================================*/
create index idx_mail_attachment_mail_id on t_mail_attachment
(
  mail_id
);


/*==============================================================*/
/* Table: t_mail_last_send_time                                 */
/*==============================================================*/
create table t_mail_last_send_time
(
  id                   bigint unsigned not null comment '主键',
  last_exec_time       datetime not null comment '上次执行时间',
  is_active            tinyint unsigned not null comment '是否有效，用于标识数据是否已被逻辑删除,1表示有效、未删除，0表示无效、已删除',
  create_time          datetime not null comment '创建时间',
  create_user_code     varchar(50) not null comment '创建人',
  modify_time          datetime comment '修改时间',
  modify_user_code     varchar(50) comment '修改人',
  primary key (id)
);

alter table t_mail_last_send_time comment '邮件发送上次调度时间记录';
