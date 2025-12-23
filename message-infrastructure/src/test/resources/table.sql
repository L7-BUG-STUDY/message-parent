drop table if exists public.message_email_config;

create table public.message_email_config
(
    id          bigint      not null
        primary key,
    type        varchar(16) not null default '',
    username    varchar(64) not null default '',
    password    varchar(64) not null default '',
    connection  boolean     not null default false,
    create_by   bigint,
    update_by   bigint,
    create_time timestamptz,
    update_time timestamptz,
    del_flag    boolean              default false
);

comment on table public.message_email_config is '邮箱配置表';


comment on column public.message_email_config.username is '用户名';

comment on column public.message_email_config.password is '密码';

create index system_users_username_idx
    on public.message_email_config (username);
