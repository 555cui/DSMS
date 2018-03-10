create database DSMS;
use DSMS;
create table table_user(
    id nvarchar(36) primary key,
    name nvarchar(128) not null unique,
    password nvarchar(128) not null,

    createTime timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updateTime timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table table_role(
    id nvarchar(36) primary key,
    name nvarchar(128) not null unique,
    level int not null,
    description nvarchar(512),

    operator nvarchar(128) not null,
    createTime timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updateTime timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    foreign key(operator) references table_user(name) ON DELETE CASCADE ON UPDATE CASCADE
);

create table table_group(
    id nvarchar(36) primary key,
    name nvarchar(128) not null unique,
    description nvarchar(512),

    operator nvarchar(128) not null,
    createTime timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updateTime timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    foreign key(operator) references table_user(name) ON DELETE CASCADE ON UPDATE CASCADE
);

create table user_in_group(
    id nvarchar(36) not null unique,
    group_id nvarchar(36) not null,
    user_id nvarchar(36) not null,

    primary key(group_id, user_id),
    foreign key(group_id) references table_group(id) ON DELETE CASCADE,
    foreign key(user_id) references table_user(id) ON DELETE CASCADE,

    operator nvarchar(128) not null,
    createTime timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updateTime timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    foreign key(operator) references table_user(name) ON DELETE CASCADE ON UPDATE CASCADE
);

create table role_for_group(
    id nvarchar(36) not null unique,
    role_id nvarchar(36) not null,
    group_id nvarchar(36) not null,

    primary key(role_id, group_id),
    foreign key(role_id) references table_role(id) ON DELETE CASCADE,
    foreign key(group_id) references table_group(id) ON DELETE CASCADE,

    operator nvarchar(128) not null,
    createTime timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updateTime timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    foreign key(operator) references table_user(name) ON UPDATE CASCADE ON DELETE CASCADE
);

insert into table_user(id, name, password)
values('superuser', 'root', 'toor');

insert into table_role(id, name, level, operator)values
('default_select', 'select', 0, 'root'),
('default_update', 'update', 0, 'root'),
('default_delete', 'delete', 0, 'root'),
('default_insert', 'insert', 0, 'root');

insert into table_group(id, name, operator)
values('supergroup', 'root', 'root');

insert into role_for_group(id, group_id, role_id, operator)values
('00000000-0000-0000-0000-000000000000', 'supergroup', 'default_select', 'root'),
('00000000-0000-0000-0000-000000000001', 'supergroup', 'default_update', 'root'),
('00000000-0000-0000-0000-000000000002', 'supergroup', 'default_insert', 'root'),
('00000000-0000-0000-0000-000000000003', 'supergroup', 'default_delete', 'root');

insert into user_in_group(id, group_id, user_id, operator)
values('000000000-0000-0000-0000-000000000000', 'supergroup', 'superuser', 'root');