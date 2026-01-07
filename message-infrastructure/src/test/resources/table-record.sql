drop table if exists message_email_record;
create table message_email_record
(
    id            bigint                   not null
        primary key,
    type          varchar(8)               not null,
    folder        varchar(64)              not null,
    subject       varchar(256)             not null,
    from_address  jsonb                    not null,
    recipients    jsonb                    not null,
    sent_date     timestamp with time zone not null,
    received_date timestamp with time zone not null,
    content       text                     not null,
    files         jsonb                    not null,
    create_by     bigint,
    update_by     bigint,
    create_time   timestamp with time zone,
    update_time   timestamp with time zone,
    del_flag      boolean,
    message_id    varchar(256)             not null
);

comment on table message_email_record is '邮件记录';

comment on column message_email_record.type is '类型';

comment on column message_email_record.folder is '文件夹';

comment on column message_email_record.subject is '主题';

comment on column message_email_record.from_address is '发送人json字符串数组';

comment on column message_email_record.recipients is '接收人json字符串数组';

comment on column message_email_record.sent_date is '发送时间';

comment on column message_email_record.received_date is '接收时间';

comment on column message_email_record.content is '内容';

comment on column message_email_record.files is '附件信息json';

comment on column message_email_record.message_id is '邮件Id';

create index
    on message_email_record (message_id);

create index
    on message_email_record (received_date);

